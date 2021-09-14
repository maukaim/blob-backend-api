package com.maukaim.cryptohub.plugins.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * All information displayed in manifest are available here.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PluginInfo {
    private String pluginId;
    private String name;
    private String author;
    private String description;
    private String version; //Should switch to Version from java-semver, or own impl of it.

}
