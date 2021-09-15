package com.maukaim.cryptohub.plugins.api.plugin;


import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Explicit declaration of Modules to help users choose the component they want to leverage on.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface ModuleDeclarator {

    String name() default "";

    String description() default "";

    Class<? extends java.lang.Module>[] requiredModules() default {};
}
