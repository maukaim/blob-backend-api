package com.maukaim.cryptohub.plugins.api.order;

/**
 * Status of any Order.
 */
public enum OrderStatus {
    NEW,
    CREATED,
    REJECTED,
    PARTIALLY_FILLED,
    FILLED;
}
