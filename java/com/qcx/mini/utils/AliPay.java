package com.qcx.mini.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.lang.ref.WeakReference;
import java.util.Map;


/**
 * Created by Smile on 17/5/31.
 */

public class AliPay {

    private OnAliPayListener mListener;
    private WeakReference<Activity> mActivity;

    public AliPay(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    public void doPay(final String obj) {
        final Activity activity = mActivity.get();
        if (activity == null) return;
        final PayTask alipay = new PayTask(activity);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> result = alipay.payV2(obj, true);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签

                        for (Map.Entry<String, String> entry : result.entrySet()) {
                            //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
                            //entry.getKey() ;entry.getValue(); entry.setValue();
                            //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
                            System.out.println("key= " + entry.getKey() + " and value= "
                                    + entry.getValue());
                        }

                        String resultStatus = result.get("resultStatus");
                        LogUtil.i(resultStatus);
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            if (mListener != null) mListener.onSuccess();
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，.
                            // 最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                if (mListener != null) mListener.onWait();
                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                if (mListener != null) mListener.onCancel();
                            }
                        }
                    }
                });
            }
        }).start();



    }


    public AliPay setOnAliPayListener(OnAliPayListener l) {
        mListener = l;
        return this;
    }

    public abstract static class OnAliPayListener {
        /**
         * 支付成功
         */
        public void onSuccess() {
        }

        /**
         * 支付取消
         */
        public void onCancel() {
        }

        /**
         * 等待确认
         */
        public void onWait() {
        }
    }

}
