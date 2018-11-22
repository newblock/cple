package com.qcx.mini.entity;

/**
 * Created by Administrator on 2017/11/27.
 */

public class UserEntity extends Entity {
    private String rongToken;
    private String token;
    private int status;

    public String getRongToken() {
        return rongToken;
    }

    public String getToken() {
        return token;
    }

    public int getStatus() {
        return status;
    }

    public void setRongToken(String rongToken) {
        this.rongToken = rongToken;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString(){
        return "rongToken="+rongToken+" \ntoken="+token+"\nstatus="+status;
    }
}
