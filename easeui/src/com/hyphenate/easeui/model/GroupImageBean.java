package com.hyphenate.easeui.model;

import java.util.List;

/**
 * Created by kongqing on 17-3-4.
 */

public class GroupImageBean {
    /**
     * error : 0
     * data : [{"icon":"http://192.168.0.19/shequ/public/upload/icons/20170304/b308cfb5a0c5f42c9bf633b67b2cdb96.png","uid":1,"id":15,"group_name":"12","group_public":"21","group_id":1},{"icon":"http://192.168.0.19/shequ/public/upload/icons/20170304/0817ccd0c9f1960bb9b52930bae4febd.png","uid":1,"id":13,"group_name":"测试群","group_public":"测试公告","group_id":1},{"icon":"http://192.168.0.19/shequ/public/upload/icons/20170304/ea9e892ee541d4095bde50566c73fbaf.png","uid":1,"id":7,"group_name":"password","group_public":"123456","group_id":1},{"icon":"http://192.168.0.19/shequ/public/upload/icons/20170304/f0075170bc61126ea912189d7953c08f.png","uid":1,"id":2,"group_name":"password","group_public":"123456","group_id":1}]
     */

    private int            error;
    private List<DataBean> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * icon : http://192.168.0.19/shequ/public/upload/icons/20170304/b308cfb5a0c5f42c9bf633b67b2cdb96.png
         * uid : 1
         * id : 15
         * group_name : 12
         * group_public : 21
         * group_id : 1
         */

        private String icon;
        private int    uid;
        private int    id;
        private String group_name;
        private String group_public;
        private long   group_id;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getGroup_public() {
            return group_public;
        }

        public void setGroup_public(String group_public) {
            this.group_public = group_public;
        }

        public long getGroup_id() {
            return group_id;
        }

        public void setGroup_id(long group_id) {
            this.group_id = group_id;
        }
    }
}
