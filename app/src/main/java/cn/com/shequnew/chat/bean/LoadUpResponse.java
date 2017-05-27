package cn.com.shequnew.chat.bean;

/**
 * Created by kongqing on 17-3-3.
 */

public class LoadUpResponse {

    /**
     * error : 0
     * data : {"uid":"1","group_id":"1","icon":"/upload/icons/20170303/c692b1bc4f17656e3af909ff9768d020.png","group_name":"password","group_public":"123456","id":"6"}
     */

    private int      error;
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
         * uid : 1
         * group_id : 1
         * icon : /upload/icons/20170303/c692b1bc4f17656e3af909ff9768d020.png
         * group_name : password
         * group_public : 123456
         * id : 6
         */

        private String uid;
        private String group_id;
        private String icon;
        private String group_name;
        private String group_public;
        private String id;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
