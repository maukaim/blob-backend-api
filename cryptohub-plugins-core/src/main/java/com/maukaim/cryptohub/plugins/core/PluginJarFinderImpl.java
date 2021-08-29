package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.core.model.Plugin;
import com.maukaim.cryptohub.plugins.core.model.PluginFactory;
import com.maukaim.cryptohub.plugins.core.model.PluginInfo;
import com.maukaim.cryptohub.plugins.core.model.PluginMalformedException;
import com.maukaim.cryptohub.plugins.core.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarFile;

@Slf4j
public class PluginJarFinderImpl implements PluginJarFinder {
    private List<Path> rootPaths;
    private PluginService pluginService;


    public PluginJarFinderImpl(PluginService pluginService, Path... pluginRoots) {
        this(Arrays.asList(pluginRoots), pluginService);
    }

    public PluginJarFinderImpl(List<Path> pluginRoots, PluginService pluginService) {
        this.rootPaths = Objects.requireNonNullElse(pluginRoots, new ArrayList<>());
        this.pluginService = pluginService;
    }

    @Override
    public List<Plugin> findPlugins() {
        return this.findPlugins(this.rootPaths);
    }

    @Override
    public List<Plugin> findPlugins(List<Path> paths) {
        Set<File> jarFiles = FileUtils.scanForJars(paths);
        if (jarFiles.isEmpty()) {
            return Collections.emptyList();
        }
        //TODO: Plutot que simplement compter ceux qui on load... peut etre faire une public internal class SearchResult?

        int pluginJarsLoaded = 0;
        List<Plugin> pluginsLoaded = new ArrayList<>();
        for (File jf : jarFiles) {
            try (JarFile jarFile = new JarFile(jf)) {
                PluginInfo info = PluginFactory.buildInfo(jarFile);
                Path filePath = jf.toPath();

                //TODO: Add Dependencies to PluginInfo to provide it to Loader
                ChildFirstClassLoader loader = new ChildFirstClassLoader(this.pluginService, Collections.emptyList(), getClass().getClassLoader());
                loader.addPath(filePath); //Associate this filePath to the newly created Loader. It can now load classes from here

                //TODO: Add ModuleProviders creation
                PluginFactory.build(info, loader, filePath, Collections.emptyList());

                pluginJarsLoaded++;
            } catch (IOException e) {
//                log.error("Impossible to convert File {} into an url object.", jf.getAbsolutePath(), e);
            } catch (PluginMalformedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void addSearchPath(Path path) {

    }

    @Override
    public void removePath(Path path) {

    }
}
