package com.maukaim.cryptohub.plugins.api.plugin;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used by AnnotationProcessor to perform tests.
 * Declare a PreProcess class to be used by the application's core
 */
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface HasPreProcess {
    Class<? extends PreProcess> value();

}
