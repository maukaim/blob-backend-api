package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class ExchangeSessionManager {

    private UUID uniqIdentifier;
    private ExchangeService exchangeService;
    private Map<String,List<ConnectionParameter>> connectionParametersCached;

    public ExchangeSessionManager(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
        this.uniqIdentifier = UUID.randomUUID();
    }


}
