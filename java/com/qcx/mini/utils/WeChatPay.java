package com.qcx.mini.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.qcx.mini.ConstantString;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * Created by Smile on 17/5/31.
 */

public class WeChatPay {
    public WeChatPay(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;


    /**
     * 调起支付
     *
     * @param partnerId
     * @param prepayId
     * @param nonceStr
     * @param timeStamp
     * @param sign
     */
    public void pay(String partnerId, String prepayId,
                    String nonceStr, String timeStamp, String sign) {
        if (!isWeixinAvilible()) {
//            Toast.makeText(mContext, "微信都没装我看你怎么支付 :)", Toast.LENGTH_SHORT).show();
        }
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, ConstantString.WXAPP_ID);
        api.registerApp( ConstantString.WXAPP_ID);

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(mContext, "请更新微信客户端 ):", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println("partnerId = [" + partnerId + "], prepayId = [" + prepayId + "], nonceStr = [" + nonceStr + "], timeStamp = [" + timeStamp + "], sign = [" + sign + "]");
        PayReq request = new PayReq();
        request.appId =  ConstantString.WXAPP_ID;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        System.out.println(request.checkArgs());
        api.sendReq(request);
    }


    /**
     * 判断微信是否安装
     *
     * @return
     */
    public boolean isWeixinAvilible() {
        final PackageManager packageManager = mContext.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
