package com.maukaim.cryptohub.plugins.core.model;


import java.util.Objects;

public enum PluginStatus {
    /**
     * Plugin has been created, but not yet resolved.
     * PluginService can't use it right now.
     */
    CREATED,

    /**
     * Has been preprocessed successfully just after loading.
     * Can now be started, meaning go to {@code ENABLED}.
     */
    AVAILABLE,

    /**
     * Plugin is enabled(started), Its modules are available across the application.
     *
     * @link PluginService} should propagate its modules.
     */
    ENABLED,

    /**
     * Plugin is disabled (stopped), PluginService won't make it available or execute anything from it.
     */
    DISABLED,

    /**
     * Error happened while starting the plugin.
     */
    ERROR;

    public boolean isStarted() {
        return this == ENABLED;
    }

    public boolean canBeStarted() {
        return !(isStarted() || Objects.equals(CREATED, this));
    }

    public boolean canBeStopped() {
        return isStarted();
    }

    public boolean isDestroyable() {
        return !isStarted();

    }
}
