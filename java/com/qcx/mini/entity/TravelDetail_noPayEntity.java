package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class TravelDetail_noPayEntity extends Entity{
    private TravelDetail eachFeedTravelDetail;
    private int status;

    public TravelDetail getEachFeedTravelDetail() {
        return eachFeedTravelDetail;
    }

    public void setEachFeedTravelDetail(TravelDetail eachFeedTravelDetail) {
        this.eachFeedTravelDetail = eachFeedTravelDetail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class TravelDetail{
        private long travelId;
        private int type;
        private long startTime;
        private String startTimeTxt;
        private String startAddress;
        private String endAddress;
        private double[] start;
        private double[] end;
        private double[] homeLocation;
        private double[] companyLocation;
        private int strategy;
        private String waypoints;

        private int recommendStartType;//0, "家是起点" 1, "公司是起点"

        private double[] recommendStart;
        private String recommendStartAddress;
        private int recommendStartDuration;
        private double recommendStartDistance;
        private long recommendTime;//车主行程的推荐上车时间，乘客没有

        private double[] recommendLocationStart;
        private String recommendLocationStartAddress;
        private long recommendLocationDuration;
        private double recommendLocationDistance;
        private long recommendLocationTime;

        private double[] recommendEnd;
        private String recommendEndAddress;
        private int recommendEndDuration;
        private double recommendEndDistance;

        private int seats;
        private int surplusSeats;
        private List<String> headPictures;
        private double travelPrice;
        private double redPacketPrice;
        private int travelStatus;
        private int pageViews;
        private int likesNum;
        private int commentsNum;
        private int sharesNum;
        private boolean liked;
        private String ownerPicture;
        private String ownerNickName;
        private int ownerSex;
        private String ownerCar;
        private long ownerPhone;
        private long lastTimeOnline;
        private boolean attentioned;
        private String homeAddr;
        private String companyAddr;

        public String getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(String waypoints) {
            this.waypoints = waypoints;
        }

        public String getHomeAddr() {
            return homeAddr;
        }

        public void setHomeAddr(String homeAddr) {
            this.homeAddr = homeAddr;
        }

        public String getCompanyAddr() {
            return companyAddr;
        }

        public void setCompanyAddr(String companyAddr) {
            this.companyAddr = companyAddr;
        }

        public int getRecommendStartType() {
            return recommendStartType;
        }

        public void setRecommendStartType(int recommendStartType) {
            this.recommendStartType = recommendStartType;
        }

        public int getRecommendStartDuration() {
            return recommendStartDuration;
        }

        public void setRecommendStartDuration(int recommendStartDuration) {
            this.recommendStartDuration = recommendStartDuration;
        }

        public double getRecommendStartDistance() {
            return recommendStartDistance;
        }

        public void setRecommendStartDistance(double recommendStartDistance) {
            this.recommendStartDistance = recommendStartDistance;
        }

        public double[] getRecommendLocationStart() {
            return recommendLocationStart;
        }

        public void setRecommendLocationStart(double[] recommendLocationStart) {
            this.recommendLocationStart = recommendLocationStart;
        }

        public String getRecommendLocationStartAddress() {
            return recommendLocationStartAddress;
        }

        public void setRecommendLocationStartAddress(String recommendLocationStartAddress) {
            this.recommendLocationStartAddress = recommendLocationStartAddress;
        }

        public long getRecommendLocationDuration() {
            return recommendLocationDuration;
        }

        public void setRecommendLocationDuration(long recommendLocationDuration) {
            this.recommendLocationDuration = recommendLocationDuration;
        }

        public double getRecommendLocationDistance() {
            return recommendLocationDistance;
        }

        public void setRecommendLocationDistance(double recommendLocationDistance) {
            this.recommendLocationDistance = recommendLocationDistance;
        }

        public long getRecommendLocationTime() {
            return recommendLocationTime;
        }

        public void setRecommendLocationTime(long recommendLocationTime) {
            this.recommendLocationTime = recommendLocationTime;
        }

        public int getRecommendEndDuration() {
            return recommendEndDuration;
        }

        public void setRecommendEndDuration(int recommendEndDuration) {
            this.recommendEndDuration = recommendEndDuration;
        }

        public double getRecommendEndDistance() {
            return recommendEndDistance;
        }

        public void setRecommendEndDistance(double recommendEndDistance) {
            this.recommendEndDistance = recommendEndDistance;
        }

        public double[] getHomeLocation() {
            return homeLocation;
        }

        public void setHomeLocation(double[] homeLocation) {
            this.homeLocation = homeLocation;
        }

        public double[] getCompanyLocation() {
            return companyLocation;
        }

        public void setCompanyLocation(double[] companyLocation) {
            this.companyLocation = companyLocation;
        }

        public int getStrategy() {
            return strategy;
        }

        public void setStrategy(int strategy) {
            this.strategy = strategy;
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

        public double[] getRecommendStart() {
            return recommendStart;
        }

        public void setRecommendStart(double[] recommendStart) {
            this.recommendStart = recommendStart;
        }

        public double[] getRecommendEnd() {
            return recommendEnd;
        }

        public void setRecommendEnd(double[] recommendEnd) {
            this.recommendEnd = recommendEnd;
        }

        public String getRecommendStartAddress() {
            return recommendStartAddress;
        }

        public void setRecommendStartAddress(String recommendStartAddress) {
            this.recommendStartAddress = recommendStartAddress;
        }

        public String getRecommendEndAddress() {
            return recommendEndAddress;
        }

        public void setRecommendEndAddress(String recommendEndAddress) {
            this.recommendEndAddress = recommendEndAddress;
        }

        public long getRecommendTime() {
            return recommendTime;
        }

        public void setRecommendTime(long recommendTime) {
            this.recommendTime = recommendTime;
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

        public double getRedPacketPrice() {
            return redPacketPrice;
        }

        public void setRedPacketPrice(double redPacketPrice) {
            this.redPacketPrice = redPacketPrice;
        }

        public int getTravelStatus() {
            return travelStatus;
        }

        public void setTravelStatus(int travelStatus) {
            this.travelStatus = travelStatus;
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

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public String getOwnerPicture() {
            return ownerPicture;
        }

        public void setOwnerPicture(String ownerPicture) {
            this.ownerPicture = ownerPicture;
        }

        public String getOwnerNickName() {
            return ownerNickName;
        }

        public void setOwnerNickName(String ownerNickName) {
            this.ownerNickName = ownerNickName;
        }

        public int getOwnerSex() {
            return ownerSex;
        }

        public void setOwnerSex(int ownerSex) {
            this.ownerSex = ownerSex;
        }

        public String getOwnerCar() {
            return ownerCar;
        }

        public void setOwnerCar(String ownerCar) {
            this.ownerCar = ownerCar;
        }

        public long getOwnerPhone() {
            return ownerPhone;
        }

        public void setOwnerPhone(long ownerPhone) {
            this.ownerPhone = ownerPhone;
        }

        public long getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(long lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public boolean isAttentioned() {
            return attentioned;
        }

        public void setAttentioned(boolean attentioned) {
            this.attentioned = attentioned;
        }
    }
}
