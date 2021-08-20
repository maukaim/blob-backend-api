package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExchangeOrchestrator {

    Set<String> getAllExchangeServices();
    ExchangeSessionManager instantiate(String exchangeName, Map<String, List<ConnectionParameter>> connectionParameters);
    //TODO: add instantiator of ExchangeService

}
