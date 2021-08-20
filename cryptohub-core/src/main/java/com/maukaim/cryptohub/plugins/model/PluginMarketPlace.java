package com.maukaim.cryptohub.plugins.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class PluginMarketPlace {
    private String name;
    private String author;
    private String version;

    private String description;
}
