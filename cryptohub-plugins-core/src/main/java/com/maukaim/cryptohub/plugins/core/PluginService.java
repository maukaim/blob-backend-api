package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleProvider;
import com.maukaim.cryptohub.plugins.core.model.Plugin;

import java.nio.file.Path;
import java.util.List;

public interface PluginService {


    <T extends Module>List<ModuleProvider<? extends T>> getProviders(Class<T> moduleInterface);

    Plugin get(String pluginId);
    List<Plugin> getPlugins();

    List<Plugin> loadPlugins();
    Plugin loadPlugin(Path pluginPath);

    void start(Plugin plugin);
    void stop(Plugin plugin);
    void destroy(Plugin plugin);

    void addLifeCycleListener(PluginLifeCycleListener listener, ModuleProvider<Module> module);
    void addLifeCycleListener(PluginLifeCycleListener listener, Plugin plugin);
//    /**
//     * Retrieves all plugins.
//     */
//    List<Plugin> getPlugins();
//
//    /**
//     * Retrieves all plugins with this state.
//     */
//    List<Plugin> getPlugins(PluginState pluginState);
//
//    /**
//     * Retrieves all resolved plugins (with resolved dependency).
//     */
//    List<Plugin> getResolvedPlugins();
//
//    /**
//     * Retrieves all unresolved plugins (with unresolved dependency).
//     */
//    List<Plugin> getUnresolvedPlugins();
//
//    /**
//     * Retrieves all started plugins.
//     */
//    List<Plugin> getStartedPlugins();
//
//    /**
//     * Retrieves the plugin with this id, or null if the plugin does not exist.
//     *
//     * @param pluginId the unique plugin identifier, specified in its metadata
//     * @return A Plugin object for this plugin, or null if it does not exist.
//     */
//    Plugin getPlugin(String pluginId);
//
//    /**
//     * Load plugins.
//     */
//    void loadPlugins();
//
//    /**
//     * Load a plugin.
//     *
//     * @param pluginPath the plugin location
//     * @return the pluginId of the installed plugin as specified in its {@linkplain PluginDescriptor metadata}
//     * @throws PluginRuntimeException if something goes wrong
//     */
//    String loadPlugin(Path pluginPath);
//
//    /**
//     * Start all active plugins.
//     */
//    void startPlugins();
//
//    /**
//     * Start the specified plugin and its dependencies.
//     *
//     * @return the plugin state
//     * @throws PluginRuntimeException if something goes wrong
//     */
//    PluginState startPlugin(String pluginId);
//
//    /**
//     * Stop all active plugins.
//     */
//    void stopPlugins();
//
//    /**
//     * Stop the specified plugin and its dependencies.
//     *
//     * @return the plugin state
//     * @throws PluginRuntimeException if something goes wrong
//     */
//    PluginState stopPlugin(String pluginId);
//
//    /**
//     * Unload all plugins
//     */
//    void unloadPlugins();
//
//    /**
//     * Unload a plugin.
//     *
//     * @param pluginId the unique plugin identifier, specified in its metadata
//     * @return true if the plugin was unloaded
//     * @throws PluginRuntimeException if something goes wrong
//     */
//    boolean unloadPlugin(String pluginId);
//
//    /**
//     * Disables a plugin from being loaded.
//     *
//     * @param pluginId the unique plugin identifier, specified in its metadata
//     * @return true if plugin is disabled
//     * @throws PluginRuntimeException if something goes wrong
//     */
//    boolean disablePlugin(String pluginId);
//
//    /**
//     * Enables a plugin that has previously been disabled.
//     *
//     * @param pluginId the unique plugin identifier, specified in its metadata
//     * @return true if plugin is enabled
//     * @throws PluginRuntimeException if something goes wrong
//     */
//    boolean enablePlugin(String pluginId);
//
//    /**
//     * Deletes a plugin.
//     *
//     * @param pluginId the unique plugin identifier, specified in its metadata
//     * @return true if the plugin was deleted
//     * @throws PluginRuntimeException if something goes wrong
//     */
//    boolean deletePlugin(String pluginId);
//
//    ClassLoader getPluginClassLoader(String pluginId);
//
//    List<Class<?>> getExtensionClasses(String pluginId);
//
//    <T> List<Class<? extends T>> getExtensionClasses(Class<T> type);
//
//    <T> List<Class<? extends T>> getExtensionClasses(Class<T> type, String pluginId);
//
//    <T> List<T> getExtensions(Class<T> type);
//
//    <T> List<T> getExtensions(Class<T> type, String pluginId);
//
//    List getExtensions(String pluginId);
//
//    Set<String> getExtensionClassNames(String pluginId);
//
//    ExtensionFactory getExtensionFactory();
//
//    /**
//     * The runtime mode. Must currently be either DEVELOPMENT or DEPLOYMENT.
//     */
//    RuntimeMode getRuntimeMode();
//
//    /**
//     * Returns {@code true} if the runtime mode is {@code RuntimeMode.DEVELOPMENT}.
//     */
//    default boolean isDevelopment() {
//        return RuntimeMode.DEVELOPMENT.equals(getRuntimeMode());
//    }
//
//    /**
//     * Returns {@code true} if the runtime mode is not {@code RuntimeMode.DEVELOPMENT}.
//     */
//    default boolean isNotDevelopment() {
//        return !isDevelopment();
//    }
//
//    /**
//     * Retrieves the {@link Plugin} that loaded the given class 'clazz'.
//     */
//    Plugin whichPlugin(Class<?> clazz);
//
//    void addPluginStateListener(PluginStateListener listener);
//
//    void removePluginStateListener(PluginStateListener listener);
//
//    /**
//     * Set the system version.  This is used to compare against the plugin
//     * requires attribute.  The default system version is 0.0.0 which
//     * disables all version checking.
//     *
//     * @default 0.0.0
//     * @param version
//     */
//    void setSystemVersion(String version);
//
//    /**
//     * Returns the system version.
//     *
//     * @return the system version
//     */
//    String getSystemVersion();
//
//    /**
//     * Gets the first path of the folders where plugins are installed.
//     *
//     * @deprecated Use {@link #getPluginsRoots()} instead to get all paths where plugins are could be installed.
//     *
//     * @return Path of plugins root
//     */
//    @Deprecated
//    Path getPluginsRoot();
//
//    /**
//     * Gets the a read-only list of all paths of the folders where plugins are installed.
//     *
//     * @return Paths of plugins roots
//     */
//    List<Path> getPluginsRoots();
//
//    VersionManager getVersionManager();

}
