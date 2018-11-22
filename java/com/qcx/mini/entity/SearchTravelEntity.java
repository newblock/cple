package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class SearchTravelEntity extends Entity {
    private List<DriverAndTravelEntity> TravelRecomment;
    private int status;

    public List<DriverAndTravelEntity> getTravelRecomment() {
        return TravelRecomment;
    }

    public void setTravelRecomment(List<DriverAndTravelEntity> travelRecomment) {
        TravelRecomment = travelRecomment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    //下面的成员都是老接口的成员
    private List<DriverAndTravelEntity> searchTravels;
    private List<SearchGroupEntity> groupLines;

    public List<SearchGroupEntity> getGroupLines() {
        return groupLines;
    }

    public void setGroupLines(List<SearchGroupEntity> groupLines) {
        this.groupLines = groupLines;
    }

    public List<DriverAndTravelEntity> getSearchTravels() {
        return searchTravels;
    }

    public void setSearchTravels(List<DriverAndTravelEntity> searchTravels) {
        this.searchTravels = searchTravels;
    }


    public static class SearchGroupEntity extends Entity{
        private String startAddressAlias;
        private int groupType;
        private long groupId;
        private String endAddressAlias;
        private boolean inGroup;
        private String GroupImg;
        private String GroupTitle;
        private String GroupNotice;
        private int peopleNum;
        private boolean isFirstItem;
        private boolean isLastItem;
        private boolean havesTravel;

        public String getGroupTitle() {
            return GroupTitle;
        }

        public void setGroupTitle(String groupTitle) {
            GroupTitle = groupTitle;
        }

        public String getGroupNotice() {
            return GroupNotice;
        }

        public void setGroupNotice(String groupNotice) {
            GroupNotice = groupNotice;
        }

        public boolean isHavesTravel() {
            return havesTravel;
        }

        public void setHavesTravel(boolean havesTravel) {
            this.havesTravel = havesTravel;
        }

        public boolean isFirstItem() {
            return isFirstItem;
        }

        public void setFirstItem(boolean firstItem) {
            isFirstItem = firstItem;
        }

        public boolean isLastItem() {
            return isLastItem;
        }

        public void setLastItem(boolean lastItem) {
            isLastItem = lastItem;
        }

        public String getStartAddressAlias() {
            return startAddressAlias;
        }

        public void setStartAddressAlias(String startAddressAlias) {
            this.startAddressAlias = startAddressAlias;
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

        public String getEndAddressAlias() {
            return endAddressAlias;
        }

        public void setEndAddressAlias(String endAddressAlias) {
            this.endAddressAlias = endAddressAlias;
        }

        public boolean isInGroup() {
            return inGroup;
        }

        public void setInGroup(boolean inGroup) {
            this.inGroup = inGroup;
        }

        public String getGroupImg() {
            return GroupImg;
        }

        public void setGroupImg(String groupImg) {
            GroupImg = groupImg;
        }

        public int getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(int peopleNum) {
            this.peopleNum = peopleNum;
        }
    }
}
