package com.maukaim.cryptohub.plugins.api.exchanges.listeners;

import com.maukaim.cryptohub.plugins.api.market.model.MarketData;

import java.util.List;

public interface MarketDataListener {
    void onMarketData(List<MarketData> data);
}
