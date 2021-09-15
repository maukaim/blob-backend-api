package com.maukaim.cryptohub.plugins.core;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.*;

/**
 * Largely inspired by
 *
 * @see <a href="https://github.com/pf4j/pf4j">pf4j project from Decebal Suiu</a>
 * Parent-Last strategy, try first to load classes & resources from this loader.
 * @see <a href="https://medium.com/@isuru89/java-a-child-first-class-loader-cbd9c3d0305">security example inspiring this implementation.</a>
 */
@Slf4j
public class ChildFirstClassLoader extends URLClassLoader {

    private static final String JAVA_PACKAGE_PREFIX = "java.";
    private static final String APP_PACKAGE_PREFIX = "com.maukaim.cryptohub.";

    public ChildFirstClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public void addPath(Path path) {
        try {
            addURL(path.toFile().getCanonicalFile().toURI().toURL());
        } catch (IOException e) {
//            log.error(e.getMessage(), e);
        }
    }

    /**
     * Load classes given their name.
     *
     * @param className name (with package path) of the class we try to load.
     * @return Class object we are retrieving.
     * @throws ClassNotFoundException if this loader and its parent can't find the className
     */
    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(className)) {
            if (className.startsWith(JAVA_PACKAGE_PREFIX) || className.startsWith(APP_PACKAGE_PREFIX)) {
                log.info("The class {} to be loaded IS from the application.", className);
                return getParent().loadClass(className);
            }
            log.info("The class {} to be loaded IS NOT from the application.", className);

            Class<?> loadedClass = findLoadedClass(className);
            return Objects.nonNull(loadedClass) ? loadedClass : this.parentLastLoadClass(className);
        }
    }

    private Class<?> parentLastLoadClass(String className) throws ClassNotFoundException {
        try {
            return findClass(className);
        } catch (ClassNotFoundException e) {
            log.info("Class not found in loader {}, will go through classic loading path...", this.getName());
            return super.loadClass(className);
        }
    }


    @Override
    public URL getResource(String name) {
        log.info("DEBUG:: Received request to load resource '{}'", name);
        URL resource = findResource(name);
        return Objects.nonNull(resource) ? resource : super.getResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL> resources = findResources(name);
        return (Objects.nonNull(resources) && resources.hasMoreElements()) ? resources : getParent().getResources(name);
    }

}
