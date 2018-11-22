package com.qcx.mini.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/8/20.
 */

public class UnFinishedTravelsEntity extends Entity {
    @SerializedName(value = "feedUnFinalList")
    private List<DriverAndTravelEntity> unFinishedTravel;
    private int status;

    public List<DriverAndTravelEntity> getUnFinishedTravel() {
        return unFinishedTravel;
    }

    public void setUnFinishedTravel(List<DriverAndTravelEntity> unFinishedTravel) {
        this.unFinishedTravel = unFinishedTravel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
