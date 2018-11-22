package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/6/4.
 */

public class PassengerEntity extends Entity {
/**
 * "passengerPhone": 15890349336,
 "nickName": "回家",
 "sex": 2,
 "lastTimeOnline": 1528084235829,
 "picture": "https://t1.driver.quchuxing.com.cn/resources/pictures/1519870197677.jpg",
 "ordersId": "152808370781315890349336",
 "ordersTravelId": "6193949554679915890349336",
 "start": [116.310959, 40.036743],
 "startAddress": "北京市海淀区上地街道上地三街7号中关村创业大厦",
 "end": [116.30933, 40.03698],
 "endAddress": "金隅嘉华大厦",
 "orderStatus": 2,
 "orderStatusDetail": "已支付",
 "location": [116.3114103190104, 40.03745144314236],
 "pickUp": false,
 "distanceFromTravelStart": 0,
 "bookSeats": 1,
 "ticketPrice": 5.0,
 "getOnTime": 1528095617000,
 "getOnTimeTxt": "6月4日15:00",
 "attention": true
 */
    private long passengerPhone;
    private String nickName;
    private int sex;
    private long lastTimeOnline;
    private String picture;
    private String ordersId;
    private String ordersTravelId;
    private double[] start;
    private String startAddress;
    private double[] end;
    private String endAddress;
    private int orderStatus;
    private String orderStatusDetail;
    private double[] location;
    private boolean pickUp;
    private int distanceFromTravelStart;
    private int bookSeats;
    private double ticketPrice;
    private long getOnTime;
    private String getOnTimeTxt;
    private boolean attention;

    public long getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(long passengerPhone) {
        this.passengerPhone = passengerPhone;
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

    public long getLastTimeOnline() {
        return lastTimeOnline;
    }

    public void setLastTimeOnline(long lastTimeOnline) {
        this.lastTimeOnline = lastTimeOnline;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getOrdersTravelId() {
        return ordersTravelId;
    }

    public void setOrdersTravelId(String ordersTravelId) {
        this.ordersTravelId = ordersTravelId;
    }

    public double[] getStart() {
        return start;
    }

    public void setStart(double[] start) {
        this.start = start;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public double[] getEnd() {
        return end;
    }

    public void setEnd(double[] end) {
        this.end = end;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDetail() {
        return orderStatusDetail;
    }

    public void setOrderStatusDetail(String orderStatusDetail) {
        this.orderStatusDetail = orderStatusDetail;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public boolean isPickUp() {
        return pickUp;
    }

    public void setPickUp(boolean pickUp) {
        this.pickUp = pickUp;
    }

    public int getDistanceFromTravelStart() {
        return distanceFromTravelStart;
    }

    public void setDistanceFromTravelStart(int distanceFromTravelStart) {
        this.distanceFromTravelStart = distanceFromTravelStart;
    }

    public int getBookSeats() {
        return bookSeats;
    }

    public void setBookSeats(int bookSeats) {
        this.bookSeats = bookSeats;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public long getGetOnTime() {
        return getOnTime;
    }

    public void setGetOnTime(long getOnTime) {
        this.getOnTime = getOnTime;
    }

    public String getGetOnTimeTxt() {
        return getOnTimeTxt;
    }

    public void setGetOnTimeTxt(String getOnTimeTxt) {
        this.getOnTimeTxt = getOnTimeTxt;
    }

    public boolean isAttention() {
        return attention;
    }

    public void setAttention(boolean attention) {
        this.attention = attention;
    }
}
