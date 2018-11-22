package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */

public class FollowerEntity extends Entity {
    private int status;
    private List<Follower> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Follower> getList() {
        return list;
    }

    public void setList(List<Follower> list) {
        this.list = list;
    }

    public class Follower{
        private String lastTimeOnline;
        private String car;
        private String picture;
        private String nickName;
        private long phone;
        private int sex;
        private int attentionStatus;

        public String getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(String lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            picture = picture;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAttentionStatus() {
            return attentionStatus;
        }

        public void setAttentionStatus(int attentionStatus) {
            this.attentionStatus = attentionStatus;
        }
    }
}
