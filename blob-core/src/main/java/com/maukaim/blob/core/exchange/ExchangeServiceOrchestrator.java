package com.maukaim.blob.core.exchange;

import com.maukaim.blob.plugins.core.model.module.ModuleInfo;
import com.maukaim.blob.plugins.core.model.module.ModuleProvider;
import com.maukaim.blob.plugins.api.exchanges.ExchangeService;
import com.maukaim.blob.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.blob.plugins.api.exchanges.model.ConnectionParameter;
import com.maukaim.blob.plugins.api.exchanges.model.ConnectionParameters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExchangeServiceOrchestrator {
    //TODO: Provide la list de tous les ExchangeServices et permet d'en initier?

    List<ModuleInfo> getAvailableExchangesInfo();
    Optional<ModuleProvider<? extends ExchangeService>> getExchangeProvider(String pluginId, String exchangeName);
    Map<String, List<ConnectionParameter>> getConnectionParameters(ModuleProvider<? extends ExchangeService> exchangeProvider);
    ExchangeWrapper connect(ModuleProvider<? extends ExchangeService> mp,
                            ConnectionParameters connectionParameters)
            throws ExchangeConnectionException;

    Optional<ExchangeWrapper> getExchange(String wrapperId);
}


