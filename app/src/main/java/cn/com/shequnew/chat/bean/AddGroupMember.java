package cn.com.shequnew.chat.bean;

/**
 * Created by kongqing on 2017/3/23.
 */

public class AddGroupMember {
    private String action = "Group.addMember";
    private String uid;
    private String groupid;

    public String getUid() {
        return uid;
    }

    public AddGroupMember setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getGroupid() {
        return groupid;
    }

    public AddGroupMember setGroupid(String groupid) {
        this.groupid = groupid;
        return this;
    }
}
