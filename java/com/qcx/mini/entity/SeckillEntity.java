package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/2/1.
 */

public class SeckillEntity extends Entity {
    private SeckillTravel travelDetail;
    private String detail;
    private int status;

    public SeckillTravel getTravelDetail() {
        return travelDetail;
    }

    public void setTravelDetail(SeckillTravel travelDetail) {
        this.travelDetail = travelDetail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class SeckillTravel{
        private long travelId;

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }
    }
}
