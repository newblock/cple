package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class GroupMembersEntity extends Entity{
    private int driversCount;
    private int membersCount;
    private int passengersCount;
    private int status;
    private List<GroupMember> members;

    public int getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(int passengersCount) {
        this.passengersCount = passengersCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDriversCount() {
        return driversCount;
    }

    public void setDriversCount(int driversCount) {
        this.driversCount = driversCount;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public List<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMember> members) {
        this.members = members;
    }


    public class GroupMember {
        private boolean isAttention;
        private String lastTimeOnline;
        private long phone;
        private String car;
        private int travelCount;
        private int sex;
        private String nickName;
        private String picture;
        private int ranking;

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public boolean isAttention() {
            return isAttention;
        }

        public void setAttention(boolean attention) {
            isAttention = attention;
        }

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

        public int getTravelCount() {
            return travelCount;
        }

        public void setTravelCount(int travelCount) {
            this.travelCount = travelCount;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
