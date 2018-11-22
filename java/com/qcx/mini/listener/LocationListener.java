package com.qcx.mini.listener;

import com.amap.api.location.AMapLocation;

/**
 * Created by Administrator on 2018/4/8.
 */

public interface LocationListener {
    void onLocationChanged(AMapLocation aMapLocation);
    void onError(int errorCode);
    void onNoLocationPermission();

}
