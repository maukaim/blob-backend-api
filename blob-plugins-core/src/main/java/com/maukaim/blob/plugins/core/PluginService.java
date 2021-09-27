package com.maukaim.blob.plugins.core;

import com.maukaim.blob.plugins.core.model.Plugin;
import com.maukaim.blob.plugins.core.model.PluginLoadResult;
import com.maukaim.blob.plugins.core.model.module.ModuleProvider;
import com.maukaim.blob.plugins.api.plugin.Module;

import java.util.Collection;
import java.util.List;

public interface PluginService {


    <T extends Module> List<ModuleProvider<? extends T>> getProviders(Class<T> moduleInterface);

    Plugin get(String pluginId);
    Collection<Plugin> getPlugins();

    Collection<Plugin> loadPlugins();
    PluginLoadResult getLastLoadingResult();

    StatusChangeResult start(String pluginId);
    StatusChangeResult stop(String pluginId);
    DestroyResult destroy(String pluginId);

    void addLifeCycleListener(PluginLifeCycleListener listener, ModuleProvider<? extends Module> module);
    void addLifeCycleListener(PluginLifeCycleListener listener, String pluginId);

}
