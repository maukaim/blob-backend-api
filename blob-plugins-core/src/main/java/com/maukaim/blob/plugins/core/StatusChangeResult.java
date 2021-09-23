package com.maukaim.blob.plugins.core;

import com.maukaim.blob.plugins.core.model.PluginStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangeResult {
    private PluginStatus previous;
    private PluginStatus current;
    private List<PluginLifeCycleException> exceptions;

    public static StatusChangeResult of(PluginStatus status, PluginLifeCycleException... exceptions){
        return new StatusChangeResult(status,status, List.of(exceptions));
    }

    public static StatusChangeResult of(PluginStatus previous, PluginStatus current, PluginLifeCycleException... exceptions){
        return new StatusChangeResult(previous,current, List.of(exceptions));
    }

    public boolean statusChanged(){
        return previous != current;
    }
}
