package com.qcx.mini.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class PersonalLineListEntity extends Entity{
    @SerializedName(alternate = {"personalTravelLineListByRole"},value = "personalTravelLineList")
    private List<PersonalLineEntity> personalTravelLineList;
    private int status;

    public List<PersonalLineEntity> getPersonalTravelLineList() {
        return personalTravelLineList;
    }

    public void setPersonalTravelLineList(List<PersonalLineEntity> personalTravelLineList) {
        this.personalTravelLineList = personalTravelLineList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
