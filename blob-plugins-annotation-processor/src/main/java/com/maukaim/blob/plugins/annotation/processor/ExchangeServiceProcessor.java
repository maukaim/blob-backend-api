package com.maukaim.blob.plugins.annotation.processor;

import com.google.auto.service.AutoService;
import com.maukaim.blob.plugins.api.exchanges.ExchangeService;
import com.maukaim.blob.plugins.api.exchanges.ExchangeServicePreProcess;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

@SupportedAnnotationTypes("*")
@AutoService(Processor.class)
public class ExchangeServiceProcessor extends AbstractModuleProcessor<ExchangeService, ExchangeServicePreProcess> {

    @Override
    public Class<ExchangeService> getModuleClass() {
        return ExchangeService.class;
    }

    @Override
    public Class<ExchangeServicePreProcess> getPreProcessClass() { return ExchangeServicePreProcess.class; }

    @Override
    protected boolean havingAModuleDeclaratorIsMandatory() {
        return true;
    }

    @Override
    protected boolean havingPreProcessIsMandatory() {
        return true;
    }

    @Override
    protected void checkPreProcessConstructor(Element ppAsElement) {
        if(!hasPublicNoArgsConstructor(ppAsElement)){
            errorMessage(ppAsElement,
                    "%s has to provide a public no-args constructor.",
                    ppAsElement.getSimpleName());
        }
    }

    private boolean hasPublicNoArgsConstructor(Element elt){
        return elt.getEnclosedElements().stream()
                .filter(elem -> elem.getKind() == ElementKind.CONSTRUCTOR && elem.getModifiers().contains(Modifier.PUBLIC))
                .map(elem -> (ExecutableElement) elem)
                .anyMatch(constructor -> constructor.getParameters().size() == 0);
    }
}
