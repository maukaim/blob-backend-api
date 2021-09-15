package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.core.model.Plugin;

import java.io.File;
import java.io.IOException;
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
