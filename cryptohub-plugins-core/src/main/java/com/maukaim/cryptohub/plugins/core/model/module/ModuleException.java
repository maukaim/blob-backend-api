package com.maukaim.cryptohub.plugins.core.model.module;

public class ModuleException extends RuntimeException {
    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }
    public ModuleException(String message) {
        super(message);
    }
}
