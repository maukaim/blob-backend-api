package com.maukaim.cryptohub.web.sse;

public class NoSuchSseClientException extends RuntimeException {
    NoSuchSseClientException(String msg) {
        super(msg);
    }
}
