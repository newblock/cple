package com.qcx.mini.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class TravelEntity extends Entity {
    private long travelId;
    private long passengerTravelId;
    private String ordersId;
    private int type;
    private long startTime;
    private String startTimeTxt;
    private long createTime;
    private double[] start;
    private double[] end;
    private String startAddress;
    private String endAddress;
    private int seats;
    private int surplusSeats;
    private List<String> headPictures;
    private double travelPrice;

    @SerializedName(alternate = {"myPhone"},value = "travelPhone")
    private long travelPhone;
    private int pageViews;
    private int likesNum;
    private int commentsNum;
    private int sharesNum;
    private int status;
    private boolean liked;
    private int ordersNum;
    private double redPacketPrice;
    private String waypoints;
    private String ordersTravelId;

    private String byWay;
    private String startRecommend;
    private String endRecommend;

    public String getByWay() {
        return byWay;
    }

    public void setByWay(String byWay) {
        this.byWay = byWay;
    }

    public String getStartRecommend() {
        return startRecommend;
    }

    public void setStartRecommend(String startRecommend) {
        this.startRecommend = startRecommend;
    }

    public String getEndRecommend() {
        return endRecommend;
    }

    public void setEndRecommend(String endRecommend) {
        this.endRecommend = endRecommend;
    }

    public String getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(String waypoints) {
        this.waypoints = waypoints;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
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

    public String getOrdersTravelId() {
        return ordersTravelId;
    }

    public void setOrdersTravelId(String ordersTravelId) {
        this.ordersTravelId = ordersTravelId;
    }

    public long getPassengerTravelId() {
        return passengerTravelId;
    }

    public void setPassengerTravelId(long passengerTravelId) {
        this.passengerTravelId = passengerTravelId;
    }
}
