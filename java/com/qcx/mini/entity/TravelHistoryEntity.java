package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/2.
 */

public class TravelHistoryEntity extends Entity {
    private List<TravelsListEntity.TravelEntity> history;
    private int status;

    public List<TravelsListEntity.TravelEntity> getHistory() {
        return history;
    }

    public void setHistory(List<TravelsListEntity.TravelEntity> history) {
        this.history = history;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
