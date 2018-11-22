package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ReleaseLinesEntity extends Entity {
    private List<ReleaseLineInfoEntity> listManualPersonalTL;
    private int status;

    public List<ReleaseLineInfoEntity> getListManualPersonalTL() {
        return listManualPersonalTL;
    }

    public void setListManualPersonalTL(List<ReleaseLineInfoEntity> listManualPersonalTL) {
        this.listManualPersonalTL = listManualPersonalTL;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
