package com.maukaim.cryptohub.plugins.core.model.module;

import com.maukaim.cryptohub.plugins.api.plugin.Module;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleProvider<T extends Module> {
    private ModuleInfo moduleInfo;
    private Class<T> modularClass;
}
