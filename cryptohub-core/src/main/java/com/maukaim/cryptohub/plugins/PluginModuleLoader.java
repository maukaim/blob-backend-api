package com.maukaim.cryptohub.plugins;


import com.maukaim.cryptohub.commons.module.ModuleDeclarator;
import com.maukaim.cryptohub.plugins.model.PluginModule;

import java.util.List;

/**
 * Load plugins from plugins repertory.
 */
public interface PluginModuleLoader {

    void unload(List<PluginModule> pluginSystems);
    void discoverAllPlugins();
    List<PluginModule> getByPath(String uriJar) throws NoPluginModuleForPathException;
    void discover(String pathURI);
    List<PluginModule> getPlugins(Class<? extends ModuleDeclarator> declaratorClazz);
    //IDEA Avoir un reload aussi pour les moment ou y a un update...? Peut etre que discover ne sert a rien et devrait etre
    // remplace par ca justement ! make sense je trouve.
}


//TODO: SOLUTION POUR MON PROBLEME de uninstall incoherent, il faut pas confondre les notions plugin user et plugin system
// Relation peut etre 1==1 ou 1==N (User==System)

// Les Users peuvent ajouter des plugins.
// Chaque plugins peut avoir plusieurs modules.
// 1 - Un pluginModule doit etre declared, pour etre trouve.
// 2 - Un pluginModule peut etre sniffed par le PluginModuleManager
// 3 - Un pluginModule doit etre
// 4 - Il n'y a pas de

// DANS UN PluginUserService
//1 - User veut uninstall un pluginUser ()
    //1.1 - Go sur la list des PluginUser
    //1.2 - Click "Uninstall" on this pluginUser
    //1.3 - Front ask Back to destroy PluginUser

//2 - System recoit la requete
    //2.1 - System look at all its PluginSystem associated to PluginUser's jarPath with PluginLoader
    //2.2 - PluginLoader uncache all its PluginSystem, and notify Managers
    //2.3 - Managers process to forget the PluginSystems.
    //2.4 - If PluginLoader uncached successfully ALL the PluginSystem
    //2.5 - System remove PluginUser.

