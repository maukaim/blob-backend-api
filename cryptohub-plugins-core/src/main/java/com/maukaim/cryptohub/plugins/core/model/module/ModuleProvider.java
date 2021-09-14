package com.maukaim.cryptohub.plugins.core.model.module;

import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.api.plugin.PreProcess;
import com.maukaim.cryptohub.plugins.core.model.Plugin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleProvider<T extends Module> {
    private ModuleInfo moduleInfo;
    private Class<? extends PreProcess> preProcess;
    private Class<? extends T> moduleClass;
}
