package com.maukaim.cryptohub.plugins.api.exchanges;

import com.google.auto.service.AutoService;
import com.maukaim.cryptohub.plugins.api.plugin.AbstractHasPreProcessProcessor;
import com.maukaim.cryptohub.plugins.api.plugin.PreProcess;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

@SupportedAnnotationTypes("*")
@AutoService(Processor.class)
public class ExchangeServiceHasPreProcessProcessor extends AbstractHasPreProcessProcessor<ExchangeService, PreProcess> {

    @Override
    public Class<ExchangeService> getModuleClass() {
        return ExchangeService.class;
    }

    @Override
    public Class<? extends PreProcess> getPreProcessClass() { return ExchangeServicePreProcess.class; }

    @Override
    protected void checkPreProcessConstructor(Element ppAnnotationAsElement) {
        if(!hasPublicNoArgsConstructor(ppAnnotationAsElement)){
            errorMessage(ppAnnotationAsElement,
                    "%s has to provide public empty and public constructor.",
                    ppAnnotationAsElement.getSimpleName());
        }
    }

    private boolean hasPublicNoArgsConstructor(Element elt){
        return elt.getEnclosedElements().stream()
                .filter(elem -> elem.getKind() == ElementKind.CONSTRUCTOR && elem.getModifiers().contains(Modifier.PUBLIC))
                .map(elem -> (ExecutableElement) elem)
                .anyMatch(constructor -> constructor.getParameters().size() == 0);
    }
}
