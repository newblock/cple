package com.qcx.mini.utils.mapUtils;


import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkStep;
import com.qcx.mini.R;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 *
 */

public class QuRoutOverlay {

    public static Polyline drawPath(AMap aMap,PolylineOptions polylineOptions){
        if(aMap==null||polylineOptions==null){
            return null;
        }
        return aMap.addPolyline(polylineOptions);
    }

    public static Polyline drawPath(AMap aMap,List<LatLng> latLngs){
        if(aMap==null||latLngs==null){
            return null;
        }
        return aMap.addPolyline(getPolylineOptions(latLngs));
    }

    public static PolylineOptions getPolylineOptions(List<LatLng> latLngs){
        return new PolylineOptions().
                addAll(latLngs)
                .width(UiUtils.getSize(10))
                .setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.map_line));
    }

    public static PolylineOptions getPolylineOptions(List<LatLng> latLngs,int color){
        return new PolylineOptions().
                addAll(latLngs)
                .width(UiUtils.getSize(10))
                .color(color);
    }


    public static List<LatLng> getLatLngs(WalkPath walkPath) {
        List<LatLng> latLngs =new ArrayList<>();
        List<WalkStep> path = walkPath.getSteps();

        for (WalkStep step : path) {
            List<LatLonPoint> latlonPoints = step.getPolyline();
            for (LatLonPoint latlonpoint : latlonPoints) {
                latLngs.add(convertToLatLng(latlonpoint));
            }
        }
        return latLngs;
    }
    public static List<LatLng> getLatLngs(DrivePath drivePath) {
        List<LatLng> latLngs =new ArrayList<>();

        List<DriveStep> drivePaths = drivePath.getSteps();

        for (DriveStep step : drivePaths) {
            List<LatLonPoint> latlonPoints = step.getPolyline();
            for (LatLonPoint latlonpoint : latlonPoints) {
                latLngs.add(convertToLatLng(latlonpoint));
            }
        }
        return latLngs;
    }


    public static LatLng convertToLatLng(LatLonPoint point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }
}
