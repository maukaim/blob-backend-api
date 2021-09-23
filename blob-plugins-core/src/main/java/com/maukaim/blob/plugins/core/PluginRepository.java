package com.maukaim.blob.plugins.core;

import com.maukaim.blob.plugins.core.model.Plugin;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface PluginRepository {
    Set<File> getPlugins(List<Path> filePaths);
    void addAutoEnable(Plugin plugin);
    void removeAutoEnable(Plugin plugin);
    void destroyPlugin(Plugin plugin) throws Exception;
    Set<String> getEnabledPluginIds(Path path);
}
