package cn.com.shequnew.chat.eventtype;

/**
 * Created by kongqing on 17-3-4.
 */

public class LoginIM {
    private String icon;
    private String nick;
    private String mobile;
    private String id;

    public String getIcon() {
        return icon;
    }

    public LoginIM setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getNick() {
        return nick;
    }

    public LoginIM setNick(String nick) {
        this.nick = nick;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public LoginIM setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getId() {
        return id;
    }

    public LoginIM setId(String id) {
        this.id = id;
        return this;
    }
}
