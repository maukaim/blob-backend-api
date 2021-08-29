package com.maukaim.cryptohub.plugins.core.model;

import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.api.plugin.PluginConfig;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PluginFactory {

    public static Plugin build(PluginInfo info, ClassLoader loader, Path path, List<ModuleProvider<? extends Module>> providers) throws PluginMalformedException {
        return Plugin.builder()
                .info(info)
                .pluginClassLoader(loader)
                .pluginPath(path)
                .status(PluginStatus.CREATED)
                .build();

    }

    public static PluginInfo buildInfo(JarFile jarFile) throws PluginMalformedException, IOException {
        Manifest manifest = jarFile.getManifest();
        if(manifest == null){
            throw new PluginMalformedException(
                    String.format("No manifest found into %s", jarFile.getName())
            );
        }

        Attributes mainAttributes = manifest.getMainAttributes();
        return PluginInfo.builder()
                .author(getOrElse(mainAttributes, PluginConfig.PLUGIN_AUTHOR, null))
                .description(getOrElse(mainAttributes, PluginConfig.PLUGIN_DESCRIPTION, "No description."))
                .name(getOrElse(mainAttributes, PluginConfig.PLUGIN_NAME, null))
                .version(getOrElse(mainAttributes, PluginConfig.PLUGIN_VERSION, null))
                .build();
    }

    private static String getOrElse(Attributes attributes, String attrId, String elseValue) throws PluginMalformedException{
        String value = attributes.getValue(attrId);

        if(Objects.isNull(value)){
            if(Objects.isNull(elseValue)){
                throw new PluginMalformedException(
                        String.format("The mandatory attribute %s not found in manifest.",
                                attrId));
            }else{
                return elseValue;
            }

        }else{
            return value;
        }
    }

    }
