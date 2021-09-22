package com.maukaim.cryptohub.plugins.api.exchanges.listeners;

import com.maukaim.cryptohub.plugins.api.order.Order;

/**
 * Listen for every new information about an order
 */
public interface OrderUpdateListener {
    void onOrderUpdate(Order order);

}
