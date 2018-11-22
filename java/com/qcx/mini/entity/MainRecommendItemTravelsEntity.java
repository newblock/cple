package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/1/9.
 */

public class MainRecommendItemTravelsEntity extends Entity {
    private MainRecommendTravelsEntity.TravelsListEntity eachFeedList;
    private int status;

    public MainRecommendTravelsEntity.TravelsListEntity getEachFeedList() {
        return eachFeedList;
    }

    public void setEachFeedList(MainRecommendTravelsEntity.TravelsListEntity eachFeedList) {
        this.eachFeedList = eachFeedList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
