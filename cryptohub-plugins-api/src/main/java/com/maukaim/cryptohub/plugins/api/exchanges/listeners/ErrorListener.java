package com.maukaim.cryptohub.plugins.api.exchanges.listeners;

//TODO: Implement a bean to listen MarketData
public interface ErrorListener {
    void onErrorMessage(String errorMessage);
}
