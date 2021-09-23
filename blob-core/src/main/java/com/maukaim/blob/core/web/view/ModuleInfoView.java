package com.maukaim.blob.core.web.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModuleInfoView {
    private String name;
    private String description;
    private String pluginName;
    private String pluginId;
}
