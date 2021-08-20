package com.maukaim.cryptohub.plugins;

import com.google.common.collect.Sets;
import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NativePluginModuleSniffer {

    protected <T> Set<Class<? extends T>> sniffSubTypes(String path, Class<T> clazz){
        Reflections reflections = new Reflections(path);
        return reflections.getSubTypesOf(clazz);
    }

    protected <T> Set<T> getInstancesOfSubTypes(Set<Class<? extends T>> subTypes) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Set<T> subInstancesCreated = Sets.newConcurrentHashSet();

        for (Class<? extends T> clazz : subTypes) {
            Constructor<? extends T> constructor = clazz.getConstructor();
            T exchangeService = constructor.newInstance();
            subInstancesCreated.add(exchangeService);
        }
        return subInstancesCreated;
    }
}
