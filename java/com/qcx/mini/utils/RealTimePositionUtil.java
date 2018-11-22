package com.qcx.mini.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.qcx.mini.MainClass;
import com.qcx.mini.User;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;

import java.util.HashMap;
import java.util.Map;

import static com.qcx.mini.net.URLString.updateLocation;

/**
 * Created by zsp on 2017/6/27.
 */

public class RealTimePositionUtil implements AMapLocationListener {
    private int interval=10*1000;
    private static RealTimePositionUtil realTimePositionUtil;
    private Context context;
    private long travelStartTime;
    private boolean isLocation=false;//已开启定位后不能再开启
    private boolean isUploadingLocation=false;
    private AMapLocation aMapLocation;
    private AMapLocationListener listener;

    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient;

    private RealTimePositionUtil(){
        context= MainClass.getInstance().getApplicationContext();
        mlocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    }

    public void setlistener(AMapLocationListener listener){
        this.listener=listener;
    }

    public void removeListener(AMapLocationListener listener){
        if(this.listener==listener){
            this.listener=null;
        }
    }

    private void loaction(){
        mLocationOption.setInterval(interval);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
        isLocation=true;
    }

    public RealTimePositionUtil stopLocation(){
        synchronized (this){
            if(mlocationClient!=null) {
                isLocation=false;
                isUploadingLocation=false;
                mlocationClient.stopLocation();
            }
        }
        return this;
    }

    public void destroyLocation(){
        synchronized (this){
            if(mlocationClient!=null) {
                mlocationClient.stopLocation();
            }
            realTimePositionUtil =null;
        }
    }

    public static RealTimePositionUtil getInstance () {
        if(realTimePositionUtil ==null){
            realTimePositionUtil =new RealTimePositionUtil();
        }
        return realTimePositionUtil;
    }

    //开启定位
    public RealTimePositionUtil startLocation(){
        if(!isLocation){
            loaction();
        }
        return this;
    }

    public void startUploadingLocation(){
        isUploadingLocation=true;
    }

    public void stopUploadingLocation(){
        isUploadingLocation=false;
    }

    public void setInterval(int interval){
        this.interval=interval;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                this.aMapLocation=amapLocation;
                if(listener!=null){
                    listener.onLocationChanged(amapLocation);
                }
                if(isUploadingLocation){
                    updateLocation(new double[]{amapLocation.getLongitude(),amapLocation.getLatitude()});
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                LogUtil.e("AmapError:"+"location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
            }
        }
    }

    private void updateLocation(double[] location){
        if(User.getInstance().isLogin()){
            Map<String,Object> params=new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("location",location);
            Request.post(updateLocation, params, new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {

                }
            });
        }
    }
}
