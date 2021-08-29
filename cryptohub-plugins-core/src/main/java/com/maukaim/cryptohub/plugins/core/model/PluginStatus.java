package com.maukaim.cryptohub.plugins.core.model;


public enum PluginStatus {
    /**
     * Plugin has been created, but not yet resolved.
     * PluginService can't use it right now.
     */
    CREATED,

    /**
     * All the dependencies are now created and resolved.
     * Can now be started, meaning turned into {@code ENABLED}
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
    ERROR
}
