package cn.com.shequnew.chat.bean;

import java.util.List;

/**
 * Created by kongqing on 17-3-14.
 */

public class GroupMember {

    /**
     * error : 0
     * data : {"group_info":{"group_name":"从","group_public":"不来了"},"list":[{"id":49,"mobile":"13689510665","password":"23c49ebbd5a76d0e965f7dfdd0ac5a51","nick":"小艺","icon":"/static/images/icon.png","gender":0,"location":"北京市 崇文区","personalized":null,"sign":0,"perple":"","think":"","hxgroups":"21,20,19,18,17,16,15,14,9,8,7,5,4,","openid":"","oauthtype":""},{"id":31,"mobile":"","password":"","nick":"安","icon":"/upload/icons/20170313/42e832df6c10507e7f5a3f05cd00638d.PNG","gender":0,"location":"海外 海外","personalized":"没有","sign":0,"perple":"","think":"","hxgroups":"7,19,18,17,16,15,14,9,8,5,4,27,24,23,","openid":"oYbN3uNK8Df5jdcL9LsA0bFxs9ko","oauthtype":"weixin"},{"id":22,"mobile":"15768251556","password":"d6894682e5b40c57c009fa5c4507947f","nick":"西西","icon":"/upload/icons/20170305/f9a42c278923981972042b57c9f176e6.jpg","gender":0,"location":"广东省 深圳市","personalized":"没有不可能","sign":0,"perple":"2","think":"","hxgroups":"16,9,8,7,20,","openid":"","oauthtype":""},{"id":21,"mobile":"18038178279","password":"0263bcf70efc6b086280efe4c8d5bf2e","nick":"Net 领域","icon":"/upload/icons/20170311/db2d29db2dba5779174b5ae9f3ae3e60.PNG","gender":0,"location":"北京市 崇文区","personalized":"测试啦啦啦","sign":0,"perple":"","think":"","hxgroups":"16,14,9,8,7,5,","openid":"","oauthtype":""},{"id":18,"mobile":"15001372759","password":"e10adc3949ba59abbe56e057f20f883e","nick":"一头发长","icon":"/upload/icons/20170313/6f55d1b92069eb9c85bc23c32cb137f8.jpg","gender":0,"location":"","personalized":null,"sign":0,"perple":"","think":"","hxgroups":"5,4,","openid":"","oauthtype":""}]}
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
         * group_info : {"group_name":"从","group_public":"不来了"}
         * list : [{"id":49,"mobile":"13689510665","password":"23c49ebbd5a76d0e965f7dfdd0ac5a51","nick":"小艺","icon":"/static/images/icon.png","gender":0,"location":"北京市 崇文区","personalized":null,"sign":0,"perple":"","think":"","hxgroups":"21,20,19,18,17,16,15,14,9,8,7,5,4,","openid":"","oauthtype":""},{"id":31,"mobile":"","password":"","nick":"安","icon":"/upload/icons/20170313/42e832df6c10507e7f5a3f05cd00638d.PNG","gender":0,"location":"海外 海外","personalized":"没有","sign":0,"perple":"","think":"","hxgroups":"7,19,18,17,16,15,14,9,8,5,4,27,24,23,","openid":"oYbN3uNK8Df5jdcL9LsA0bFxs9ko","oauthtype":"weixin"},{"id":22,"mobile":"15768251556","password":"d6894682e5b40c57c009fa5c4507947f","nick":"西西","icon":"/upload/icons/20170305/f9a42c278923981972042b57c9f176e6.jpg","gender":0,"location":"广东省 深圳市","personalized":"没有不可能","sign":0,"perple":"2","think":"","hxgroups":"16,9,8,7,20,","openid":"","oauthtype":""},{"id":21,"mobile":"18038178279","password":"0263bcf70efc6b086280efe4c8d5bf2e","nick":"Net 领域","icon":"/upload/icons/20170311/db2d29db2dba5779174b5ae9f3ae3e60.PNG","gender":0,"location":"北京市 崇文区","personalized":"测试啦啦啦","sign":0,"perple":"","think":"","hxgroups":"16,14,9,8,7,5,","openid":"","oauthtype":""},{"id":18,"mobile":"15001372759","password":"e10adc3949ba59abbe56e057f20f883e","nick":"一头发长","icon":"/upload/icons/20170313/6f55d1b92069eb9c85bc23c32cb137f8.jpg","gender":0,"location":"","personalized":null,"sign":0,"perple":"","think":"","hxgroups":"5,4,","openid":"","oauthtype":""}]
         */

        private GroupInfoBean  group_info;
        private List<ListBean> list;

        public GroupInfoBean getGroup_info() {
            return group_info;
        }

        public void setGroup_info(GroupInfoBean group_info) {
            this.group_info = group_info;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class GroupInfoBean {
            /**
             * group_name : 从
             * group_public : 不来了
             */

            private String group_name;
            private String group_public;

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
        }

        public static class ListBean {
            /**
             * id : 49
             * mobile : 13689510665
             * password : 23c49ebbd5a76d0e965f7dfdd0ac5a51
             * nick : 小艺
             * icon : /static/images/icon.png
             * gender : 0
             * location : 北京市 崇文区
             * personalized : null
             * sign : 0
             * perple :
             * think :
             * hxgroups : 21,20,19,18,17,16,15,14,9,8,7,5,4,
             * openid :
             * oauthtype :
             */

            private int    id;
            private String mobile;
            private String password;
            private String nick;
            private String icon;
            private int    gender;
            private String location;
            private Object personalized;
            private int    sign;
            private String perple;
            private String think;
            private String hxgroups;
            private String openid;
            private String oauthtype;

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

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
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

            public int getSign() {
                return sign;
            }

            public void setSign(int sign) {
                this.sign = sign;
            }

            public String getPerple() {
                return perple;
            }

            public void setPerple(String perple) {
                this.perple = perple;
            }

            public String getThink() {
                return think;
            }

            public void setThink(String think) {
                this.think = think;
            }

            public String getHxgroups() {
                return hxgroups;
            }

            public void setHxgroups(String hxgroups) {
                this.hxgroups = hxgroups;
            }

            public String getOpenid() {
                return openid;
            }

            public void setOpenid(String openid) {
                this.openid = openid;
            }

            public String getOauthtype() {
                return oauthtype;
            }

            public void setOauthtype(String oauthtype) {
                this.oauthtype = oauthtype;
            }
        }
    }
}
