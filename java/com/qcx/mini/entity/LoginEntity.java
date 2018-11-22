package com.qcx.mini.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/13.
 */

public class LoginEntity extends Entity {
    private String rongToken;
    private int  status;
    private String token;
    private boolean authenStatus;
    private boolean passStatus;
    private boolean zmxyStatus;
    private long phone;
    private String inviteCode;

    @SerializedName(value = "carOwnerStatus")
    private int driverStatus;

    public String getInviteCode() {
        return inviteCode;
    }

    public LoginEntity setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        return this;
    }

    public String getRongToken() {
        return rongToken;
    }

    public void setRongToken(String rongToken) {
        this.rongToken = rongToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAuthenStatus() {
        return authenStatus;
    }

    public void setAuthenStatus(boolean authenStatus) {
        this.authenStatus = authenStatus;
    }

    public boolean isPassStatus() {
        return passStatus;
    }

    public void setPassStatus(boolean passStatus) {
        this.passStatus = passStatus;
    }

    public boolean isZmxyStatus() {
        return zmxyStatus;
    }

    public void setZmxyStatus(boolean zmxyStatus) {
        this.zmxyStatus = zmxyStatus;
    }

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
