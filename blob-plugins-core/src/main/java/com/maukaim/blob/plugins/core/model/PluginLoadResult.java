package com.maukaim.blob.plugins.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginLoadResult {
    private Integer nbOfJars;
    private List<Plugin> plugins;
    private List<Exception> exceptions;

    public static PluginLoadResult EMPTY = new PluginLoadResult(0, Collections.emptyList(), Collections.emptyList());
}
