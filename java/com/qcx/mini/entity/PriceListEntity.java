package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 */

public class PriceListEntity extends Entity {
    private int status;
    private List<PriceEntity> priceList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<PriceEntity> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceEntity> priceList) {
        this.priceList = priceList;
    }
}
