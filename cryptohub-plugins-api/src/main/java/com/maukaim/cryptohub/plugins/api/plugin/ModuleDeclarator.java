package com.maukaim.cryptohub.plugins.api.plugin;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface ModuleDeclarator {
    String name() default "";

    String description() default "";

    Class<? extends java.lang.Module>[] requiredModules() default {};
}
