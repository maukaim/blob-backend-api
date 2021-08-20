package com.maukaim.cryptohub.plugins.model;

import com.maukaim.cryptohub.commons.module.ModuleDeclarator;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class PluginModuleFactory {

    public static List<PluginModule> build(List<Class<? extends ModuleDeclarator>> pluginDeclarators, List<Class<?>> classesLoaded, ClassLoader loader, String jarFilePath) {
        return pluginDeclarators.stream()
                .map(clazz -> {
                    try {
                        return Optional.<ModuleDeclarator>of(clazz.getConstructor().newInstance());
                    } catch (InstantiationException e) {
                        log.warn("Impossible to instantiate {}.", clazz.getSimpleName(), e);
                        return Optional.<ModuleDeclarator>empty();
                    } catch (IllegalAccessException e) {
                        log.warn("Impossible to access the constructor of {}", clazz.getSimpleName(), e);
                        return Optional.<ModuleDeclarator>empty();
                    } catch (InvocationTargetException e) {
                        log.warn("Impossible to invoke the constructor of {}", clazz.getSimpleName(), e);
                        return Optional.<ModuleDeclarator>empty();
                    } catch (NoSuchMethodException e) {
                        log.warn("No public-NoArgs-constructor available for {}", clazz.getSimpleName(), e);
                        return Optional.<ModuleDeclarator>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(pluginDeclarator -> build(pluginDeclarator, classesLoaded, loader, jarFilePath))
                .collect(Collectors.toList());
    }

    public static <T extends ModuleDeclarator> PluginModule build(T pluginDeclarator, List<Class<?>> classesLoaded, ClassLoader loader, String jarFilePath) {
        return PluginModule.builder()
                .name(pluginDeclarator.getName())
                .description(pluginDeclarator.getDescription())
                .loadedClasses(classesLoaded)
                .loader(loader)
                .jarPath(jarFilePath)
                .declarator(pluginDeclarator.getClass())
                .build();
    }
}
