package com.hyphenate.easeui.model;

import java.io.Serializable;

/**
 * Created by kongqing on 17-3-5.
 */

public class UserLodingInFo implements Serializable {
    public static boolean isLoading = false;

    public static synchronized UserLodingInFo getInstance() {
        if (userLodingInFo == null) {
            return INIT.stance;
        }
        return userLodingInFo;
    }

    public static UserLodingInFo userLodingInFo;

    public static void setInstance(UserLodingInFo u) {
        userLodingInFo = u;
    }

    private static class INIT {
        private static final UserLodingInFo stance = new UserLodingInFo();
    }

    private String icon;
    private String nick;
    private String mobile;
    private String id;

    public String getIcon() {
        return icon;
    }

    public UserLodingInFo setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getNick() {
        return nick;
    }

    public UserLodingInFo setNick(String nick) {
        this.nick = nick;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public UserLodingInFo setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getId() {
        return id;
    }

    public UserLodingInFo setId(String id) {
        this.id = id;
        return this;
    }
}
