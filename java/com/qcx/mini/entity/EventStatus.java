package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/1/26.
 */

public enum EventStatus {

    PAY_SUCCESS("支付成功",1),
    SHARE_SUCCESS("分享成功",2),

    ZMXY_AUTH_SUCCESS("芝麻信用认证成功",31),

    ME_DRIVER_INFO_CHANGED("主页信息改变",32),

    SHOW_MAIN_RED_POINT("关注页小红点",10),
    HIDE_MAIN_RED_POINT("关注页小红点",11);

    private String describe;
    private int status;

    EventStatus(String describe,int status){
        this.describe=describe;
        this.status=status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
