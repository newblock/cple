package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class MainUnfinishTravelsEntity extends Entity {
    private List<UnFinishTravelEntity> feedUnFinalList;
    private int status;

    public List<UnFinishTravelEntity> getFeedUnFinalList() {
        return feedUnFinalList;
    }

    public void setFeedUnFinalList(List<UnFinishTravelEntity> feedUnFinalList) {
        this.feedUnFinalList = feedUnFinalList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class UnFinishTravelEntity extends Entity{
        private long travelId;
        private String ordersTravelId;
        private int type;
        private long startTime;
        private String startTimeTxt;
        private long createTime;
        private String endAddress;
        private int status;
        private int ordersNum;
        private int seats;
        private int surplusSeats;

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

        public String getOrdersTravelId() {
            return ordersTravelId;
        }

        public void setOrdersTravelId(String ordersTravelId) {
            this.ordersTravelId = ordersTravelId;
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

        public int getOrdersNum() {
            return ordersNum;
        }

        public void setOrdersNum(int ordersNum) {
            this.ordersNum = ordersNum;
        }
    }
}
