package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ReleaseResultEntity extends Entity {
    private List<Long> travelIds;
    private List<Long> passengerTravelIds;
    private List<JoinedGroup> joinedGroups;
    private int status;
    private int travelType;

    public int getTravelType() {
        return travelType;
    }

    public void setTravelType(int travelType) {
        this.travelType = travelType;
    }

    public List<Long> getPassengerTravelIds() {
        return passengerTravelIds;
    }

    public void setPassengerTravelIds(List<Long> passengerTravelIds) {
        this.passengerTravelIds = passengerTravelIds;
    }

    public List<Long> getTravelIds() {
        return travelIds;
    }

    public void setTravelIds(List<Long> travelIds) {
        this.travelIds = travelIds;
    }

    public List<JoinedGroup> getJoinedGroups() {
        return joinedGroups;
    }

    public void setJoinedGroups(List<JoinedGroup> joinedGroups) {
        this.joinedGroups = joinedGroups;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class JoinedGroup{
        private long groupId;
        private String groupTitle;
        private boolean isJoined;
        private boolean isCheck;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
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

        public boolean isJoined() {
            return isJoined;
        }

        public void setJoined(boolean joined) {
            isJoined = joined;
        }
    }
}
