package com.qcx.mini.entity;

import com.qcx.mini.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/20.
 */

public class TravelDetail_DriverEntity extends Entity {
    private int status;
    private List<Passenger> passengers;
    private SeckillTravelNopay seckillTravelNopay;
    private Travel travel;
    private Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public SeckillTravelNopay getSeckillTravelNopay() {
        return seckillTravelNopay;
    }

    public void setSeckillTravelNopay(SeckillTravelNopay seckillTravelNopay) {
        this.seckillTravelNopay = seckillTravelNopay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public class Passenger{
        private long lastTimeOnline;
        private int distanceSort;
        private double ticketPrice;
        private int bookSeats;
        private String nickName;
        private String startAddress;
        private int sex;
        private String ordersTravelId;
        private double[] start;
        private int orderStatus;
        private String picture;
        private String orderStatusDetail;
        private long passengerPhone;
        private long getOnTime;
        private double[] end;
        private String endAddress;
        private double[] location;
        private boolean isAttention;
        private long startTime;
        private boolean isPickUp;

        public boolean isPickUp() {
            return isPickUp;
        }

        public Passenger setPickUp(boolean pickUp) {
            isPickUp = pickUp;
            return this;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

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

        public long getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(long lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public int getDistanceSort() {
            return distanceSort;
        }

        public void setDistanceSort(int distanceSort) {
            this.distanceSort = distanceSort;
        }

        public double getTicketPrice() {
            return ticketPrice;
        }

        public void setTicketPrice(double ticketPrice) {
            this.ticketPrice = ticketPrice;
        }

        public int getBookSeats() {
            return bookSeats;
        }

        public void setBookSeats(int bookSeats) {
            this.bookSeats = bookSeats;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getOrderStatusDetail() {
            return orderStatusDetail;
        }

        public void setOrderStatusDetail(String orderStatusDetail) {
            this.orderStatusDetail = orderStatusDetail;
        }

        public long getPassengerPhone() {
            return passengerPhone;
        }

        public void setPassengerPhone(long passengerPhone) {
            this.passengerPhone = passengerPhone;
        }

        public long getGetOnTime() {
            return getOnTime;
        }

        public void setGetOnTime(long getOnTime) {
            this.getOnTime = getOnTime;
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
    }

    public class Travel{
        private boolean haveOrder;
        private double driverIncome;
        private String startAddress;
        private double[] start;
        private String startTimeTxt;
        private int surplusSeats;
        private int seats;
        private int travelStatus;
        private long travelId;
        private long startTime;
        private long finalTime;
        private double[] end;
        private int strategy;
        private String endAddress;
        private int createType;
        private String waypoints;
        private double travelPrice;

        public double getTravelPrice() {
            return travelPrice;
        }

        public void setTravelPrice(double travelPrice) {
            this.travelPrice = travelPrice;
        }

        public String getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(String waypoints) {
            this.waypoints = waypoints;
        }

        public int getCreateType() {
            return createType;
        }

        public void setCreateType(int createType) {
            this.createType = createType;
        }

        public long getFinalTime() {
            return finalTime;
        }

        public void setFinalTime(long finalTime) {
            this.finalTime = finalTime;
        }

        public String getEndAddress() {
            return endAddress;
        }

        public void setEndAddress(String endAddress) {
            this.endAddress = endAddress;
        }

        public boolean isHaveOrder() {
            return haveOrder;
        }

        public void setHaveOrder(boolean haveOrder) {
            this.haveOrder = haveOrder;
        }

        public double getDriverIncome() {
            return driverIncome;
        }

        public void setDriverIncome(double driverIncome) {
            this.driverIncome = driverIncome;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public double[] getStart() {
            return start;
        }

        public void setStart(double[] start) {
            this.start = start;
        }

        public String getStartTimeTxt() {
            return startTimeTxt;
        }

        public void setStartTimeTxt(String startTimeTxt) {
            this.startTimeTxt = startTimeTxt;
        }

        public int getSurplusSeats() {
            return surplusSeats;
        }

        public void setSurplusSeats(int surplusSeats) {
            this.surplusSeats = surplusSeats;
        }

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public int getTravelStatus() {
            return travelStatus;
        }

        public void setTravelStatus(int travelStatus) {
            this.travelStatus = travelStatus;
        }

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public double[] getEnd() {
            return end;
        }

        public void setEnd(double[] end) {
            this.end = end;
        }

        public int getStrategy() {
            return strategy;
        }

        public void setStrategy(int strategy) {
            this.strategy = strategy;
        }
    }

    public class SeckillTravelNopay{
        /**
         "lastTimeOnline": 1516438848328,
         "nickName": "回家",
         "startAddress": "中关村创业大厦",
         "sex": 2,
         "start": [116.311268, 40.037357],
         "passengerTravelStatus": 1,
         "startTimeTxt": "2018-01-20 21:50:00",
         "seats": 1,
         "picture": "https://t1.driver.quchuxing.com.cn/resources/pictures/1514892200562.jpg",
         "travelId": 12017440371879,
         "startTime": 1516456200000,
         "end": [116.29423, 40.09479],
         "endAddress": "生命科学园(地铁站)"
         */

        private long lastTimeOnline;
        private String nickName;
        private String startAddress;
        private int sex;
        private double[] start;
        private int passengerTravelStatus;
        private String startTimeTxt;
        private int seats;
        private String picture;
        private long travelId;
        private long startTime;
        private double[] end;
        private String endAddress;
        private String waypoints;
        private double price;
        private long passengerPhone;
        private boolean isAttention;

        public boolean isAttention() {
            return isAttention;
        }

        public void setAttention(boolean attention) {
            isAttention = attention;
        }

        public long getPassengerPhone() {
            return passengerPhone;
        }

        public void setPassengerPhone(long passengerPhone) {
            this.passengerPhone = passengerPhone;
        }

        public String getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(String waypoints) {
            this.waypoints = waypoints;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public long getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(long lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public double[] getStart() {
            return start;
        }

        public void setStart(double[] start) {
            this.start = start;
        }

        public int getPassengerTravelStatus() {
            return passengerTravelStatus;
        }

        public void setPassengerTravelStatus(int passengerTravelStatus) {
            this.passengerTravelStatus = passengerTravelStatus;
        }

        public String getStartTimeTxt() {
            return startTimeTxt;
        }

        public void setStartTimeTxt(String startTimeTxt) {
            this.startTimeTxt = startTimeTxt;
        }

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
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
    }

    public class Driver{
        private String carNumber;
        private long lastTimeOnline;
        private long phone;
        private String car;
        private String nickName;
        private int sex;
        private String picture;
        private String age;

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public long getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(long lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
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

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

}
