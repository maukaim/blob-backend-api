package com.maukaim.cryptohub.exchange;

import com.google.common.collect.Maps;
import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.commons.module.ExchangeDeclarator;
import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.exchange.sniffer.ExchangeModuleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ExchangeOrchestratorImpl implements ExchangeOrchestrator {

    private final ExchangeModuleManager exchangeSniffer;
    Map<UUID, ExchangeSessionManager> exchangeConnectionManagers = Maps.newConcurrentMap();

    @Autowired
    ExchangeOrchestratorImpl(ExchangeModuleManager exchangeDiscoverer){
        this.exchangeSniffer = exchangeDiscoverer;
    }

    @PostConstruct
    public void init(){
        //TODO: Init cache of Managers with database sources
    }

    @Override
    public Set<String> getAllExchangeServices() {
        return this.exchangeSniffer.getExchangeLoaders().keySet();
    }

    @Override
    public ExchangeSessionManager instantiate(String exchangeName, Map<String, List<ConnectionParameter>>  connectionParameters) {
        ExchangeDeclarator exchangeLoader = this.exchangeSniffer.getExchangeLoader(exchangeName);

        ExchangeService exchangeService = exchangeLoader.getInstance();
        ExchangeSessionManager exchangeConnectionManager = new ExchangeSessionManager(exchangeService);
        this.exchangeConnectionManagers.putIfAbsent(exchangeConnectionManager.getUniqIdentifier(), exchangeConnectionManager);
        return exchangeConnectionManager;
    }


}
