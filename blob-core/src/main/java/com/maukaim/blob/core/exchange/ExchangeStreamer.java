package com.maukaim.blob.core.exchange;

import com.maukaim.blob.plugins.api.market.model.MarketData;
import com.maukaim.blob.plugins.api.order.Order;

public interface ExchangeStreamer {
    void streamMarketData(String fromId, MarketData message);
    void streamOrderUpdate(String fromId, Order message);
}
