package com.qcx.mini.entity;

import android.annotation.SuppressLint;

import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.widget.wheel.IPickerViewReminderData;

/**
 * Created by Administrator on 2018/1/3.
 */

public class PriceEntity extends Entity implements IPickerViewReminderData {
    public static String itemTextInfo="元";
    private boolean isHintRemiderText=false;

    private double price;
    private double redPacketPrice;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRedPacketPrice() {
        return redPacketPrice;
    }

    public void setRedPacketPrice(double redPacketPrice) {
        this.redPacketPrice = redPacketPrice;
    }

    public void setHintRemiderText(boolean hintRemiderText) {
        isHintRemiderText = hintRemiderText;
    }

    @Override
    public String getPickerViewText() {
        return (int)price+itemTextInfo;
    }

    @Override
    public String getReminderText() {
        if(isHintRemiderText){
            return "";
        }else {
            return "(含"+ CommonUtil.formatPrice(getRedPacketPrice(),2)+"元雷锋红包)";
        }
    }
}
