package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 * 常用线路匹配的行程
 */

public class TravelLineDetailEntity extends Entity{
    private TravelLineDetail travelLineDetail;
    private int status;

    public TravelLineDetail getTravelLineDetail() {
        return travelLineDetail;
    }

    public void setTravelLineDetail(TravelLineDetail travelLineDetail) {
        this.travelLineDetail = travelLineDetail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class TravelLineDetail extends Entity{
        private PersonalLineEntity personalTravelLine;
        private List<DriverAndTravelEntity> travelOneDetailVos;
        private boolean publish;

        public boolean isPublish() {
            return publish;
        }

        public void setPublish(boolean publish) {
            this.publish = publish;
        }

        public PersonalLineEntity getPersonalTravelLine() {
            return personalTravelLine;
        }

        public void setPersonalTravelLine(PersonalLineEntity personalTravelLine) {
            this.personalTravelLine = personalTravelLine;
        }

        public List<DriverAndTravelEntity> getTravelOneDetailVos() {
            return travelOneDetailVos;
        }

        public void setTravelOneDetailVos(List<DriverAndTravelEntity> travelOneDetailVos) {
            this.travelOneDetailVos = travelOneDetailVos;
        }
    }

}
