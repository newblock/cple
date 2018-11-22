package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class TravelDetail_PassengerEntity extends Entity {
    /**
     *
     "travel": {
     "recommendStartTime": 1516698000000,
     "lastTimeOnline": 1516610751276,
     "ordersStatus": 2,
     "bookSeats": 1,
     "sex": 2,
     "driverStartAddress": "实创大厦",
     "recommendStart": [116.31148529052734, 40.038047790527344],
     "picture": "https://t1.driver.quchuxing.com.cn/resources/pictures/1513253884232.jpg",
     "carNumber": "苏C***B5",
     "driverEndAddress": "望京(地铁站)",
     "car": "宝马白色",
     "driverStart": [116.31148529052734, 40.038047790527344],
     "nickname": "宋**老板",
     "recommendStartAddress": "实创大厦",
     "recommendEnd": [116.46940612792969, 39.99851989746094],
     "driverPhone": 18515064016,
     "startTime": 1516698000000,
     "recommendEndAddress": "",
     "driverEnd": [116.46940612792969, 39.99851989746094]},
     "status": 200,
     "sameWayPassengers": [{passengerPhone": 15890349336,passengerPicture": "https://t1.driver.quchuxing.com.cn/resources/pictures/1514892200562.jpg"}]
     */
    private Travel travel;
    private int status;
    private List<Passenger> sameWayPassengers;

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Passenger> getSameWayPassengers() {
        return sameWayPassengers;
    }

    public void setSameWayPassengers(List<Passenger> sameWayPassengers) {
        this.sameWayPassengers = sameWayPassengers;
    }

    public class Travel{
        private long recommendStartTime;
        private long lastTimeOnline;
        private int ordersStatus;
        private int bookSeats;
        private int sex;
        private String driverStartAddress;
        private double[] recommendStart;
        private String picture;
        private String carNumber;
        private String driverEndAddress;
        private String car;
        private String ordersId;
        private double[] driverStart;
        private String nickname;
        private String recommendStartAddress;
        private double[] recommendEnd;
        private long driverPhone;
        private long startTime;
        private String recommendEndAddress;
        private double[] driverEnd;
        private long finalTime;
        private double price;
        private long passengerTravelId;
        private int strategy;
        private String waypoints;
        private boolean isAttention;
        private double[] location;

        public boolean isAttention() {
            return isAttention;
        }

        public void setAttention(boolean attention) {
            isAttention = attention;
        }

        public double[] getLocation() {
            return location;
        }

        public void setLocation(double[] location) {
            this.location = location;
        }

        public String getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(String waypoints) {
            this.waypoints = waypoints;
        }

        public int getStrategy() {
            return strategy;
        }

        public void setStrategy(int strategy) {
            this.strategy = strategy;
        }

        public String getOrdersId() {
            return ordersId;
        }

        public void setOrdersId(String ordersId) {
            this.ordersId = ordersId;
        }

        public long getPassengerTravelId() {
            return passengerTravelId;
        }

        public void setPassengerTravelId(long passengerTravelId) {
            this.passengerTravelId = passengerTravelId;
        }

        public long getFinalTime() {
            return finalTime;
        }

        public void setFinalTime(long finalTime) {
            this.finalTime = finalTime;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public long getRecommendStartTime() {
            return recommendStartTime;
        }

        public void setRecommendStartTime(long recommendStartTime) {
            this.recommendStartTime = recommendStartTime;
        }

        public long getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(long lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public int getOrdersStatus() {
            return ordersStatus;
        }

        public void setOrdersStatus(int ordersStatus) {
            this.ordersStatus = ordersStatus;
        }

        public int getBookSeats() {
            return bookSeats;
        }

        public void setBookSeats(int bookSeats) {
            this.bookSeats = bookSeats;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getDriverStartAddress() {
            return driverStartAddress;
        }

        public void setDriverStartAddress(String driverStartAddress) {
            this.driverStartAddress = driverStartAddress;
        }

        public double[] getRecommendStart() {
            return recommendStart;
        }

        public void setRecommendStart(double[] recommendStart) {
            this.recommendStart = recommendStart;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public String getDriverEndAddress() {
            return driverEndAddress;
        }

        public void setDriverEndAddress(String driverEndAddress) {
            this.driverEndAddress = driverEndAddress;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public double[] getDriverStart() {
            return driverStart;
        }

        public void setDriverStart(double[] driverStart) {
            this.driverStart = driverStart;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRecommendStartAddress() {
            return recommendStartAddress;
        }

        public void setRecommendStartAddress(String recommendStartAddress) {
            this.recommendStartAddress = recommendStartAddress;
        }

        public double[] getRecommendEnd() {
            return recommendEnd;
        }

        public void setRecommendEnd(double[] recommendEnd) {
            this.recommendEnd = recommendEnd;
        }

        public long getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(long driverPhone) {
            this.driverPhone = driverPhone;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getRecommendEndAddress() {
            return recommendEndAddress;
        }

        public void setRecommendEndAddress(String recommendEndAddress) {
            this.recommendEndAddress = recommendEndAddress;
        }

        public double[] getDriverEnd() {
            return driverEnd;
        }

        public void setDriverEnd(double[] driverEnd) {
            this.driverEnd = driverEnd;
        }
    }

    public class Passenger{
        private long passengerPhone;
        private String passengerPicture;

        public long getPassengerPhone() {
            return passengerPhone;
        }

        public void setPassengerPhone(long passengerPhone) {
            this.passengerPhone = passengerPhone;
        }

        public String getPassengerPicture() {
            return passengerPicture;
        }

        public void setPassengerPicture(String passengerPicture) {
            this.passengerPicture = passengerPicture;
        }
    }
}
