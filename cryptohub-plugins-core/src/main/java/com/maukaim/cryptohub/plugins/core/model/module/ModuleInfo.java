package com.maukaim.cryptohub.plugins.core.model.module;

import com.maukaim.cryptohub.plugins.core.model.Plugin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleInfo {
    private String name;
    private String description;
    private Plugin plugin;
}
