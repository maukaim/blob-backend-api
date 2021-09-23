package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.plugins.api.market.model.MarketData;
import com.maukaim.cryptohub.plugins.api.order.Order;

public interface ExchangeStreamer {
    void streamMarketData(String fromId, MarketData message);
    void streamOrderUpdate(String fromId, Order message);
}
