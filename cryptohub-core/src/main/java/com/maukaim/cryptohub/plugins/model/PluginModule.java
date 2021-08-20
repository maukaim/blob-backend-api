package com.maukaim.cryptohub.plugins.model;

import com.maukaim.cryptohub.commons.module.ModuleDeclarator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public abstract class PluginModule {
    private String name;
    private String description;
    ClassLoader loader;

    private List<Class<?>> loadedClasses;
    private Class<? extends ModuleDeclarator> declarator;

    public abstract String  getBoule();
    // TODO: Abstract method static possible sur abstractClass !
    //  Donc on va faire ca pour forcer les Modules a declarer des informations statics.

}


