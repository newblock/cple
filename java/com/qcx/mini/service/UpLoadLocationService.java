package com.qcx.mini.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.qcx.mini.utils.RealTimePositionUtil;

/**
 * Created by Administrator on 2018/5/15.
 */

public class UpLoadLocationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        RealTimePositionUtil.getInstance().startLocation()
                .startUploadingLocation();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("UpLoadLocationService onDestroy invoke");
        super.onDestroy();
    }
}
