package com.qcx.mini.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.qcx.mini.MainClass;

/**
 * Created by zsp on 2017/6/27.
 */

public class LocationUtil implements AMapLocationListener {
    private static LocationUtil realTimePositionUtil;

    private int interval=10*1000;
    private Context context;
    private boolean isLocation=false;//已开启定位后不能再开启
    private AMapLocationListener listener;

    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient;

    private LocationUtil(){
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

    private void loaction(){
        mLocationOption.setInterval(interval);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
        isLocation=true;
    }

    public LocationUtil stopLocation(){
        if(mlocationClient!=null) {
            isLocation=false;
            mlocationClient.stopLocation();
        }
        return this;
    }

    public void destroyLocation(){
        if(mlocationClient!=null) mlocationClient.stopLocation();
        realTimePositionUtil =null;
    }


    public static LocationUtil getInstance () {
        if(realTimePositionUtil ==null){
            realTimePositionUtil =new LocationUtil();
        }
        return realTimePositionUtil;
    }

    //开启定位
    public LocationUtil startLocation(){
        if(!isLocation){
            loaction();
        }
        return this;
    }

    public void setInterval(int interval){
        this.interval=interval;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
//                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                amapLocation.getLatitude();//获取纬度
//                amapLocation.getLongitude();//获取经度
//                amapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                df.format(date);//定位时间
                if(listener!=null){
                    listener.onLocationChanged(amapLocation);
                }
//                Log.i("dddd","getLatitude="+amapLocation.getLatitude()+" getLongitude="+amapLocation.getLongitude());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError","location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
            }
        }
    }
}
