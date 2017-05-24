package com.hyphenate.easeui.model;

/**
 * Created by kongqing on 17-3-4.
 */

public class UserBean {
    private String nick;

    public String getIcon() {
        return icon;
    }

    public UserBean setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getNick() {
        return nick;
    }

    public UserBean setNick(String nick) {
        this.nick = nick;
        return this;
    }

    private String icon;
}
