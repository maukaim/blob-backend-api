package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.core.model.PluginDependency;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

/**
 * TODO: Implement a child first load strategy +
 * Think about security as here : https://medium.com/@isuru89/java-a-child-first-class-loader-cbd9c3d0305
 */
public class ChildFirstClassLoader extends URLClassLoader {

    private PluginService pluginService;
    private List<PluginDependency> pluginDependencies;

    //TODO: --->>> pluginService and pluginInfo useful for Dependencies management !
    public ChildFirstClassLoader(PluginService pluginService, List<PluginDependency> pluginDependencies, ClassLoader parent) {
        super(new URL[0], parent);
        this.pluginDependencies = pluginDependencies;
        this.pluginService = pluginService;
    }

    public void addPath(Path path) {
        try {
            addURL(path.toFile().getCanonicalFile().toURI().toURL());
        } catch (IOException e) {
//            log.error(e.getMessage(), e);
        }
    }

}
