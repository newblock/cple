package com.qcx.mini.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10.
 */

public class GroupInfoEntity extends Entity {
    private double advisePrice;
    private GroupMembersEntity groupMembers;
    private int groupType;
    private String groupNotice;
    private String startAddress;
    private long groupId;
    private double[] start;
    private String groupTitle;
    private double[] end;
    private String groupManager;
    private String endAddress;
    private String groupBanner;
    private boolean isJoined;
    private int status;
    private int distance;
    private Map<String,List<GroupTravelEntity>> thatDayTravels;//上线班特有
    private List<DriverAndTravelEntity> travelOneVos;//景点群特有
    private List<String> citys;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<String> getCitys() {
        return citys;
    }

    public void setCitys(List<String> citys) {
        this.citys = citys;
    }

    public String getGroupBanner() {
        return groupBanner;
    }

    public void setGroupBanner(String groupBanner) {
        this.groupBanner = groupBanner;
    }

    public List<DriverAndTravelEntity> getTravelOneVos() {
        return travelOneVos;
    }

    public void setTravelOneVos(List<DriverAndTravelEntity> travelOneVos) {
        this.travelOneVos = travelOneVos;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }

    public double getAdvisePrice() {
        return advisePrice;
    }

    public void setAdvisePrice(double advisePrice) {
        this.advisePrice = advisePrice;
    }

    public GroupMembersEntity getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(GroupMembersEntity groupMembers) {
        this.groupMembers = groupMembers;
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

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public double[] getStart() {
        return start;
    }

    public void setStart(double[] start) {
        this.start = start;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public double[] getEnd() {
        return end;
    }

    public void setEnd(double[] end) {
        this.end = end;
    }

    public String getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(String groupManager) {
        this.groupManager = groupManager;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String,List<GroupTravelEntity>> getThatDayTravels() {
        return thatDayTravels;
    }

    public void setThatDayTravels(Map<String,List<GroupTravelEntity>> thatDayTravels) {
        this.thatDayTravels = thatDayTravels;
    }

    public class GroupTravelEntity{
        private long travelId;
        private String startAddress;
        private double price;
        private long startTime;
        private int surplusSeats;
        private String endAddress;
        private String picture;
        private String title;
        private int travelType;
        private int status;
        private int seats;

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTravelType() {
            return travelType;
        }

        public void setTravelType(int travelType) {
            this.travelType = travelType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "travelId="+travelId+"\n startAddress="+startAddress
                    +"\n price="+price
                    +"\n startTime="+startTime
                    +"\n surplusSeats="+surplusSeats
                    +"\n endAddress="+endAddress
                    +"\n picture="+picture;
        }

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getSurplusSeats() {
            return surplusSeats;
        }

        public void setSurplusSeats(int surplusSeats) {
            this.surplusSeats = surplusSeats;
        }

        public String getEndAddress() {
            return endAddress;
        }

        public void setEndAddress(String endAddress) {
            this.endAddress = endAddress;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
