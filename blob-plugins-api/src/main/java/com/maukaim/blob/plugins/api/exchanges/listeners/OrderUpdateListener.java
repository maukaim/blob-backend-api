package com.maukaim.blob.plugins.api.exchanges.listeners;

import com.maukaim.blob.plugins.api.order.Order;

/**
 * Listen for every new information about an order
 */
public interface OrderUpdateListener {
    void onOrderUpdate(Order order);

}
