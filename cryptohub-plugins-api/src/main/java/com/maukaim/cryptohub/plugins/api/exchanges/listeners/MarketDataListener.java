package com.maukaim.cryptohub.plugins.api.exchanges.listeners;

import com.maukaim.cryptohub.plugins.api.market.model.MarketData;

import java.util.List;

/**
 * Listener to receive MarketData live
 */
public interface MarketDataListener {
    void onMarketData(List<MarketData> data);
}
