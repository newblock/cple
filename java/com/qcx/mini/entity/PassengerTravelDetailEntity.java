package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */

public class PassengerTravelDetailEntity extends Entity {
    private OrdersTravelDetail ordersTravelDetailVo;
    private int status;

    public OrdersTravelDetail getOrdersTravelDetailVo() {
        return ordersTravelDetailVo;
    }

    public void setOrdersTravelDetailVo(OrdersTravelDetail ordersTravelDetailVo) {
        this.ordersTravelDetailVo = ordersTravelDetailVo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class  OrdersTravelDetail{
        private double[] start;
        private double[] end;
        private String startAddress;
        private String endAddress;
        private int strategy;
        private String waypoints;
        private long startTime;
        private int createType;
        private double travelPrice;
        private long passengerTravelId;
        private String driverPicture;
        private String nickName;
        private int sex;
        private String carNumber;
        private String car;
        private long driverPhone;
        private long lastTimeOnline;
        private double[] location;
        private boolean attention;
        private String ordersId;
        private String ordersTravelId;
        private long ordersStartTime;
        private double[] ordersStart;
        private double[] ordersEnd;
        private String ordersStartAddress;
        private String ordersEndAddress;
        private int bookSeats;
        private double ordersPrice;
        private int ordersStatus;
        private long finalTime;
        private List<Picture> fellowTravelerVoList;
        private PayEntity noPayOrdersVo;
        private boolean currentTravelOrders;
        private int seats;

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public PayEntity getNoPayOrdersVo() {
            return noPayOrdersVo;
        }

        public void setNoPayOrdersVo(PayEntity noPayOrdersVo) {
            this.noPayOrdersVo = noPayOrdersVo;
        }

        public List<Picture> getFellowTravelerVoList() {
            return fellowTravelerVoList;
        }

        public void setFellowTravelerVoList(List<Picture> fellowTravelerVoList) {
            this.fellowTravelerVoList = fellowTravelerVoList;
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

        public int getStrategy() {
            return strategy;
        }

        public void setStrategy(int strategy) {
            this.strategy = strategy;
        }

        public String getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(String waypoints) {
            this.waypoints = waypoints;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getCreateType() {
            return createType;
        }

        public void setCreateType(int createType) {
            this.createType = createType;
        }

        public double getTravelPrice() {
            return travelPrice;
        }

        public void setTravelPrice(double travelPrice) {
            this.travelPrice = travelPrice;
        }

        public long getPassengerTravelId() {
            return passengerTravelId;
        }

        public void setPassengerTravelId(long passengerTravelId) {
            this.passengerTravelId = passengerTravelId;
        }

        public String getDriverPicture() {
            return driverPicture;
        }

        public void setDriverPicture(String driverPicture) {
            this.driverPicture = driverPicture;
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

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public long getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(long driverPhone) {
            this.driverPhone = driverPhone;
        }

        public long getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(long lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public double[] getLocation() {
            return location;
        }

        public void setLocation(double[] location) {
            this.location = location;
        }

        public boolean isAttention() {
            return attention;
        }

        public void setAttention(boolean attention) {
            this.attention = attention;
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

        public long getOrdersStartTime() {
            return ordersStartTime;
        }

        public void setOrdersStartTime(long ordersStartTime) {
            this.ordersStartTime = ordersStartTime;
        }

        public double[] getOrdersStart() {
            return ordersStart;
        }

        public void setOrdersStart(double[] ordersStart) {
            this.ordersStart = ordersStart;
        }

        public double[] getOrdersEnd() {
            return ordersEnd;
        }

        public void setOrdersEnd(double[] ordersEnd) {
            this.ordersEnd = ordersEnd;
        }

        public String getOrdersStartAddress() {
            return ordersStartAddress;
        }

        public void setOrdersStartAddress(String ordersStartAddress) {
            this.ordersStartAddress = ordersStartAddress;
        }

        public String getOrdersEndAddress() {
            return ordersEndAddress;
        }

        public void setOrdersEndAddress(String ordersEndAddress) {
            this.ordersEndAddress = ordersEndAddress;
        }

        public int getBookSeats() {
            return bookSeats;
        }

        public void setBookSeats(int bookSeats) {
            this.bookSeats = bookSeats;
        }

        public double getOrdersPrice() {
            return ordersPrice;
        }

        public void setOrdersPrice(double ordersPrice) {
            this.ordersPrice = ordersPrice;
        }

        public int getOrdersStatus() {
            return ordersStatus;
        }

        public void setOrdersStatus(int ordersStatus) {
            this.ordersStatus = ordersStatus;
        }

        public long getFinalTime() {
            return finalTime;
        }

        public void setFinalTime(long finalTime) {
            this.finalTime = finalTime;
        }

        public boolean isCurrentTravelOrders() {
            return currentTravelOrders;
        }

        public void setCurrentTravelOrders(boolean currentTravelOrders) {
            this.currentTravelOrders = currentTravelOrders;
        }
    }

    public class Picture{
        private String picture;
        private long phone;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }
    }
}
