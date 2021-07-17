package com.maukaim.cryptohub.exchange;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.commons.parameter.Parameter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExchangeSnifferImpl implements ExchangeSniffer {

    @Getter
    private Map<String,ExchangeService> exchangeServices = Maps.newConcurrentMap();

    @PostConstruct
    @Override
    public void sniff() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException{
        Reflections reflections = new Reflections("com.maukaim.cryptohub");
        Set<Class<? extends ExchangeService>> subTypes = reflections.getSubTypesOf(ExchangeService.class);

        Set<ExchangeService> exchangeServicesDiscovered = Sets.newConcurrentHashSet();

        for (Class<? extends ExchangeService> clazz : subTypes) {
            Constructor<? extends ExchangeService> constructor = clazz.getConstructor();
            ExchangeService exchangeService = constructor.newInstance();
            exchangeServicesDiscovered.add(exchangeService);
        }

        exchangeServicesDiscovered.forEach(exchange -> this.exchangeServices.putIfAbsent(
                this.getExchangeFinalName(exchange.getExchangeName()),
                exchange));

        log.info("DEBUG::: Exchange service(s) discovered -> {}",
                exchangeServicesDiscovered.stream().map(ExchangeService::getExchangeName).collect(Collectors.joining(", "))
        );
    }

    private String getExchangeFinalName(String name){
        return this.exchangeServices.containsKey(name)? this.getExchangeFinalNameNumbered(name, 2) : name;
    }

    private String getExchangeFinalNameNumbered(String name, Integer number){
        return this.exchangeServices.containsKey(name) ?
                this.getExchangeFinalNameNumbered(name, ++number) : String.format("%s (%s)", name, number);
    }

}
