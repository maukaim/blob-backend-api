package com.maukaim.cryptohub.exchange;

import com.google.common.collect.Sets;
import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.commons.parameter.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExchangeServiceDiscoverer {

    private Set<ExchangeService> exchangeServices = Sets.newConcurrentHashSet();

    @PostConstruct
    public void init() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException{
        Reflections reflections = new Reflections("com.maukaim.cryptohub");
        Set<Class<? extends ExchangeService>> subTypes = reflections.getSubTypesOf(ExchangeService.class);
        for (Class<? extends ExchangeService> clazz : subTypes) {
            Constructor<? extends ExchangeService> constructor = clazz.getConstructor();
            ExchangeService exchangeService = constructor.newInstance();
            this.exchangeServices.add(exchangeService);
        }


        log.info("DEBUG::: Exchange service(s) discovered -> {}",
                exchangeServices.stream().map(es -> es.getClass().getSimpleName()).collect(Collectors.joining(", "))
        );
    }

}
