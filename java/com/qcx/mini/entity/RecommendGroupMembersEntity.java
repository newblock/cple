package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class RecommendGroupMembersEntity extends Entity {
    private List<Member> matchUsers;

    public List<Member> getMatchUsers() {
        return matchUsers;
    }

    public void setMatchUsers(List<Member> matchUsers) {
        this.matchUsers = matchUsers;
    }

    public class Member{
        private String lastTimeOnline;
        private long phone;
        private String car;
        private String nickName;
        private int sex;
        private String picture;
        private boolean  isSelected;

        public String getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(String lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
