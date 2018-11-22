package com.qcx.mini.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/27.
 */

public class DriverAndTravelEntity extends Entity {

    @SerializedName(alternate = {"otherPicture"},value = "ownerPicture")
    private String picture;
    @SerializedName(alternate = {"otherNickName"},value = "ownerNickName")
    private String nickName;
    @SerializedName(alternate = {"otherSex"},value = "ownerSex")
    private int sex;
    @SerializedName(alternate = {"otherCar"},value = "ownerCar")
    private String car;
    @SerializedName(alternate = {"carNumber"},value = "otherCarNumber")
    private String carNumber;
    @SerializedName(alternate = {"otherPhone"},value = "ownerPhone")
    private long phone;
    @SerializedName(alternate = {"attention"},value = "attentioned")
    private boolean attention;

    private String age;
    private long lastTimeOnline;
    private TravelEntity travelVo;

    private String myPicture;
    private String myNickname;

    private boolean isHideAttentionView;
    private boolean isHideDingzuo;

    private String byWay;
    private String startRecommend;
    private String endRecommend;

    public String getMyPicture() {
        return myPicture;
    }

    public void setMyPicture(String myPicture) {
        this.myPicture = myPicture;
    }

    public String getMyNickname() {
        return myNickname;
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        car = car;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public boolean isAttention() {
        return attention;
    }

    public void setAttention(boolean attention) {
        this.attention = attention;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public long getLastTimeOnline() {
        return lastTimeOnline;
    }

    public void setLastTimeOnline(long lastTimeOnline) {
        this.lastTimeOnline = lastTimeOnline;
    }

    public TravelEntity getTravelVo() {
        return travelVo;
    }

    public void setTravelVo(TravelEntity travelVo) {
        this.travelVo = travelVo;
    }

    public boolean isHideAttentionView() {
        return isHideAttentionView;
    }

    public void setHideAttentionView(boolean hideAttentionView) {
        isHideAttentionView = hideAttentionView;
    }

    public boolean isHideDingzuo() {
        return isHideDingzuo;
    }

    public void setHideDingzuo(boolean hideDingzuo) {
        isHideDingzuo = hideDingzuo;
    }

    public String getByWay() {
        return byWay;
    }

    public void setByWay(String byWay) {
        this.byWay = byWay;
    }

    public String getStartRecommend() {
        return startRecommend;
    }

    public void setStartRecommend(String startRecommend) {
        this.startRecommend = startRecommend;
    }

    public String getEndRecommend() {
        return endRecommend;
    }

    public void setEndRecommend(String endRecommend) {
        this.endRecommend = endRecommend;
    }
}
