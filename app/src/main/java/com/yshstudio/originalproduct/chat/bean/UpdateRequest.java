package com.yshstudio.originalproduct.chat.bean;

/**
 * Created by kongqing on 17-3-16.
 */

public class UpdateRequest {
    private String action = "System.updateVersion";

    public String getVersion() {
        return version;
    }

    public UpdateRequest setVersion(String version) {
        this.version = version;
        return this;
    }

    private String version;
}
