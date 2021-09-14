package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.core.model.*;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleException;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleProvider;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleProviderFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Slf4j
public class PluginLoaderImpl implements PluginLoader {

    @Override
    public PluginLoadResult loadPlugins(Set<File> jarFiles) {
        if (jarFiles.isEmpty()) {
            return PluginLoadResult.builder().nbOfJars(0).build();
        }

        List<Plugin> pluginsLoaded = new ArrayList<>();
        List<Exception> exceptionsOccurred = new ArrayList<>();
        for (File jf : jarFiles) {
            try (JarFile jarFile = new JarFile(jf)) {
                PluginInfo info = PluginFactory.buildInfo(jarFile);
                Path filePath = jf.toPath();

                ChildFirstClassLoader loader = new ChildFirstClassLoader(getClass().getClassLoader());
                loader.addPath(filePath);

                Plugin plugin = PluginFactory.build(info, loader, filePath, Collections.emptyList());
                List<ModuleProvider<? extends Module>> moduleProviders = ModuleProviderFactory.build(plugin);
                plugin.setModuleProviders(moduleProviders);
                pluginsLoaded.add(plugin);

            } catch (IOException e) {
                log.error("Impossible to convert File {} into an url object.", jf.getAbsolutePath(), e);
                exceptionsOccurred.add(e);
            } catch (PluginMalformedException | ModuleException e) {
                exceptionsOccurred.add(e);
            }
        }
        log.info("Errors occurred: {}",
                exceptionsOccurred.stream().map(Exception::getMessage)
                        .collect(Collectors.joining("\n")));
        return PluginLoadResult.builder()
                .nbOfJars(jarFiles.size())
        .exceptions(exceptionsOccurred)
                .plugins(pluginsLoaded)
                .build();
    }

}
