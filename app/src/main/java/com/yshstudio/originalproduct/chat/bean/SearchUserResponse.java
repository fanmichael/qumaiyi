package com.yshstudio.originalproduct.chat.bean;

import java.util.List;

/**
 * Created by kongqing on 17-3-15.
 */

public class SearchUserResponse {

    /**
     * error : 0
     * data : [{"id":18,"mobile":"15001372759","nick":"一头发长","icon":"/upload/icons/20170313/6f55d1b92069eb9c85bc23c32cb137f8.jpg","gender":0,"location":"","personalized":null}]
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
         * id : 18
         * mobile : 15001372759
         * nick : 一头发长
         * icon : /upload/icons/20170313/6f55d1b92069eb9c85bc23c32cb137f8.jpg
         * gender : 0
         * location :
         * personalized : null
         */

        private int    id;
        private String mobile;
        private String nick;
        private String icon;
        private int    gender;
        private String location;
        private Object personalized;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Object getPersonalized() {
            return personalized;
        }

        public void setPersonalized(Object personalized) {
            this.personalized = personalized;
        }
    }
}
