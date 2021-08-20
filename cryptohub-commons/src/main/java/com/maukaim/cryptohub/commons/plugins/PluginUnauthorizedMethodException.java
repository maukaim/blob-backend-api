package com.maukaim.cryptohub.commons.plugins;

public class PluginUnauthorizedMethodException extends RuntimeException {
    public PluginUnauthorizedMethodException(String message) {
        super(message);
    }
}
