package com.maukaim.cryptohub.plugin;

import com.maukaim.cryptohub.plugins.core.PluginLifeCycleException;
import com.maukaim.cryptohub.plugins.core.PluginLifeCycleListener;
import com.maukaim.cryptohub.plugins.core.PluginRepository;
import com.maukaim.cryptohub.plugins.core.model.Plugin;
import com.maukaim.cryptohub.plugins.core.model.PluginStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PluginAutoEnableListener implements PluginLifeCycleListener {

    private final PluginRepository pluginRepository;

    public PluginAutoEnableListener(PluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    @Override
    public void beforeStatusChange(Plugin plugin, PluginStatus current, PluginStatus next) throws PluginLifeCycleException {
        // Ignored
    }

    @Override
    public void beforeDestroy(Plugin plugin) throws PluginLifeCycleException {
        // Ignored
    }

    @Override
    public void afterStatusChanged(Plugin plugin, PluginStatus old, PluginStatus current) {
        if (current.isStarted()) {
            this.addAutoEnable(plugin);
        } else {
            this.removeAutoEnable(plugin);
        }
    }

    @Override
    public void afterDestroy(Plugin plugin) {
        this.removeAutoEnable(plugin);
    }

    private void addAutoEnable(Plugin plugin) {
        this.pluginRepository.addAutoEnable(plugin);

    }

    private void removeAutoEnable(Plugin plugin) {
        this.pluginRepository.removeAutoEnable(plugin);
    }

}
