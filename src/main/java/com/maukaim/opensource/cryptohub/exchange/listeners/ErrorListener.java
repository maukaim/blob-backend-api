package com.maukaim.opensource.cryptohub.exchange.listeners;

import com.maukaim.opensource.cryptohub.market.model.MarketData;

import java.util.List;

//TODO: Implement a bean to listen MarketData
public interface ErrorListener {
    void onErrorMessage(String errorMessage);
}
