package com.maukaim.cryptohub.plugins;


import com.maukaim.cryptohub.commons.module.ModuleDeclarator;
import com.maukaim.cryptohub.plugins.model.PluginModule;

import java.util.List;

/**
 * Load plugins from plugins repertory.
 */
public interface PluginModuleManager {
    List<Class<? extends ModuleDeclarator>> getManagedDeclaratorClasses();
    void onNewPlugin(PluginModule plugin, Class<? extends ModuleDeclarator> clazz);
    void onRemoved(PluginModule plugin, Class<? extends ModuleDeclarator> clazz);
}
