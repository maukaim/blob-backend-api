package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.core.model.PluginLoadResult;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface PluginLoader {
    PluginLoadResult loadPlugins(Set<File> jarFiles);

}
