package com.qcx.mini.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.qcx.mini.share.ShareTravelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class UnOrderDriverTravelDetailEntity extends Entity {
    private UnOrderTravelDetail travelDetail;
    private int status;

    public UnOrderTravelDetail getTravelDetail() {
        return travelDetail;
    }

    public void setTravelDetail(UnOrderTravelDetail travelDetail) {
        this.travelDetail = travelDetail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class UnOrderTravelDetail {
        private String picture;
        private String nickName;
        private String car;
        private String lastTimeOnlineTxt;
        private int sex;
        private long phone;
        private String startTimeTxt;
        private double[] start;
        private String startAddress;
        private double[] end;
        private String endAddress;
        private double travelPrice;
        private double redPacketPrice;
        private int seats;
        private int surplusSeats;
        private int travelStatus;
        private int strategy;
        private double[] matchStart;
        private String matchStartAddress;
        private double[] matchEnd;
        private String matchEndAddress;
        private double[] recommendStartLocation;
        private String recommendStartAddress;
        private double[] recommendEndLocation;
        private String recommendEndAddress;
        private double extraDistance;
        private double extraMoney;
        private boolean recommendStatus;
        private String waypoints;
        private long startTime;
        private List<PassengerEntity> passengers;
        private long recommendStartTime;
        private long recommendEndTime;
        private long travelId;
        private long pickUpStartTime;
        private ArrayList<TravelStation> travelList;

        public ArrayList<TravelStation> getTravelList() {
            return travelList;
        }

        public void setTravelList(ArrayList<TravelStation> travelList) {
            this.travelList = travelList;
        }

        public long getPickUpStartTime() {
            return pickUpStartTime;
        }

        public void setPickUpStartTime(long pickUpStartTime) {
            this.pickUpStartTime = pickUpStartTime;
        }

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public long getRecommendStartTime() {
            return recommendStartTime;
        }

        public void setRecommendStartTime(long recommendStartTime) {
            this.recommendStartTime = recommendStartTime;
        }

        public long getRecommendEndTime() {
            return recommendEndTime;
        }

        public void setRecommendEndTime(long recommendEndTime) {
            this.recommendEndTime = recommendEndTime;
        }

        public List<PassengerEntity> getPassengers() {
            return passengers;
        }

        public void setPassengers(List<PassengerEntity> passengers) {
            this.passengers = passengers;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(String waypoints) {
            this.waypoints = waypoints;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getLastTimeOnlineTxt() {
            return lastTimeOnlineTxt;
        }

        public void setLastTimeOnlineTxt(String lastTimeOnlineTxt) {
            this.lastTimeOnlineTxt = lastTimeOnlineTxt;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }

        public String getStartTimeTxt() {
            return startTimeTxt;
        }

        public void setStartTimeTxt(String startTimeTxt) {
            this.startTimeTxt = startTimeTxt;
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

        public double getTravelPrice() {
            return travelPrice;
        }

        public void setTravelPrice(double travelPrice) {
            this.travelPrice = travelPrice;
        }

        public double getRedPacketPrice() {
            return redPacketPrice;
        }

        public void setRedPacketPrice(double redPacketPrice) {
            this.redPacketPrice = redPacketPrice;
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

        public int getTravelStatus() {
            return travelStatus;
        }

        public void setTravelStatus(int travelStatus) {
            this.travelStatus = travelStatus;
        }

        public int getStrategy() {
            return strategy;
        }

        public void setStrategy(int strategy) {
            this.strategy = strategy;
        }

        public double[] getMatchStart() {
            return matchStart;
        }

        public void setMatchStart(double[] matchStart) {
            this.matchStart = matchStart;
        }

        public String getMatchStartAddress() {
            return matchStartAddress;
        }

        public void setMatchStartAddress(String matchStartAddress) {
            this.matchStartAddress = matchStartAddress;
        }

        public double[] getMatchEnd() {
            return matchEnd;
        }

        public void setMatchEnd(double[] matchEnd) {
            this.matchEnd = matchEnd;
        }

        public String getMatchEndAddress() {
            return matchEndAddress;
        }

        public void setMatchEndAddress(String matchEndAddress) {
            this.matchEndAddress = matchEndAddress;
        }

        public double[] getRecommendStartLocation() {
            return recommendStartLocation;
        }

        public void setRecommendStartLocation(double[] recommendStartLocation) {
            this.recommendStartLocation = recommendStartLocation;
        }

        public String getRecommendStartAddress() {
            return recommendStartAddress;
        }

        public void setRecommendStartAddress(String recommendStartAddress) {
            this.recommendStartAddress = recommendStartAddress;
        }

        public double[] getRecommendEndLocation() {
            return recommendEndLocation;
        }

        public void setRecommendEndLocation(double[] recommendEndLocation) {
            this.recommendEndLocation = recommendEndLocation;
        }

        public String getRecommendEndAddress() {
            return recommendEndAddress;
        }

        public void setRecommendEndAddress(String recommendEndAddress) {
            this.recommendEndAddress = recommendEndAddress;
        }

        public double getExtraDistance() {
            return extraDistance;
        }

        public void setExtraDistance(double extraDistance) {
            this.extraDistance = extraDistance;
        }

        public double getExtraMoney() {
            return extraMoney;
        }

        public void setExtraMoney(double extraMoney) {
            this.extraMoney = extraMoney;
        }

        public boolean isRecommendStatus() {
            return recommendStatus;
        }

        public void setRecommendStatus(boolean recommendStatus) {
            this.recommendStatus = recommendStatus;
        }
    }

    public static class TravelStation implements Parcelable {
        /**
         * "travelId": 61910174085078,
         * "driverPhone": 15130096570,
         * "beginTime": 1529375400000,
         * "location": [116.32027435302734, 40.05790710449219],
         * "address": "西二旗大街",
         * "index": 0
         */

        private long travelId;
        private long driverPhone;
        private long beginTime;
        private double[] location;
        private String address;
        private int index;

        public TravelStation() {
        }

        public TravelStation(Parcel source) {
            travelId = source.readLong();
            driverPhone = source.readLong();
            beginTime = source.readLong();
            int locationLength = source.readInt();
            if (locationLength > 0) {
                location = new double[locationLength];
                for (int i = 0; i < locationLength; i++) {
                    location[i] = source.readDouble();
                }
            }
            address = source.readString();
            index = source.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(travelId);
            dest.writeLong(driverPhone);
            dest.writeLong(beginTime);
            dest.writeInt(location == null ? 0 : location.length);
            if(location!=null){
                for(int i=0;i<location.length;i++){
                    dest.writeDouble(location[i]);
                }
            }
            dest.writeString(address);
            dest.writeInt(index);
        }

        public static final Parcelable.Creator<TravelStation> CREATOR = new Parcelable.Creator<TravelStation>() {

            @Override
            public TravelStation createFromParcel(Parcel source) {
                return new TravelStation(source);
            }

            @Override
            public TravelStation[] newArray(int size) {
                return new TravelStation[size];
            }
        };


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

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public double[] getLocation() {
            return location;
        }

        public void setLocation(double[] location) {
            this.location = location;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
