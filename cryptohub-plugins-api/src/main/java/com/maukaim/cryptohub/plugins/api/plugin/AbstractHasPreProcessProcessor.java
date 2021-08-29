package com.maukaim.cryptohub.plugins.api.plugin;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;


public abstract class AbstractHasPreProcessProcessor<M extends Module, PPP extends PreProcess> extends AbstractProcessor implements HasPreProcessProcessor<M, PPP> {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return this.process(getModuleClass(), getPreProcessClass(), annotations, roundEnv);
    }

    @Override
    public boolean process(Class<M> moduleClass, Class<? extends PPP> pppClass,
                           Set<? extends TypeElement> annotationRequested, RoundEnvironment roundEnv) {

        if (roundEnv.processingOver()) {
            return false;
        }

        Types typeUtils = processingEnv.getTypeUtils();
        Elements elementUtils = processingEnv.getElementUtils();


        TypeMirror moduleSubType = elementUtils.getTypeElement(moduleClass.getCanonicalName()).asType();
        TypeMirror pppSubType = elementUtils.getTypeElement(pppClass.getCanonicalName()).asType();

        Set<? extends Element> allElements = roundEnv.getRootElements();
        for (Element elt : allElements) {
            if (elt.getKind().isClass() && typeUtils.isAssignable(elt.asType(), moduleSubType)) {
                HasPreProcess ppAnnotation = elt.getAnnotation(HasPreProcess.class);
                if (ppAnnotation == null) {
                    //All M should have a PreProcess annotation !
                    errorMessage(elt,
                            "%s's should have a %s annotation (Providing %s or a subClass of it).",
                            elt.getSimpleName(), HasPreProcess.class.getSimpleName(), pppClass.getSimpleName());
                } else {
                    // All M should have its PreProcess annotation with a PPP in it.
                    TypeMirror ppAnnotationClzSubType = null;
                    try {
                        ppAnnotation.value();
                    } catch (MirroredTypeException e) {
                        ppAnnotationClzSubType = e.getTypeMirror();
                    }

                    if (!typeUtils.isAssignable(ppAnnotationClzSubType, pppSubType)) {
                        errorMessage(elt,
                                "%s's %s annotation should provide (or a subClass of) %s.",
                                elt.getSimpleName(), HasPreProcess.class.getSimpleName(), pppClass.getSimpleName());
                    }

                    Element ppAnnotationAsElement = typeUtils.asElement(ppAnnotationClzSubType);

                    if (ppAnnotationAsElement.getKind() == ElementKind.INTERFACE) {
                        errorMessage(ppAnnotationAsElement,
                                "To be used in the annotation @%s of a %s module, %s shouldn't be an interface.",
                                HasPreProcess.class.getSimpleName(), moduleClass.getSimpleName(), ppAnnotationAsElement.getSimpleName());

                    } else if (ppAnnotationAsElement.getModifiers().contains(Modifier.ABSTRACT)) {
                        errorMessage(ppAnnotationAsElement,
                                "To be used in the annotation @%s of a %s module, %s shouldn't be declared abstract.",
                                HasPreProcess.class.getSimpleName(), moduleClass.getSimpleName(), ppAnnotationAsElement.getSimpleName());
                    }
                    //PreProcess constructor check
                    this.checkPreProcessConstructor(ppAnnotationAsElement);
                }
            }
        }
        return false;
    }

    protected void checkPreProcessConstructor(Element ppAnnotationAsElement) {
        this.noteMessage(ppAnnotationAsElement, "No checkConstructor implemented for %s.", this.getClass().getSimpleName());
    }

    protected void errorMessage(Element element, String message, Object... args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element);
    }

    protected void noteMessage(Element element, String message, Object... args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element);
    }

    protected void warningMessage(Element element, String message, Object... args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, String.format(message, args), element);
    }
}
