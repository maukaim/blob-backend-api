package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeService;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.ConnectionListener;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;
import lombok.Data;

@Data
public class ExchangeWrapper implements ConnectionListener {
    private String id;
    private ExchangeService service;
    private ConnectionParameters connectionParametersCached;

    public ExchangeWrapper(String id, ExchangeService service, ConnectionParameters connectionParameters) {
        this.id = id;
        this.service = service;
        this.connectionParametersCached = connectionParameters;
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


}
