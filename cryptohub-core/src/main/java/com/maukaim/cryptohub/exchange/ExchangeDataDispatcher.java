package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.plugins.api.market.model.MarketData;
import com.maukaim.cryptohub.plugins.api.order.Order;

/**
 * In charge of notifying every
 */
public interface ExchangeDataDispatcher {
    void addExchangeDataListener();
    void send(String wrapperId, MarketData data);
    void send(String wrapperId, Order data);
}
