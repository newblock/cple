package com.qcx.mini.utils;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.qcx.mini.ConstantString;
import com.qcx.mini.MainClass;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.listener.LocationListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Created by Administrator on 2018/4/8.
 */

public class Location {

    private  AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationListener mLocationListener;
    private static Location mLocation;

    private Location(){
        mLocationClient=new AMapLocationClient(MainClass.getInstance());
        mLocationOption=new AMapLocationClientOption();
        mLocationListener=new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                SharedPreferencesUtil.getAppSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_LOCATION_CITY,aMapLocation.getCity());
                if(locationListener!=null){
                    if(aMapLocation.getErrorCode()==0){
                        locationListener.onLocationChanged(aMapLocation);
                    }else {
                        locationListener.onError(aMapLocation.getErrorCode());
                    }
                }
            }
        };
    }

    public static Location getInstance(){
        if(mLocation==null){
            mLocation=new Location();
        }
        mLocation.initOption();
        return mLocation;
    }

    private void initOption(){
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
//        mLocationOption.setInterval(1000);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setHttpTimeOut(6000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
    }

    public void startLocation(){
        mLocationClient.setLocationListener(mLocationListener);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    private LocationListener locationListener;
    public void startLocation(final LocationListener listener, @Nullable AMapLocationClientOption option){
        this.locationListener=listener;
        if(option!=null){
            mLocationClient.setLocationOption(option);
        }
        mLocation.startLocation();
    }

    public void onStop(){
        if(mLocationClient!=null) {
            mLocationClient.stopLocation();
        }
    }

    public void onDestroy(){
        if(mLocationClient!=null){
            mLocationClient.onDestroy();
            mLocationClient=null;
            mLocation=null;
        }
    }

    public void setListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public Location setLocationListener(AMapLocationListener locationListener) {
        this.mLocationListener = locationListener;
        return this;
    }

    public AMapLocationClientOption getmLocationOption() {
        return mLocationOption;
    }

    public void setmLocationOption(AMapLocationClientOption mLocationOption) {
        this.mLocationOption = mLocationOption;
    }

}
