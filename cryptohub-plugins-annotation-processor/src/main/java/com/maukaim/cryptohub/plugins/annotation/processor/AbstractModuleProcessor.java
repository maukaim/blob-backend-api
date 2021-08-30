package com.maukaim.cryptohub.plugins.annotation.processor;

import com.maukaim.cryptohub.plugins.api.plugin.HasPreProcess;
import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.api.plugin.ModuleDeclarator;
import com.maukaim.cryptohub.plugins.api.plugin.PreProcess;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Objects;
import java.util.Set;


public abstract class AbstractModuleProcessor<M extends Module, PPP extends PreProcess> extends AbstractProcessor implements ModuleProcessor<M, PPP> {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    protected final Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }

    protected final Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return this.process(getModuleClass(), getPreProcessClass(), annotations, roundEnv);
    }

    @Override
    public boolean process(Class<M> moduleBoundClass, Class<PPP> preProcessBoundClass,
                           Set<? extends TypeElement> annotationRequested, RoundEnvironment roundEnv) {

        if (roundEnv.processingOver()) {
            return false;
        }
        TypeMirror moduleAsTypeMirror = this.getElementUtils().getTypeElement(moduleBoundClass.getCanonicalName()).asType();
        TypeMirror preProcessProvidedAsTypeMirror = this.getElementUtils().getTypeElement(preProcessBoundClass.getCanonicalName()).asType();

        Set<? extends Element> allElements = roundEnv.getRootElements();
        for (Element elt : allElements) {
            if (elt.getKind().isClass() && this.getTypeUtils().isAssignable(elt.asType(), moduleAsTypeMirror)) {
                this.processModuleElement(elt, preProcessProvidedAsTypeMirror);
            }
        }
        return false;
    }

    protected final String getSimpleName(TypeMirror t) {
        return this.getSimpleName(this.getTypeUtils().asElement(t));
    }

    protected final String getSimpleName(Element e) {
        return e.getSimpleName().toString();
    }

    private void processModuleElement(Element elt, TypeMirror preProcessSuperTypeMirror) {
        this.processHasProcessAnnotation(elt, preProcessSuperTypeMirror);
        this.processModuleDeclaratorAnnotation(elt);
    }

    private void processModuleDeclaratorAnnotation(Element elt) {
        ModuleDeclarator moduleDeclaratorAnnotation = elt.getAnnotation(ModuleDeclarator.class);
        if (Objects.isNull(moduleDeclaratorAnnotation) && this.havingAModuleDeclaratorIsMandatory()) {
            errorMessage(elt,
                    "%s should be annotated with @%s.",
                    elt.getSimpleName(), ModuleDeclarator.class.getSimpleName());

        } else if (Objects.nonNull(moduleDeclaratorAnnotation)) {
            this.checkModuleDeclaratorAnnotationValidity(elt, moduleDeclaratorAnnotation);
        }
    }

    protected void checkModuleDeclaratorAnnotationValidity(Element elt, ModuleDeclarator moduleDeclaratorAnnotation) {
        this.noteMessage(elt, "%s does not implement checks on annotation @%s for module %s.",
                 this.getClass().getSimpleName(), ModuleDeclarator.class.getSimpleName(), elt.getSimpleName());

    }

    protected abstract boolean havingAModuleDeclaratorIsMandatory();

    private void processHasProcessAnnotation(Element elt, TypeMirror preProcessSuperTypeMirror) {
        HasPreProcess hasPreProcessAnnotation = elt.getAnnotation(HasPreProcess.class);

        if (Objects.isNull(hasPreProcessAnnotation) && this.havingPreProcessIsMandatory()) {
            errorMessage(elt,
                    "%s should be annotated with @%s (Providing %s or a subClass of it).",
                    elt.getSimpleName(), HasPreProcess.class.getSimpleName(), this.getSimpleName(preProcessSuperTypeMirror));

        } else if (Objects.nonNull(hasPreProcessAnnotation)) {
            this.checkHasPreProcessAnnotationValidity(elt, hasPreProcessAnnotation, preProcessSuperTypeMirror);
        }
    }

    protected void checkHasPreProcessAnnotationValidity(Element elt, HasPreProcess hasPreProcessAnnotation,
                                                        TypeMirror preProcessSuperTypeMirror) {
        TypeMirror preProcessProvidedAsTypeMirror = this.getPreProcessFromAnnotation(hasPreProcessAnnotation);
        if (Objects.isNull(preProcessProvidedAsTypeMirror)) {
            errorMessage(elt, "Impossible to retrieve the class provided by @%s annotation on %s module.",
                    HasPreProcess.class.getSimpleName(), elt.getSimpleName());

        } else {
            this.checkPreProcessProvidedValidity(elt, preProcessProvidedAsTypeMirror, preProcessSuperTypeMirror);
        }
    }

    protected void checkPreProcessProvidedValidity(Element elt, TypeMirror preProcessProvided,
                                                   TypeMirror preProcessRequired) {

        if (!this.getTypeUtils().isAssignable(preProcessProvided, preProcessRequired)) {
            errorMessage(elt,
                    "%s's @%s annotation should provide class %s (or a subClass of it). %s class isn't.",
                    elt.getSimpleName(), HasPreProcess.class.getSimpleName(),
                    this.getSimpleName(preProcessRequired), this.getSimpleName(preProcessProvided));
        }

        Element preProcessProvidedAsElement = this.getTypeUtils().asElement(preProcessProvided);

        if (preProcessProvidedAsElement.getKind() == ElementKind.INTERFACE) {
            errorMessage(preProcessProvidedAsElement,
                    "To be used in the annotation @%s of the module %s, %s shouldn't be an interface.",
                    HasPreProcess.class.getSimpleName(), elt.getSimpleName(), preProcessProvidedAsElement.getSimpleName());

        } else if (preProcessProvidedAsElement.getModifiers().contains(Modifier.ABSTRACT)) {
            errorMessage(preProcessProvidedAsElement,
                    "To be used in the annotation @%s of the module %s, %s shouldn't be declared abstract.",
                    HasPreProcess.class.getSimpleName(), elt.getSimpleName(), preProcessProvidedAsElement.getSimpleName());
        }

        this.checkPreProcessConstructor(preProcessProvidedAsElement);

    }

    private TypeMirror getPreProcessFromAnnotation(HasPreProcess hasPreProcessAnnotation) {
        try {
            hasPreProcessAnnotation.value();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        return null;

    }

    abstract protected boolean havingPreProcessIsMandatory();

    protected void checkPreProcessConstructor(Element ppAnnotationAsElement) {
        this.noteMessage(ppAnnotationAsElement, "No checkConstructor implemented for annotation processor : %s.", this.getClass().getSimpleName());
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
