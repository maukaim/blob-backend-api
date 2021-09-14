package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.core.model.Plugin;
import com.maukaim.cryptohub.plugins.core.utils.FileSystemUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class PluginRepositoryImpl implements PluginRepository {
    @Override
    public Set<File> getPlugins(List<Path> filePaths) {
        return FileSystemUtils.scanForJars(filePaths);
    }

    @Override
    public void addAutoEnable(Plugin plugin) {
        Path pluginDirectoryPath = plugin.getPluginPath().getParent();
        Set<String> enabledPluginIds = FileSystemUtils.getEnabledPluginIds(pluginDirectoryPath);
        String pluginId = plugin.getInfo().getPluginId();
        if (!enabledPluginIds.contains(pluginId)) {
            FileSystemUtils.writeEnabledPluginIfAbsent(pluginId, pluginDirectoryPath);
        }
    }

    @Override
    public void removeAutoEnable(Plugin plugin) {
        String pluginId = plugin.getInfo().getPluginId();
        Set<Path> autoEnableFilePaths = FileSystemUtils.getEnabledPluginFilesPaths(plugin.getPluginPath().getParent());
        Set<Path> autoEnableFilesHavingThisPluginId = autoEnableFilePaths.stream()
                .collect(Collectors.toMap(path -> path,
                        FileSystemUtils::getEnabledPluginIds))
                .entrySet().stream()
                .filter(pluginIdsByPaths -> pluginIdsByPaths.getValue().contains(pluginId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        autoEnableFilesHavingThisPluginId.forEach( path-> FileSystemUtils.removePluginIdFromFile(pluginId,path));
    }

    @Override
    public void destroyPlugin(Plugin plugin) throws Exception {
        boolean isDestroyed = Files.deleteIfExists(plugin.getPluginPath());
        if(!isDestroyed){
            throw new PluginLifeCycleException("We tried to destroy %s, but it does not exist.", true);
        }

    }

    @Override
    public Set<String> getEnabledPluginIds(Path path) {
        return FileSystemUtils.getEnabledPluginIds(path);
    }
}
