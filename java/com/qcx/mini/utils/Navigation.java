package com.qcx.mini.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;

import java.util.Locale;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by Administrator on 2018/1/24.
 */

public class Navigation {


    public static void toBaidu(double lon, double lat, String name, int navMode, Context context) {
        try {
            double[] bd = bd_encrypt(lat, lon);
            Intent i1 = new Intent();
            String modeStr = "";
            switch (navMode) {
                case ConstantValue.NavigationMode.DRIVE:
                    modeStr="driving";
                    break;
                case ConstantValue.NavigationMode.BUS:
                    modeStr="transit";
                    break;
                case ConstantValue.NavigationMode.RIDING:
                    modeStr="riding";
                    break;
                case ConstantValue.NavigationMode.WALK:
                    modeStr="walking";
                    break;
                default:
                    modeStr="walking";
                    break;
            }
            i1.setData(Uri.parse("baidumap://map/direction?destination=name:" + name + "|latlng:" + String.valueOf(bd[1]) + "," + String.valueOf(bd[0]) + "&mode=" + modeStr));
            context.startActivity(i1);
        } catch (Exception e) {
            ToastUtil.showToast("未找到百度地图APP");
        }
    }

    public static void togGaode(double lon, double lat, String name, int navMode, Context context) {
        try {
            String dat;
            dat = String.format(Locale.CHINA, "amapuri://route/plan/?dlat=%f&dlon=%f&dname=%s&dev=0&t=%d", lat, lon, name, navMode);
            Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse(dat));
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showToast("未找到高德地图APP");
        }
    }

    public static void pathGaode(double[] start, String startAddress, double end[], String endAddress, Context context, boolean isDriving) {
        try {
            String dat;
//            dat = String.format(Locale.CHINA,"amapuri://route/plan/?dlat=%f&dlon=%f&dname=%s&dev=0&t=2", lat, lon, name);
            String m;
            if (isDriving) {
                m = "0";
            } else {
                m = "2";
            }
            if (TextUtils.isEmpty(startAddress)) {
                dat = String.format(Locale.CHINA, "amapuri://route/plan/?slat=%f&slon=%f&dlat=%f&dlon=%f&dname=%s&dev=0&t=%s"
                        , start[1], start[0], end[1], end[0], endAddress, m);
            } else {
                dat = String.format(Locale.CHINA, "amapuri://route/plan/?slat=%f&slon=%f&sname=%s&dlat=%f&dlon=%f&dname=%s&dev=0&t=%s"
                        , start[1], start[0], startAddress, end[1], end[0], endAddress, m);
            }
//dat=amapuri://route/plan/?sid=BGVIS1&slat=39.92848272&slon=116.39560823&sname=A&did=BGVIS2&dlat=39.98848272&dlon=116.47560823&dname=B&dev=0&t=0

            Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse(dat));
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showToast("未找到高德地图APP");
        }
    }

    public static void pathBaidu(double[] start, String startAddress, double end[], String endAddress, Context context, boolean isDriving) {
        try {
            Intent i1 = new Intent();
            String m;
            start = bd_encrypt(start[1], start[0]);
            end = bd_encrypt(end[1], end[0]);
            if (isDriving) {
                m = "driving";
            } else {
                m = "walking";
            }
            String uriStr;
            if (TextUtils.isEmpty(startAddress)) {
                uriStr = "baidumap://map/direction?origin=latlng:" + String.valueOf(start[1]) + "," + String.valueOf(start[0])
                        + "&destination=name:" + endAddress + "|latlng:" + String.valueOf(end[1]) + "," + String.valueOf(end[0]) + "&mode=" + m;
            } else {
                uriStr = "baidumap://map/direction?origin=name:" + startAddress + "|latlng:" + String.valueOf(start[1]) + "," + String.valueOf(start[0])
                        + "&destination=name:" + endAddress + "|latlng:" + String.valueOf(end[1]) + "," + String.valueOf(end[0]) + "&mode=" + m;
            }
            LogUtil.i("UUUUUUUUUUU uri=" + uriStr);
            i1.setData(Uri.parse(uriStr));
            context.startActivity(i1);
        } catch (Exception e) {
            ToastUtil.showToast("未找到百度地图APP");
        }
    }

    public static void navigation(double lon, double lat, String name, Context context, int type,int navMode) {
        if (context == null) {
            return;
        }
        switch (type) {
            case ConstantValue.NavigationType.GAODE:
                togGaode(lon, lat, name, navMode,context);
                break;
            case ConstantValue.NavigationType.BAIDU:
                toBaidu(lon, lat, name,navMode, context);
                break;
        }
    }

    public static void path(double[] start, String startAddress, double end[], String endAddress, Context context, boolean isDriving, int type) {

        if (context == null || start == null || end == null || start.length != 2 || end.length != 2) {
            return;
        }
        switch (type) {
            case ConstantValue.NavigationType.GAODE:
                pathGaode(start, startAddress, end, endAddress, context, isDriving);
                break;
            case ConstantValue.NavigationType.BAIDU:
                pathBaidu(start, startAddress, end, endAddress, context, isDriving);
                break;
        }
    }

    //高德转百度
    public static double[] bd_encrypt(double gg_lat, double gg_lon) {
        double bd_lat;
        double bd_lon;
        double x = gg_lon, y = gg_lat;
        double z = sqrt(x * x + y * y) + 0.00002 * sin(y * Math.PI);
        double theta = atan2(y, x) + 0.000003 * cos(x * Math.PI);
        bd_lon = z * cos(theta) + 0.0065;
        bd_lat = z * sin(theta) + 0.006;
        return new double[]{bd_lon, bd_lat};
    }
}
