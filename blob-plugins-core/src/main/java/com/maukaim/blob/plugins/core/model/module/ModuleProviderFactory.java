package com.maukaim.blob.plugins.core.model.module;

import com.maukaim.blob.plugins.annotation.processor.ModuleIndexNotFoundException;
import com.maukaim.blob.plugins.annotation.processor.ModuleIndexNotReadableException;
import com.maukaim.blob.plugins.annotation.processor.utils.FileSystemHelper;
import com.maukaim.blob.plugins.api.plugin.HasPreProcess;
import com.maukaim.blob.plugins.api.plugin.Module;
import com.maukaim.blob.plugins.api.plugin.ModuleDeclarator;
import com.maukaim.blob.plugins.api.plugin.PreProcess;
import com.maukaim.blob.plugins.core.model.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ModuleProviderFactory {

    private static final String MODULE_INDEX_RESOURCE = FileSystemHelper.MODULE_INDEX_RESOURCE;

    private static ModuleProvider<? extends Module> build(String moduleName, Plugin plugin) throws ModuleException {
        Class<?> clz;
        try {
            clz = plugin.getPluginClassLoader().loadClass(moduleName);
        } catch (ClassNotFoundException e) {
            log.error("Error while loading class {}",moduleName, e);
            throw new ModuleException(
                    String.format("Plugin %s declared a module %s, but plugin's ClassLoader can't find it.",
                            plugin.getInfo().getName(), moduleName), e);
        }
        if (Module.class.isAssignableFrom(clz)) {
            Class<? extends Module> moduleClass = clz.asSubclass(Module.class);
            return ModuleProvider.builder()
                    .moduleInfo(buildInfo(plugin, moduleClass))
                    .preProcess(getPreProcess(moduleClass))
                    .moduleClass(moduleClass)
                    .build();
        } else {
            throw new ModuleException(
                    String.format("Plugin %s declared %s as a module, but it does not implement %s.",
                            plugin.getInfo().getName(), clz.getSimpleName(), Module.class.getSimpleName()));
        }

    }

    public static List<ModuleProvider<? extends Module>> build(Plugin plugin) throws ModuleException {
        try {
            Set<String> moduleNamesDeclared = FileSystemHelper.readModuleIndex(plugin.getPluginClassLoader());
            return moduleNamesDeclared.stream()
                    .map(name -> ModuleProviderFactory.build(name, plugin))
                    .collect(Collectors.toList());
        } catch (ModuleIndexNotFoundException e) {
            throw new ModuleException(String.format("No %s found for plugin %s ",
                    e.getModuleIndexPath(), plugin.getInfo().getName()), e.getCause());
        } catch (ModuleIndexNotReadableException e) {
            throw new ModuleException(String.format("Impossible to read %s file of plugin %s ",
                    e.getModuleIndexPath(), plugin.getInfo().getName()), e.getCause());
        }

    }

    private static Class<? extends PreProcess> getPreProcess(Class<? extends Module> moduleClass) {
        HasPreProcess hasPreProcess = moduleClass.getAnnotation(HasPreProcess.class);
        return (hasPreProcess == null) ? null : hasPreProcess.value();
    }

    private static ModuleInfo buildInfo(Plugin plugin, Class<?> moduleClass) {
        return ModuleInfo.builder()
                .description(getDescription(moduleClass))
                .name(getName(moduleClass))
                .plugin(plugin)
                .build();
    }

    private static String getDescription(Class<?> moduleClass) {
        ModuleDeclarator declarator = moduleClass.getAnnotation(ModuleDeclarator.class);
        return (declarator == null) ? null : declarator.description();
    }

    private static String getName(Class<?> moduleClass) {
        ModuleDeclarator declarator = moduleClass.getAnnotation(ModuleDeclarator.class);
        return (declarator == null) ? null : declarator.name();
    }
}
