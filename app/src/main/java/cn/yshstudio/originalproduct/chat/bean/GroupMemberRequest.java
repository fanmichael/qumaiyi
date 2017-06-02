package cn.yshstudio.originalproduct.chat.bean;

/**
 * Created by kongqing on 17-3-14.
 */

public class GroupMemberRequest {

    /**
     * action : Group.groupMember
     * groupid : 1489226403646
     */

    private String action = "Group.groupMember";
    private String groupid;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getGroupid() {
        return groupid;
    }

    public GroupMemberRequest setGroupid(String groupid) {
        this.groupid = groupid;
        return this;
    }
}
