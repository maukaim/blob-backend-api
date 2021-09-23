package com.maukaim.blob.plugins.core.model.module;

import com.maukaim.blob.plugins.api.plugin.Module;
import com.maukaim.blob.plugins.api.plugin.PreProcess;
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
    private Class<? extends PreProcess> preProcess;
    private Class<? extends T> moduleClass;
}
