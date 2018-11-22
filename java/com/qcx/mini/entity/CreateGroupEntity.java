package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/4/12.
 */

public class CreateGroupEntity extends Entity {
    private long groupId;
    private int status;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
