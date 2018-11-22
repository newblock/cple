package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/2/2.
 */

public class LateEntity extends Entity{
    private int status;
    private boolean lateStatus;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isLateStatus() {
        return lateStatus;
    }

    public void setLateStatus(boolean lateStatus) {
        this.lateStatus = lateStatus;
    }
}
