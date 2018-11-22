package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/4/16.
 */

public class HaveCreateGroupAuthEntity extends Entity {
    private boolean isHaveCreateGroupAuth;
    private int status;

    public boolean isHaveCreateGroupAuth() {
        return isHaveCreateGroupAuth;
    }

    public void setHaveCreateGroupAuth(boolean haveCreateGroupAuth) {
        isHaveCreateGroupAuth = haveCreateGroupAuth;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
