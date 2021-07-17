package com.maukaim.cryptohub.commons.exchanges.listeners;

import com.maukaim.cryptohub.commons.market.model.MarketData;

import java.util.List;

public interface MarketDataListener {
    void onMarketData(List<MarketData> data);
}
