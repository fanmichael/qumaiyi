package com.yshstudio.originalproduct.chat.bean;

/**
 * Created by kongqing on 2017/3/23.
 */

public class AddGroupMmberResponse {

    /**
     * error : 0
     * data : {"result":true,"groupid":"1490013077252","action":"add_member","user":"15625052273"}
     */

    private int error;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
    private DataBean data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * result : true
         * groupid : 1490013077252
         * action : add_member
         * user : 15625052273
         */

        private boolean result;
        private String groupid;
        private String action;
        private String user;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}
