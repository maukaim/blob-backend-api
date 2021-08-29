package com.maukaim.cryptohub.plugins.api.plugin;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public interface HasPreProcessProcessor<M extends Module, PP extends PreProcess> {
    boolean process(
            Class<M> moduleSubClass,
            Class<? extends PP> preProcessActorSubClass,
            Set<? extends TypeElement> annotationRequested,
            RoundEnvironment roundEnv);

    Class<M> getModuleClass();
    Class<? extends PP> getPreProcessClass();
}
