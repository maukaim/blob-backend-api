package com.maukaim.blob.core.web.sse;

public class NoSuchSseClientException extends RuntimeException {
    NoSuchSseClientException(String msg) {
        super(msg);
    }
}
