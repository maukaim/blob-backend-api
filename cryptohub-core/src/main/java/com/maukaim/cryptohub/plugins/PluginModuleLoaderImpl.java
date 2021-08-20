package com.maukaim.cryptohub.plugins;

import com.google.common.collect.Maps;
import com.maukaim.cryptohub.commons.module.ModuleDeclarator;
import com.maukaim.cryptohub.plugins.model.PluginModule;
import com.maukaim.cryptohub.plugins.model.PluginModuleFactory;
import com.maukaim.cryptohub.plugins.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PluginModuleLoaderImpl implements PluginModuleLoader {
    private String pluginDirectory;
    private List<PluginModuleManager> managers;
    private Map<Class<? extends ModuleDeclarator>, List<PluginModuleManager>> managersByDeclarator = Maps.newConcurrentMap();
    private Map<String, List<PluginModule>> plugins = Maps.newConcurrentMap();

    @PostConstruct
    void init() {
        log.info("Initializing PluginLoaderImpl...");
        log.info("Classify managers by declarators");
        initManagers();
        discoverAllPlugins();

        log.info("PluginLoaderImpl initialized !");
    }

    @Autowired
    public PluginModuleLoaderImpl(List<PluginModuleManager> managers,
                                  @Value("cryptohub.plugins.repository") String pluginDirectory) {
        this.managers = Objects.requireNonNullElse(managers, new ArrayList<>());
        this.pluginDirectory = pluginDirectory;

    }

    private void initManagers() {
        this.managers.forEach(manager -> {
            List<Class<? extends ModuleDeclarator>> managedDeclaratorType = manager.getManagedDeclaratorClasses();
            managedDeclaratorType.forEach(declaratorType -> this.addManager(declaratorType, manager));
        });
    }

    private void addManager(Class<? extends ModuleDeclarator> declaratorType, PluginModuleManager manager) {
        log.info("Add manager {} for declarator type {}", manager.getClass().getSimpleName(), declaratorType.getSimpleName());
        this.managersByDeclarator.computeIfPresent(declaratorType, (key, val) -> {
            val.add(manager);
            return val;
        });
        this.managersByDeclarator.computeIfAbsent(declaratorType, key -> List.of(manager));
    }

    @Override
    public void unload(List<PluginModule> pluginModules) {
        for (PluginModule plugin : pluginModules) {
            if (this.plugins.containsKey(plugin.getJarPath())) {
                this.plugins.get(plugin.getJarPath()).remove(plugin);
                this.notifyRemove(List.of(plugin));

                if (this.plugins.get(plugin.getJarPath()).isEmpty()) {
                    log.info("Removed all plugins from JAR at path {}, will remove JAR.", plugin.getJarPath());
                }

            } else {
                log.error("Impossible to unload plugin {} as cache does not recognize its filePath {}",
                        plugin.getName(),
                        plugin.getJarPath());
            }
        }
    }

    @Scheduled(cron = "0 0/5 * 1/1 * ?")
    @Override
    public void discoverAllPlugins() {
        discover(this.pluginDirectory);
    }

    @Override
    public List<PluginModule> getByPath(String uriJar) throws NoPluginModuleForPathException {
        return null;
    }

    @Override
    public void discover(String pathURI) {
        //1 lance scan des .jar
        List<File> jarFiles = FileUtils.scanForJars(pathURI);
        //2 load les jars.
        if (jarFiles.isEmpty()) {
            log.info("No plugins available in directory: {}", pathURI);
            return;
        }

        int pluginJarsLoaded = 0;
        for (File jf : jarFiles) {
            try {
                Boolean loaded = loadJarFileContent(jf);
                if (loaded) ++pluginJarsLoaded;
            } catch (MalformedURLException e) {
                log.error("Impossible to convert File {} into a url object.", jf.getAbsolutePath(), e);
            }
        }

        if (pluginJarsLoaded == 0) {
            log.info("No plugin JARs loaded on {} found.", jarFiles.size());
        } else {
            log.info("{}/{} JARs loaded", pluginJarsLoaded, jarFiles.size());
        }
    }

    @Override
    public List<PluginModule> getPlugins(Class<? extends ModuleDeclarator> declaratorClazz) {
        return null;
    }

    private Boolean loadJarFileContent(File jf) throws MalformedURLException {
        URL urlFile = jf.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{urlFile});
        String filePath = jf.getAbsolutePath();

        if (!this.plugins.containsKey(filePath)) {
            return this.storeNewPlugin(filePath, loader);
        } else {
            log.info("Already loaded {}, won't reload it", filePath);
            return false;
        }
    }

    private Boolean storeNewPlugin(String absolutePath, URLClassLoader loader) {
        try {
            List<PluginModule> pluginsLoaded = this.loadPlugins(absolutePath, loader);
            this.plugins.computeIfAbsent(absolutePath, key -> {
                this.notifyNew(pluginsLoaded);
                return new ArrayList<>() {{
                    addAll(pluginsLoaded);
                }};
            });
            return true;
        } catch (IOException e) {
            log.error("Impossible to convert file {} to a JarFile", absolutePath, e);
        } catch (NoPluginEntryPointException e) {
            log.error("No PluginEntry implementation found in jar JarFile", absolutePath, e);
        }
        return false;
    }

    private void notifyNew(List<PluginModule> pluginsLoaded) {
        this.managersByDeclarator.keySet()
                .forEach(declaratorClazz ->
                        pluginsLoaded.stream()
                                .filter(plugin -> plugin.getDeclarator().isAssignableFrom(declaratorClazz))
                                .forEach(plugin ->
                                        this.managersByDeclarator.getOrDefault(declaratorClazz, Collections.emptyList())
                                                .forEach(manager -> manager.onNewPlugin(plugin, declaratorClazz))
                                )
                );
    }

    private void notifyRemove(List<PluginModule> unloadedPlugins) {
        unloadedPlugins.stream()
                .forEach(plugin -> {
                    Class<? extends ModuleDeclarator> clazz = plugin.getDeclarator();
                    this.managersByDeclarator.getOrDefault(clazz, Collections.emptyList())
                            .forEach(manager -> manager.onRemoved(plugin, clazz));
                });
    }

    private List<PluginModule> loadPlugins(String jarFilePath, URLClassLoader loader) throws IOException, NoPluginEntryPointException {
        JarFile jarFile = new JarFile(jarFilePath);
        log.info("Loading plugin from jarfile {} ...", jarFile.getName());

        Enumeration<JarEntry> entries = jarFile.entries();
        List<Class<?>> classesLoaded = this.loadClasses(entries, loader);

        List<Class<? extends ModuleDeclarator>> pluginDeclarators = classesLoaded.stream()
                .filter(clazz -> clazz.isAssignableFrom(ModuleDeclarator.class))
                .map(clazz -> {
                    Class<? extends ModuleDeclarator> subClass = clazz.asSubclass(ModuleDeclarator.class);
                    return subClass;
                })
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(pluginDeclarators))
            throw new NoPluginEntryPointException(
                    String.format("No %s found in JarFile located at %s. Classes & loader not stored.",
                            ModuleDeclarator.class.getSimpleName(),
                            jarFilePath));

        List<PluginModule> pluginsLoaded = PluginModuleFactory.build(pluginDeclarators, classesLoaded, loader, jarFilePath);

        return Objects.requireNonNullElse(pluginsLoaded, Collections.emptyList());
    }

    private List<Class<?>> loadClasses(Enumeration<JarEntry> entries, URLClassLoader loader) {
        List<Class<?>> classesLoaded = new ArrayList<>();

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String jarSubFileURI = jarEntry.toString();

            if (FilenameUtils.getExtension(jarSubFileURI).equalsIgnoreCase(FileUtils.CLASS_EXTENSION)) {
                String jarSubFileClassPath = FilenameUtils.removeExtension(jarSubFileURI).replaceAll("/", ".");
                try {
                    Class<?> clazz = Class.forName(jarSubFileClassPath, true, loader);
                    loader.loadClass(jarSubFileClassPath);
                    classesLoaded.add(clazz);
                } catch (ClassNotFoundException e) {
                    log.error("No public class {} found from Jar entry {}.", jarSubFileClassPath, jarSubFileURI, e);
                }
            } else {
                log.info("{} ignored -> not a class file.", jarSubFileURI);
            }
        }

        return classesLoaded;
    }

}
