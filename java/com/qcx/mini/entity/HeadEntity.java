package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/1/23.
 */

public class HeadEntity  {
    public static final int SAME_NONE=0;
    public static final int SAME_LEFT=1;
    public static final int SAME_RIGHT=2;
    public static final int SAME_LEFT_RIGHT=3;

    private String picture;
    private String statusText;
    private long phone;
    private long lastOnline;
    private double price;
    private String name;
    private int orderStatus;
    private double[] start;
    private double[] end;
    private String startAddress;
    private String endAddress;
    private String ordersId;
    private double[] location;
    private int samePassenger;//0 表示前后都不是同一个人，1 前面是同一个人，2 后面是同一个人， 3前后都是同一个人
    private int sex;
    private long startTime;
    private int passengerNum;
    private boolean isAttention;
    private boolean isPickUp;

    public boolean isPickUp() {
        return isPickUp;
    }

    public HeadEntity setPickUp(boolean pickUp) {
        isPickUp = pickUp;
        return this;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean attention) {
        isAttention = attention;
    }

    public int getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(int passengerNum) {
        this.passengerNum = passengerNum;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSamePassenger() {
        return samePassenger;
    }

    public void setSamePassenger(int samePassenger) {
        this.samePassenger = samePassenger;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
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

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public HeadEntity(){}


    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
