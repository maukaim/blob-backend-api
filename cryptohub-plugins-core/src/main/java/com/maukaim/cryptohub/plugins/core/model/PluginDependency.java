package com.maukaim.cryptohub.plugins.core.model;

import lombok.Data;

@Data
public class PluginDependency {
    private String pluginId;
    //TODO: Add version management for dependencies;

}
