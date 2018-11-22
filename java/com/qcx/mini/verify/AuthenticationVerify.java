package com.qcx.mini.verify;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/1/30.
 */

public class AuthenticationVerify {
    public static void verifyImg(boolean[] pictures) throws VerifyException {
        if(pictures==null){
            throw new VerifyException("请上传照片信息");
        }
        for(int i=0;i<pictures.length;i++){
            if(!pictures[i]){
                throw new VerifyException("请上传照片信息");
            }
        }
    }

    public static void verifyCarType(String cardType) throws VerifyException {
        if (TextUtils.isEmpty(cardType)) {
            throw new VerifyException("车型不能为空");
        }
    }

    public static void verifyCarColor(String color) throws VerifyException {
        if (TextUtils.isEmpty(color)) {
            throw new VerifyException("颜色不能为空");
        }
    }

    public static void verifyCarNum(String num) throws VerifyException {
        if (TextUtils.isEmpty(num)) {
            throw new VerifyException("请输入车牌号");
        }
        if (!num.matches("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4,5}[A-Z0-9挂学警港澳]{1}$")) {
            throw new VerifyException("请输入正确的车牌号");
        }
    }
}
