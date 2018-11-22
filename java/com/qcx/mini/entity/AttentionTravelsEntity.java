package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class AttentionTravelsEntity extends Entity{
    private List<DriverAndTravelEntity> travels;
    private int status;

    public List<DriverAndTravelEntity> getTravels() {
        return travels;
    }

    public void setTravels(List<DriverAndTravelEntity> travels) {
        this.travels = travels;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
