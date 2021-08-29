package com.maukaim.cryptohub.plugins.core.model.module;

import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.api.plugin.ModuleDeclarator;
import com.maukaim.cryptohub.plugins.core.model.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractModuleFactory<T extends Module> implements ModuleFactory<T> {

    @Override
    public T build(ModuleProvider<? extends T> module) {
        Class<? extends T> modularClass = module.getModularClass();
        try {
            Constructor<? extends T> constructor = modularClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ModuleConstructorException(
                    String.format("%s' s simple constructor not found.",
                            modularClass.getSimpleName()),
                    e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new ModuleConstructorException(
                    String.format("Exception on call of %s's constructor.",
                            modularClass.getSimpleName()),
                    e);
        }
    }

    public ModuleInfo buildInfo(Plugin plugin, Class<?> moduleClass) {
        return ModuleInfo.builder()
                .description(getDescription(moduleClass))
                .name(getName(moduleClass))
                .plugin(plugin)
                .build();
    }

    protected String getDescription(Class<?> moduleClass) {
        ModuleDeclarator declarator = moduleClass.getAnnotation(ModuleDeclarator.class);
        if (declarator == null) {
            return "";
        } else {
            return declarator.description();
        }
    }

    protected String getName(Class<?> moduleClass) {
        ModuleDeclarator declarator = moduleClass.getAnnotation(ModuleDeclarator.class);
        if (declarator == null) {
            return "";
        } else {
            return declarator.name();
        }
    }

}
