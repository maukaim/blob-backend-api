package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeService;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.ExchangeServiceListener;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;
import com.maukaim.cryptohub.plugins.api.market.model.MarketData;
import com.maukaim.cryptohub.plugins.api.order.Order;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Data
@Slf4j
public class ExchangeWrapper implements ExchangeServiceListener {
    private String id;
    private ExchangeService service;
    private ConnectionParameters connectionParametersCached;
    private ExchangeStreamer streamer;


    public ExchangeWrapper(ExchangeService service, ConnectionParameters connectionParameters,
                           ExchangeStreamer streamer) {
        this.id = UUID.randomUUID().toString();
        this.service = service;
        this.connectionParametersCached = connectionParameters;
        this.streamer = streamer;
        this.service.setExchangeServiceListener(this);
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
    public void onMarketData(MarketData data) {
        log.info("Received ! {}", data);
        this.streamer.streamMarketData(this.id, data);
    }

    @Override
    public void onOrderUpdate(Order order) {
        this.streamer.streamOrderUpdate(this.id, order);
    }
}
