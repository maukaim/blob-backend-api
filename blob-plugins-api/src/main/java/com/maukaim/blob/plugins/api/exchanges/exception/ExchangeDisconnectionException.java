package com.maukaim.blob.plugins.api.exchanges.exception;


/**
 * Thrown when the disconnection with an Exchange result with an error.
 */
public class ExchangeDisconnectionException extends Exception {
    public ExchangeDisconnectionException(String message) {
        super(message);
    }
}
