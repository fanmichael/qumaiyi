package cn.yshstudio.originalproduct.chat.bean;

/**
 * Created by kongqing on 17-3-16.
 */

public class UpdateResponse {

    /**
     * error : 0
     * data : {"apk_url":"http://www.baidu.com","server_version":"1.201","info":"新版本来啦"}
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
         * apk_url : http://www.baidu.com
         * server_version : 1.201
         * info : 新版本来啦
         */

        private String apk_url;
        private String server_version;
        private String info;

        public String getApk_url() {
            return apk_url;
        }

        public void setApk_url(String apk_url) {
            this.apk_url = apk_url;
        }

        public String getServer_version() {
            return server_version;
        }

        public void setServer_version(String server_version) {
            this.server_version = server_version;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
