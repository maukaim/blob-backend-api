package com.maukaim.blob.plugins.core.model.module;

import com.maukaim.blob.plugins.core.model.Plugin;
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
