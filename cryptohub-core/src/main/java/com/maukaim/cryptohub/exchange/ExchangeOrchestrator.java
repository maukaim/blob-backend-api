package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.commons.exchanges.ExchangeService;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface ExchangeOrchestrator {

    Map<String, ExchangeService> getAllExchangeServices();
    void discoverExchangeServices() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

}
