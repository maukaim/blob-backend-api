package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.plugins.core.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceOrchestratorImpl implements ExchangeServiceOrchestrator {
    private ExchangeServiceFactory factory;
    private PluginService pluginService;

    public ExchangeServiceOrchestratorImpl(
            @Autowired ExchangeServiceFactory factory,
            @Autowired PluginService pluginService){
        this.pluginService = pluginService;
        this.factory = factory;
    }
}
