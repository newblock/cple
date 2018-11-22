package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */

public class WayListEntity extends Entity {
    private MyJoinedGroups myJoinedGroups;
    private int status;

    public MyJoinedGroups getMyJoinedGroups() {
        return myJoinedGroups;
    }

    public void setMyJoinedGroups(MyJoinedGroups myJoinedGroups) {
        this.myJoinedGroups = myJoinedGroups;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class MyJoinedGroups extends Entity{
        private boolean isRecommend;
        private List<GroupItem> groupsList;

        public boolean isRecommend() {
            return isRecommend;
        }

        public void setRecommend(boolean recommend) {
            isRecommend = recommend;
        }

        public List<GroupItem> getGroupsList() {
            return groupsList;
        }

        public void setGroupsList(List<GroupItem> groupsList) {
            this.groupsList = groupsList;
        }
    }

    public class GroupItem{
        private long groupId;
        private String groupTitle;
        private String groupBanner;
        private String groupNotice;
        private boolean isJoined;
        private int groupType;
        private double groupAdvicePrice;
        private int distance;
        private int groupNum;

        public String getGroupNotice() {
            return groupNotice;
        }

        public void setGroupNotice(String groupNotice) {
            this.groupNotice = groupNotice;
        }

        public double getGroupAdvicePrice() {
            return groupAdvicePrice;
        }

        public void setGroupAdvicePrice(double groupAdvicePrice) {
            this.groupAdvicePrice = groupAdvicePrice;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getGroupNum() {
            return groupNum;
        }

        public void setGroupNum(int groupNum) {
            this.groupNum = groupNum;
        }

        public int getGroupType() {
            return groupType;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
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

        public String getGroupBanner() {
            return groupBanner;
        }

        public void setGroupBanner(String groupBanner) {
            this.groupBanner = groupBanner;
        }

        public boolean isJoined() {
            return isJoined;
        }

        public void setJoined(boolean joined) {
            isJoined = joined;
        }
    }
}
