package cn.yshstudio.originalproduct.chat.bean;

/**
 * Created by kongqing on 17-3-15.
 */

public class SearchUser {
    private String action = "User.searchUsers";

    public String getContent() {
        return content;
    }

    public SearchUser setContent(String content) {
        this.content = content;
        return this;
    }

    public String getAction() {
        return action;
    }

    public SearchUser setAction(String action) {
        this.action = action;
        return this;
    }

    private String content;
}
