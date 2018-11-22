package com.qcx.mini.entity;

import com.qcx.mini.widget.wheel.IPickerViewData;
import com.qcx.mini.widget.wheel.IPickerViewReminderData;

/**
 * Created by Administrator on 2017/12/26.
 */

public class WheelIntEntity implements IPickerViewReminderData{
    private String itemText;
    private int data;

    public WheelIntEntity(String itemText, int data) {
        this.itemText = itemText;
        this.data = data;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    @Override
    public String getPickerViewText() {
        return itemText;
    }

    @Override
    public String getReminderText() {
        return null;
    }
}
