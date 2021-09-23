package com.maukaim.blob.plugins.core;

public class PluginLifeCycleException extends Exception {
    private String reason;
    private Boolean blockingException;

    public PluginLifeCycleException(String reason, Throwable cause) {
        this(reason, cause, true);
    }

    public PluginLifeCycleException(String reason, Throwable cause, Boolean isBlocking) {
        super(cause);
        this.reason = reason;
        this.blockingException = isBlocking;
    }

    public PluginLifeCycleException(String reason) {
        this(reason, true);
    }

    public PluginLifeCycleException(String reason, Boolean isBlocking) {
        super();
        this.reason = reason;
        this.blockingException = isBlocking;
    }


    public String getReason() {
        return reason;
    }

    public Boolean isBlocking() {
        return blockingException;
    }
}
