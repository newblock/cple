package com.qcx.mini.entity;

import com.qcx.mini.widget.wheel.IPickerViewData;

/**
 * Created by Administrator on 2017/12/26.
 */

public class WheelTimeEntity implements IPickerViewData {
    private String itemText;
    private long l;

    public WheelTimeEntity(String itemText, long l) {
        this.itemText = itemText;
        this.l = l;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public void setL(long l) {
        this.l = l;
    }

    public long getL() {
        return l;
    }

    @Override
    public String getPickerViewText() {
        return itemText;
    }
}
