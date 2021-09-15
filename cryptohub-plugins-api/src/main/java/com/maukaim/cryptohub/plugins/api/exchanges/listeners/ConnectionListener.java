package com.maukaim.cryptohub.plugins.api.exchanges.listeners;

import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;

/**
 * Listener notified when the connection with an exchange is established
 * or if an unexpected error disconnected the service.
 */
public interface ConnectionListener {
    void onConnected(ConnectionParameters parameterUsed);

    void onUnexpectedDisconnect(String errorMessage, boolean needReconnection);
}
