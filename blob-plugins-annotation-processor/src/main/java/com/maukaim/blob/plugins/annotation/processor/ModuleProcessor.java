package com.maukaim.blob.plugins.annotation.processor;

import com.maukaim.blob.plugins.api.plugin.Module;
import com.maukaim.blob.plugins.api.plugin.PreProcess;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public interface ModuleProcessor<M extends Module, PP extends PreProcess> {
    boolean process(
            Class<M> moduleSubClass,
            Class<PP> preProcessActorSubClass,
            Set<? extends TypeElement> annotationRequested,
            RoundEnvironment roundEnv);

    Class<M> getModuleClass();
    Class<PP> getPreProcessClass();
}
