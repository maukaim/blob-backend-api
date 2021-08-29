package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.core.model.Plugin;

import java.nio.file.Path;
import java.util.List;

public interface PluginJarFinder {
    List<Plugin> findPlugins();
    List<Plugin> findPlugins(List<Path> paths);
    void addSearchPath(Path path);
    void removePath(Path path);
}
