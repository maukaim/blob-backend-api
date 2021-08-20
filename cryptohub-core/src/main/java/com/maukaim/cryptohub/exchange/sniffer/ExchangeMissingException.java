package com.maukaim.cryptohub.exchange.sniffer;

public class ExchangeMissingException extends RuntimeException {
    public ExchangeMissingException(String message) {
        super(message);
    }
}
