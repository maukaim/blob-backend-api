package com.maukaim.cryptohub.plugins.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class Plugin {
    private String name;
    private String author;
    private String version;

    private String jarPath;
    private Boolean activated;

    private String description;
    private ClassLoader loader;
    private List<Class<? extends PluginModule>> modules;

    //TODO: Pour unload :
    // Si Module is Native, alors PAS POSSIBLE
    // Sinon, on demande de unload a tous les modules manager.
    // Si 1 retourne une erreur ModuleUnloadException, alors prevenir
    // User que pour unload total, on doit relancer appli / server.
}
