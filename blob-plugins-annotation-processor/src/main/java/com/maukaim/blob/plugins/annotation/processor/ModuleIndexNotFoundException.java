package com.maukaim.blob.plugins.annotation.processor;

public class ModuleIndexNotFoundException extends Exception {
    private String moduleIndexPath;

    public ModuleIndexNotFoundException(Throwable cause, String path) {
        super(cause);
        this.moduleIndexPath = path;
    }

    public ModuleIndexNotFoundException(String path) {
        super();
        this.moduleIndexPath = path;
    }

    public String getModuleIndexPath() {
        return moduleIndexPath;
    }
}
