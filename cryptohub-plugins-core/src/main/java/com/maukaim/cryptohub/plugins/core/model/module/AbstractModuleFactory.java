package com.maukaim.cryptohub.plugins.core.model.module;

import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.api.plugin.PreProcess;
import com.maukaim.cryptohub.plugins.core.model.PluginMalformedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractModuleFactory<T extends Module> implements ModuleFactory<T> {

    @Override
    public T build(ModuleProvider<? extends T> module) {
        Class<? extends T> modularClass = module.getModuleClass();
        try {
            Constructor<? extends T> constructor = modularClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ModuleConstructionException(
                    String.format("%s' s simple constructor not found.",
                            modularClass.getSimpleName()),
                    e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ModuleConstructionException(
                    String.format("Exception on call of %s's constructor.",
                            modularClass.getSimpleName()),
                    e);
        }
    }

    public <PP extends PreProcess> PP buildPreProcess(Class<? extends PreProcess> preProcess, Class<PP> preProcessSubClass) throws ModuleConstructionException {
        try {
            return preProcessSubClass.cast(preProcess.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ModuleConstructionException(e.getMessage(), e);
        } catch (ClassCastException e) {
            throw new ModuleConstructionException(
                    String.format("Impossible to get an instance of %s from %s.",
                            preProcessSubClass.getSimpleName(),
                            preProcess.getSimpleName()), e);
        }
    }


}
