package com.hyphenate.easeui.model;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kongqing on 17-3-4.
 */

public class UserInfo implements Serializable {
    public static String getIcon(EMMessage message) {
        try {
            return message.getStringAttribute("userheader");
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUid(EMMessage message) {
        try {
            return message.getStringAttribute("from");
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNick(EMMessage message) {
        try {
            return message.getStringAttribute("username");
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, User> getInfo() {
        return info;
    }

   /* private void setInfo(Map<String, User> info) {
        this.info = info;
    }*/

    public void addInfo(User userInfo) {
        if (info == null) {
            info = new HashMap<>();
        }
        info.put(userInfo.getUid(), userInfo);

    }

    private Map<String, User> info = new HashMap<>();

    private UserInfo() {

    }

    private static UserInfo userInfo;

    public static void setUserInfo(UserInfo userInfo) {
        UserInfo.userInfo = userInfo;
    }

    public static synchronized UserInfo getInstance() {
        if (userInfo == null) {
            userInfo = INIT.userInfo;
        }
        return userInfo;
    }

    private static class INIT {
        private static final UserInfo userInfo = new UserInfo();
    }

    public static class User implements Serializable {
        private String uid;

        public String getUid() {
            return uid;
        }

        public User setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getNick() {
            return nick;
        }

        public User setNick(String nick) {
            this.nick = nick;
            return this;
        }

        public String getIcon() {
            return icon;
        }

        public User setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        private String icon;
        private String nick;
    }
}
