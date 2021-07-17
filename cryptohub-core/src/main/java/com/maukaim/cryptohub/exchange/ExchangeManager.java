package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
public class ExchangeManager {

    private UUID uniqIdentifier;
    private ExchangeService exchangeService;
    private List<ConnectionParameter> connectionParametersCached;

    public ExchangeManager(ExchangeService exchangeService){
        this.exchangeService = exchangeService;
        this.uniqIdentifier = UUID.randomUUID();
    }


}
