package com.maukaim.blob.plugins.api.exchanges.listeners;

import com.maukaim.blob.plugins.api.market.model.MarketData;

/**
 * Listener to receive MarketData live
 */
public interface MarketDataListener {
    void onMarketData(MarketData data);
}
