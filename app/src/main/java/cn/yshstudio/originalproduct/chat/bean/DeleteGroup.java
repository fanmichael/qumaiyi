package cn.yshstudio.originalproduct.chat.bean;

/**
 * Created by kongqing on 2017/3/23.
 */

public class DeleteGroup {
    private String action = "Group.delGroup";
    private String groupid;
    private String uid;

    public String getAction() {
        return action;
    }

    public DeleteGroup setAction(String action) {
        this.action = action;
        return this;
    }

    public String getGroupid() {
        return groupid;
    }

    public DeleteGroup setGroupid(String groupid) {
        this.groupid = groupid;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public DeleteGroup setUid(String uid) {
        this.uid = uid;
        return this;
    }
}
