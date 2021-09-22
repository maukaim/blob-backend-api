package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeService;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.ExchangeServiceListener;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;
import com.maukaim.cryptohub.plugins.api.market.model.MarketData;
import com.maukaim.cryptohub.plugins.api.order.Order;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ExchangeWrapper implements ExchangeServiceListener {
    private String id;
    private ExchangeService service;
    private ConnectionParameters connectionParametersCached;
    private ExchangeDataDispatcher exchangeDataDispatcher;


    public ExchangeWrapper(ExchangeService service, ConnectionParameters connectionParameters, ExchangeDataDispatcher sender) {
        this.id = UUID.randomUUID().toString();
        this.service = service;
        this.connectionParametersCached = connectionParameters;
        this.exchangeDataDispatcher = sender;

        this.service.setExchangeListener(this);
        // When the persistence will be added, add a repository here to persist states of managers
    }

    @Override
    public void onConnected(ConnectionParameters parameterUsed) {
        this.connectionParametersCached = parameterUsed;
    }

    @Override
    public void onUnexpectedDisconnect(String errorMessage, boolean authorizeReconnect) {
        if (authorizeReconnect) {
            try {
                this.service.connect(this.connectionParametersCached);
            } catch (ExchangeConnectionException ignored) {
            }
        }
    }


    @Override
    public void onMarketData(List<MarketData> data) {
        this.exchangeDataDispatcher.send();
    }

    @Override
    public void onOrderUpdate(Order order) {

    }
}
