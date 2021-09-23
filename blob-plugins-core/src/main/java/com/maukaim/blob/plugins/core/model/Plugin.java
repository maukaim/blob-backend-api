package com.maukaim.blob.plugins.core.model;

import com.maukaim.blob.plugins.core.model.module.ModuleProvider;
import com.maukaim.blob.plugins.api.plugin.Module;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plugin {
    private PluginInfo info;
    private Path pluginPath;
    private ClassLoader pluginClassLoader;
    private List<ModuleProvider<? extends Module>> moduleProviders;
    private PluginStatus status;

    @Override
    public boolean equals(Object other){
        if(other == null) return false;

        if (this == other) return true;

        if(this.getClass().equals(other.getClass())){
            Plugin otherTyped = (Plugin) other;
            return this.getInfo().getPluginId().equals(otherTyped.getInfo().getPluginId());
        }

        return false;
    }
}
