package com.maukaim.opensource.cryptohub.exchange.listeners;

import com.maukaim.opensource.cryptohub.market.model.MarketData;

import java.util.List;

public interface MarketDataListener {
    void onMarketData(List<MarketData> data);
}
