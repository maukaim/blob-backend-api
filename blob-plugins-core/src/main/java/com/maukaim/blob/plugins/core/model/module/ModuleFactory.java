package com.maukaim.blob.plugins.core.model.module;

import com.maukaim.blob.plugins.api.plugin.Module;

public interface ModuleFactory<T extends Module> {
    T build(ModuleProvider<? extends T> module);
}
