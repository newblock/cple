package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class MainRecommendTravelsEntity extends Entity {
    private List<TravelsListEntity> feedList;
    private int recommendStartIndex;
    private int status;

    public List<TravelsListEntity> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<TravelsListEntity> feedList) {
        this.feedList = feedList;
    }

    public int getRecommendStartIndex() {
        return recommendStartIndex;
    }

    public void setRecommendStartIndex(int recommendStartIndex) {
        this.recommendStartIndex = recommendStartIndex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class TravelsListEntity extends Entity {
        private String ownerPicture;
        private String ownerNickName;
        private int ownerSex;
        private String ownerCar;
        private long lastTimeOnline;
        private long ownerPhone;
        private List<MainRecommendItemTravelEntity> travelVoList;
        private int pageNum;//当前显示的行程页
        private boolean attentioned;

        public long getOwnerPhone() {
            return ownerPhone;
        }

        public void setOwnerPhone(long ownerPhone) {
            this.ownerPhone = ownerPhone;
        }

        public boolean isAttentioned() {
            return attentioned;
        }

        public void setAttentioned(boolean attentioned) {
            this.attentioned = attentioned;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
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

        public long getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(long lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public List<MainRecommendItemTravelEntity> getTravelVoList() {
            return travelVoList;
        }

        public void setTravelVoList(List<MainRecommendItemTravelEntity> travelVoList) {
            this.travelVoList = travelVoList;
        }
    }

    public static class MainRecommendItemTravelEntity extends Entity{
        private long travelId;
        private int type;
        private long startTime;
        private String startTimeTxt;
        private long createTime;
        private String startAddress;
        private String endAddress;
        private int seats;
        private int surplusSeats;
        private List<String> headPictures;
        private double travelPrice;
        private int pageViews;
        private int likesNum;
        private int commentsNum;
        private int sharesNum;
        private double redPacketPrice;
        private int travelType;
        private boolean liked;
        private long travelPhone;
        private double[] start;
        private double[] end;
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public long getTravelPhone() {
            return travelPhone;
        }

        public void setTravelPhone(long travelPhone) {
            this.travelPhone = travelPhone;
        }

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
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

        public double getRedPacketPrice() {
            return redPacketPrice;
        }

        public void setRedPacketPrice(double redPacketPrice) {
            this.redPacketPrice = redPacketPrice;
        }

        public int getTravelType() {
            return travelType;
        }

        public void setTravelType(int travelType) {
            this.travelType = travelType;
        }
    }
}
