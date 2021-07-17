package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.commons.exchanges.ExchangeService;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface ExchangeSniffer {
    void sniff() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
    Map<String, ExchangeService> getExchangeServices();
}
