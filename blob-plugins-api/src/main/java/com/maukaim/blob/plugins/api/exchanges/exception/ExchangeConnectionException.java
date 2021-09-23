package com.maukaim.blob.plugins.api.exchanges.exception;


/**
 * Thrown when the connection with an Exchange result with an error.
 */
public class ExchangeConnectionException extends Exception {
    public ExchangeConnectionException(String message) {
        super(message);
    }
}
