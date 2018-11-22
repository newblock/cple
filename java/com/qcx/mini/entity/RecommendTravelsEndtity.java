package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */

public class RecommendTravelsEndtity extends Entity {
    private List<DriverAndTravelEntity> recommendTravel;
    private int status;

    public List<DriverAndTravelEntity> getRecommendTravel() {
        return recommendTravel;
    }

    public void setRecommendTravel(List<DriverAndTravelEntity> recommendTravel) {
        this.recommendTravel = recommendTravel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
