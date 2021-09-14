package com.maukaim.cryptohub.plugins.api.exchanges.exception;

/**
 * Thrown when the OrderType provided does not exist.
 */
public class OrderTypeNotFoundException extends Exception {
    public OrderTypeNotFoundException(String message) {
        super(message);
    }
}
