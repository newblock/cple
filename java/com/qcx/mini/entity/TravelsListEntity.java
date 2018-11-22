package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class TravelsListEntity extends Entity {
    private List<TravelEntity> result;
    private int status;
    private String nickName;
    private String picture;
    private TravelEntity historyTravel;

    public TravelEntity getHistoryTravel() {
        return historyTravel;
    }

    public void setHistoryTravel(TravelEntity historyTravel) {
        this.historyTravel = historyTravel;
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

    public List<TravelEntity> getResult() {
        return result;
    }

    public void setResult(List<TravelEntity> result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class TravelEntity extends Entity{
        private long travelId;
        private int type;
        private long startTime;
        private String startTimeTxt;
        private long createTime;
        private String startAddress;
        private String endAddress;
        private int seats;
        private int surplusSeats;
        private List<String> headPictures;
        private double travelPrice;
        private long travelPhone;
        private int pageViews;
        private int likesNum;
        private int commentsNum;
        private int sharesNum;
        private int status;
        private boolean liked;
        private int ordersNum;
        private double redPacketPrice;
        private double[] start;
        private double[] end;
        private String ordersId;
        private String waypoints;
        private UserInfo driverInfo;
        private boolean attention;
        private String ordersTravelId;

        public String getOrdersTravelId() {
            return ordersTravelId;
        }

        public void setOrdersTravelId(String ordersTravelId) {
            this.ordersTravelId = ordersTravelId;
        }

        public boolean isAttention() {
            return attention;
        }

        public void setAttention(boolean attention) {
            this.attention = attention;
        }

        public UserInfo getDriverInfo() {
            return driverInfo;
        }

        public void setDriverInfo(UserInfo driverInfo) {
            this.driverInfo = driverInfo;
        }

        public String getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(String waypoints) {
            this.waypoints = waypoints;
        }

        public String getOrdersId() {
            return ordersId;
        }

        public void setOrdersId(String ordersId) {
            this.ordersId = ordersId;
        }

        public double[] getStart() {
            return start;
        }

        public void setStart(double[] start) {
            this.start = start;
        }

        public double[] getEnd() {
            return end;
        }

        public void setEnd(double[] end) {
            this.end = end;
        }

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getStartTimeTxt() {
            return startTimeTxt;
        }

        public void setStartTimeTxt(String startTimeTxt) {
            this.startTimeTxt = startTimeTxt;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public String getEndAddress() {
            return endAddress;
        }

        public void setEndAddress(String endAddress) {
            this.endAddress = endAddress;
        }

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public int getSurplusSeats() {
            return surplusSeats;
        }

        public void setSurplusSeats(int surplusSeats) {
            this.surplusSeats = surplusSeats;
        }

        public List<String> getHeadPictures() {
            return headPictures;
        }

        public void setHeadPictures(List<String> headPictures) {
            this.headPictures = headPictures;
        }

        public double getTravelPrice() {
            return travelPrice;
        }

        public void setTravelPrice(double travelPrice) {
            this.travelPrice = travelPrice;
        }

        public long getTravelPhone() {
            return travelPhone;
        }

        public void setTravelPhone(long travelPhone) {
            this.travelPhone = travelPhone;
        }

        public int getPageViews() {
            return pageViews;
        }

        public void setPageViews(int pageViews) {
            this.pageViews = pageViews;
        }

        public int getLikesNum() {
            return likesNum;
        }

        public void setLikesNum(int likesNum) {
            this.likesNum = likesNum;
        }

        public int getCommentsNum() {
            return commentsNum;
        }

        public void setCommentsNum(int commentsNum) {
            this.commentsNum = commentsNum;
        }

        public int getSharesNum() {
            return sharesNum;
        }

        public void setSharesNum(int sharesNum) {
            this.sharesNum = sharesNum;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public int getOrdersNum() {
            return ordersNum;
        }

        public void setOrdersNum(int ordersNum) {
            this.ordersNum = ordersNum;
        }

        public double getRedPacketPrice() {
            return redPacketPrice;
        }

        public void setRedPacketPrice(double redPacketPrice) {
            this.redPacketPrice = redPacketPrice;
        }
    }

    public class UserInfo{
        private String picture;
        private String car;
        private String nickName;
        private String age;
        private String carNumber;
        private int sex;
        private long phone;

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
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

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
