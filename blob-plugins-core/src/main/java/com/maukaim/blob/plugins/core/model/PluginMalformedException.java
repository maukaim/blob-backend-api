package com.maukaim.blob.plugins.core.model;

public class PluginMalformedException extends Exception {
    public PluginMalformedException(String message, Throwable cause) {
        super(message, cause);
    }
    public PluginMalformedException(String message) {
        super(message);
    }
}
