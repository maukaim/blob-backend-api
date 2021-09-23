package com.maukaim.blob.plugins.core;

import com.maukaim.blob.plugins.core.model.PluginLoadResult;

import java.io.File;
import java.util.Set;

public interface PluginLoader {
    PluginLoadResult loadPlugins(Set<File> jarFiles);

}
