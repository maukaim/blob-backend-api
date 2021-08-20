package com.maukaim.cryptohub.plugins;

import lombok.Getter;

public class NoPluginModuleForPathException extends RuntimeException {
    @Getter
    private String path;
    public NoPluginModuleForPathException(String message) {
        super(message);
    }
}
