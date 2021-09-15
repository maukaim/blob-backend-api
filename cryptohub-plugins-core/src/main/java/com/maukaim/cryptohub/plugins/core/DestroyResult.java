package com.maukaim.cryptohub.plugins.core;

import com.maukaim.cryptohub.plugins.core.model.PluginStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestroyResult {
    private boolean destroyed;
    private List<PluginLifeCycleException> exceptions;
}
