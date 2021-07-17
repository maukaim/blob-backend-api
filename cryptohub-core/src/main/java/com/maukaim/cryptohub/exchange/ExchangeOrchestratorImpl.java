package com.maukaim.cryptohub.exchange;

import com.google.common.collect.Maps;
import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

@Service
public class ExchangeOrchestratorImpl implements ExchangeOrchestrator {

    private final ExchangeSniffer exchangeSniffer;
    Map<UUID, ExchangeManager> exchangeServiceManagers = Maps.newConcurrentMap();

    @Autowired
    ExchangeOrchestratorImpl(ExchangeSniffer exchangeDiscoverer){
        this.exchangeSniffer = exchangeDiscoverer;
    }

    @PostConstruct
    public void init(){
        //TODO: Init cache of Managers with database sources
    }

    @Override
    public Map<String, ExchangeService> getAllExchangeServices() {
        return this.exchangeSniffer.getExchangeServices();
    }

    @Override
    public void discoverExchangeServices() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.exchangeSniffer.sniff();
    }


}
