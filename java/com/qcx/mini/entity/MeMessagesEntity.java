package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class MeMessagesEntity extends Entity {
    private List<MeMessageEntity> news;

    public List<MeMessageEntity> getNews() {
        return news;
    }

    public void setNews(List<MeMessageEntity> news) {
        this.news = news;
    }

    public static class MeMessageEntity{
        private String creatTime;
        private String nickName;
        private String comment;
        private int type;
        private TravelEntity driverTravel;
        private String picture;

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public TravelEntity getDriverTravel() {
            return driverTravel;
        }

        public void setDriverTravel(TravelEntity driverTravel) {
            this.driverTravel = driverTravel;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }


    public static class  TravelEntity{
        private long travelId;
        private long driverPhone;
        private int seats;
        private int surplusSeats;
        private long startTime;
        private double[] start;
        private String startAddress;
        private double[] end;
        private String endAddress;
        private int status;
        private int travelType;
        private double travelPrice;
        private double price;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getTravelPrice() {
            return travelPrice;
        }

        public void setTravelPrice(double travelPrice) {
            this.travelPrice = travelPrice;
        }

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public long getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(long driverPhone) {
            this.driverPhone = driverPhone;
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

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
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
    }
}
