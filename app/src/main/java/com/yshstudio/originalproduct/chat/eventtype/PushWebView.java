package com.yshstudio.originalproduct.chat.eventtype;

/**
 * Created by kongqing on 17-3-7.
 */

public class PushWebView {
    public static final int PUSH = 1;
    public static final int BACK = 2;

    public int getTYPE() {
        return TYPE;
    }

    public PushWebView setTYPE(int TYPE) {
        this.TYPE = TYPE;
        return this;
    }

    private int TYPE = -1;
}
