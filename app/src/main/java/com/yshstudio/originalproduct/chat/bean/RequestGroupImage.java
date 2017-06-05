package com.yshstudio.originalproduct.chat.bean;

/**
 * Created by kongqing on 17-3-4.
 */

public class RequestGroupImage {
    private String action = "Group.groupQuery";

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;
}
