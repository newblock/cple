package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/5/3.
 */

public class PersonalTravelLine extends Entity {
    private PersonalLineEntity personalTravelLine;
    private int status;

    public PersonalLineEntity getPersonalTravelLine() {
        return personalTravelLine;
    }

    public void setPersonalTravelLine(PersonalLineEntity personalTravelLine) {
        this.personalTravelLine = personalTravelLine;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
