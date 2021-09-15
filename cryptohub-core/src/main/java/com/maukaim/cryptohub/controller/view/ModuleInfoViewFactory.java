package com.maukaim.cryptohub.controller.view;

import com.maukaim.cryptohub.plugins.core.model.module.ModuleInfo;

public class ModuleInfoViewFactory {

    public static ModuleInfoView build(ModuleInfo moduleInfo){
        return ModuleInfoView.builder()
                .description(moduleInfo.getDescription())
                .name(moduleInfo.getName())
                .pluginId(moduleInfo.getPlugin().getInfo().getPluginId())
                .pluginName(moduleInfo.getPlugin().getInfo().getName())
                .build();
    }
}
