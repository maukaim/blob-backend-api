package com.maukaim.blob.plugins.annotation.processor;

public class ModuleIndexNotReadableException extends Exception {
    private String moduleIndexPath;

    public ModuleIndexNotReadableException(Throwable cause, String path) {
        super(cause);
        this.moduleIndexPath = path;
    }

    public ModuleIndexNotReadableException(String path) {
        super();
        this.moduleIndexPath = path;
    }

    public String getModuleIndexPath() {
        return moduleIndexPath;
    }
}
