package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */

public class HotGroupsEntity extends Entity {
    private List<HotGroup> hotGroups;
    private int status;

    public List<HotGroup> getHotGroups() {
        return hotGroups;
    }

    public void setHotGroups(List<HotGroup> hotGroups) {
        this.hotGroups = hotGroups;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class HotGroup{
        private String startCity;
        private double advisePrice;
        private int groupType;
        private String groupNotice;
        private long groupId;
        private String groupTitle;
        private int groupMemberNum;
        private int distance;
        private String groupBanner;
        private String endCity;
        private boolean isJoined;

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getStartCity() {
            return startCity;
        }

        public void setStartCity(String startCity) {
            this.startCity = startCity;
        }

        public double getAdvisePrice() {
            return advisePrice;
        }

        public void setAdvisePrice(double advisePrice) {
            this.advisePrice = advisePrice;
        }

        public int getGroupType() {
            return groupType;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
        }

        public String getGroupNotice() {
            return groupNotice;
        }

        public void setGroupNotice(String groupNotice) {
            this.groupNotice = groupNotice;
        }

        public long getGroupId() {
            return groupId;
        }

        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }

        public String getGroupTitle() {
            return groupTitle;
        }

        public void setGroupTitle(String groupTitle) {
            this.groupTitle = groupTitle;
        }

        public int getGroupMemberNum() {
            return groupMemberNum;
        }

        public void setGroupMemberNum(int groupMemberNum) {
            this.groupMemberNum = groupMemberNum;
        }

        public String getGroupBanner() {
            return groupBanner;
        }

        public void setGroupBanner(String groupBanner) {
            this.groupBanner = groupBanner;
        }

        public String getEndCity() {
            return endCity;
        }

        public void setEndCity(String endCity) {
            this.endCity = endCity;
        }

        public boolean isJoined() {
            return isJoined;
        }

        public void setJoined(boolean joined) {
            isJoined = joined;
        }
    }
}
