package com.maukaim.cryptohub.plugins.core.model.module;

import com.maukaim.cryptohub.plugins.api.plugin.Module;

public interface ModuleFactory<T extends Module> {
    T build(ModuleProvider<? extends T> module);
}
