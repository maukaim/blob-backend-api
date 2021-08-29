package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.core.model.Plugin;
import com.maukaim.cryptohub.plugins.core.model.PluginStatus;

public interface PluginLifeCycleListener {


    void beforeStart(Plugin plugin);
    void beforeStatusChanged(Plugin plugin, PluginStatus current, PluginStatus next);
    void beforeStop(Plugin plugin);
    void beforeDestroy(Plugin plugin);

    void afterResolve(Plugin plugin);
    void afterStart(Plugin plugin);
    void afterStatusChanged(Plugin plugin, PluginStatus old, PluginStatus current);
    void afterStop(Plugin plugin);
    void afterDestroy(Plugin plugin);
}
