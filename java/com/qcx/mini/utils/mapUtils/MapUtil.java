package com.qcx.mini.utils.mapUtils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.activity.TravelDetail_DriverActivity;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

import java.io.File;
import java.util.List;

import static com.amap.api.maps.model.Text.ALIGN_LEFT;
import static com.amap.api.maps.model.Text.ALIGN_RIGHT;

/**
 * Created by Administrator on 2017/11/24.
 */

public class MapUtil {
    private Context context;
    private AMap aMap;

    public MapUtil(Context context,AMap aMap){
        this.context=context;
        this.aMap=aMap;
    }

    public Text addText( double[] location,String text){
        if(location!=null&&location.length==2){
            LogUtil.i("addText   "+text+ "  location1="+location[0]+" location2="+location[1]);
            if(text==null){
                text=" ";
            }
            TextOptions textOptions=new TextOptions().text(text)
                    .position(new LatLng(location[1],location[0]))
                    .backgroundColor(Color.TRANSPARENT)
                    .fontColor(Color.BLACK)
                    .fontSize((int)(12* UiUtils.SCREENRATIO))
                    .align(ALIGN_LEFT,0);
            return aMap.addText(textOptions);
        }else {
            LogUtil.i("addText  null= "+text);
            return null;
        }
    }

    public Text addOrChangeText(Text text,double[] location,String textStr){
        if(TextUtils.isEmpty(textStr)||location==null){
            if(text!=null){
                text.setVisible(false);
                return text;
            }else {
                return null;
            }
        }

        if(text!=null){
            text.setPosition(new LatLng(location[1],location[0]));
            text.setText(textStr);
        }else {
            text=addText(location,textStr);
        }
        return text;
    }

    /**
     *
     * @param location index0 经度，index1纬度
     * @param img
     * @return
     */
    public Marker addMarkerToMap(double[] location, int img){
        if(location!=null&&location.length==2){
            MarkerOptions homeOptions = new MarkerOptions()
                    .position(new LatLng(location[1],location[0]))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(),img)));
            return aMap.addMarker(homeOptions);
        }else {
            return null;
        }
    }
    public Marker addMarkerToMap(double[] location, int img,float anchor){
        if(location!=null&&location.length==2){
            MarkerOptions homeOptions = new MarkerOptions()
                    .position(new LatLng(location[1],location[0]))
                    .anchor(anchor,anchor)
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(),img)));
            return aMap.addMarker(homeOptions);
        }else {
            return null;
        }
    }
    /**
     *
     * @param marker
     * @param location index 0 经度，index 1 纬度
     */
    public void moveMorker(Marker marker,double[] location){
        if(marker!=null){
            marker.setPosition(new LatLng(location[1],location[0]));
        }else {
            LogUtil.e("move a null marker");
        }
    }

    public Marker addOrMoveMarker(Marker marker,double[] location,int img){
        if(marker!=null&&!marker.isRemoved()){
            moveMorker(marker,location);
        }else {
            marker=addMarkerToMap(location,img);
        }
        return marker;
    }

    /**
     * 去除LOGO和缩放按钮
     */
    public void iniMap(){
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoLeftMargin(20000);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
    }

    /**
     * 画路线
     * @param driveRouteResult
     */
    public NextDrivingRouteOverlay drawPath(DriveRouteResult driveRouteResult){
        if(driveRouteResult!=null){
            DrivePath drivePath = driveRouteResult.getPaths()
                    .get(0);
            NextDrivingRouteOverlay nextDrivingRouteOverlay = new NextDrivingRouteOverlay(
                    context, aMap, drivePath,
                    driveRouteResult.getStartPos(),
                    driveRouteResult.getTargetPos(), null);
            nextDrivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
            nextDrivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
            nextDrivingRouteOverlay.removeFromMap();
            nextDrivingRouteOverlay.addToMap();
            showAllMarkers(200,nextDrivingRouteOverlay.getStartMarker(),nextDrivingRouteOverlay.getEndMarker());
            return nextDrivingRouteOverlay;
        }else {
            return null;
        }
    }

    /**
     * 画路线
     * @param driveRouteResult
     * @param pathColor 路线颜色
     */
    public NextDrivingRouteOverlay drawPath(DriveRouteResult driveRouteResult,int pathColor,boolean isShowMarker){
        if(driveRouteResult==null){
            return null;
        }else {
            DrivePath drivePath = driveRouteResult.getPaths()
                    .get(0);
            NextDrivingRouteOverlay nextDrivingRouteOverlay = new NextDrivingRouteOverlay(
                    context, aMap, drivePath,
                    driveRouteResult.getStartPos(),
                    driveRouteResult.getTargetPos(), null);
            if(pathColor!=0){
                nextDrivingRouteOverlay.setShowTexture(false);
            }
            nextDrivingRouteOverlay.setPathColor(pathColor);
            nextDrivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
            nextDrivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
            nextDrivingRouteOverlay.removeFromMap();
            nextDrivingRouteOverlay.setShowStartAndEndMarker(isShowMarker);
            nextDrivingRouteOverlay.addToMap();
            nextDrivingRouteOverlay.zoomToSpan(400);
            return nextDrivingRouteOverlay;
        }
    }

    /**
     *
     * @param listener 定位回调接口
     * @param showMyLocation 是否显示小蓝点
     *  @param imgId 定位蓝点图片资源ID 0表示显示默认图标
     */
    public void location(AMap.OnMyLocationChangeListener listener,boolean showMyLocation,int imgId,boolean isShowScope) {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(showMyLocation);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        if(imgId!=0){
            try {
                myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(),imgId)));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(!isShowScope){
            myLocationStyle.strokeColor(0);//设置定位蓝点精度圆圈的边框颜色的方法。
            myLocationStyle.radiusFillColor( 0);//设置定位蓝点精度圆圈的填充颜色的方法。
        }
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        // aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(listener);
    }

    public void showAllMarkers(int space, Marker... markers) {
       showAllMarkers(space,null,markers);
    }

    public void showAllMarkers(int space, AMap.CancelableCallback callback, Marker... markers) {
        try {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
            for(Marker marker:markers){
                boundsBuilder.include(marker.getPosition());//把所有点都include进去（LatLng类型）
            }
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), space),callback);//第二个参数为四周留空宽度
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showAllLine(double[] start,double[] end,int left,int right,int top,int bottom,AMap.CancelableCallback callback){
        try {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
            boundsBuilder.include(new LatLng(start[1],start[0]));
            boundsBuilder.include(new LatLng(end[1],end[0]));
            aMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(boundsBuilder.build(),left,right,top,bottom),callback);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void showAllTip(int space, AMap.CancelableCallback callback, List<Tip> tips) {
        try {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
            for(Tip tip:tips){
                if(tip!=null&&tip.getPoint()!=null){
                    boundsBuilder.include(new LatLng(tip.getPoint().getLatitude(),tip.getPoint().getLongitude()));//把所有点都include进去（LatLng类型）
                }
            }
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), space),callback);//第二个参数为四周留空宽度
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 组装地图截图和其他View截图，需要注意的是目前提供的方法限定为MapView与其他View在同一个ViewGroup下
     *@param    bitmap             地图截图回调返回的结果
     *@param   viewContainer      MapView和其他要截图的View所在的父容器ViewGroup
     *@param   mapView            MapView控件
     *@param   views              其他想要在截图中显示的控件
     * */
    public static Bitmap getMapAndViewScreenShot(Bitmap bitmap, ViewGroup viewContainer, MapView mapView, View...views){
        int width = viewContainer.getWidth();
        int height = viewContainer.getHeight();
        final Bitmap screenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenBitmap);
        canvas.drawBitmap(bitmap, mapView.getLeft(), mapView.getTop(), null);
        for (View view:views){
            view.setDrawingCacheEnabled(true);
            canvas.drawBitmap(view.getDrawingCache(), view.getLeft(), view.getTop(), null);
        }

        return screenBitmap;
    }

    public void setZoom(float zoom){
        aMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    public void changeMapStyle(){
//        aMap.setCustomMapStylePath(ConstantString.FilePath.getMapStyleFilePath());
//        aMap.setMapCustomEnable(true);
    }

    /**
     * 逆地理编码获取信息
     * @param regeocode
     * @return
     */
    public Tip getTip(RegeocodeResult regeocode){
        if(regeocode==null) {
            return null;
        }
        Tip tip = new Tip();
        RegeocodeAddress address = regeocode.getRegeocodeAddress();
        String addressName;
        tip.setPostion(new LatLonPoint(regeocode.getRegeocodeQuery().getPoint().getLatitude()
                , regeocode.getRegeocodeQuery().getPoint().getLongitude()));
        if (address.getAois() != null
                && address.getAois().size() > 0
                && !TextUtils.isEmpty(address.getAois().get(0).getAoiName())) {
            addressName = address.getAois().get(0).getAoiName();
        } else if (!TextUtils.isEmpty(address.getFormatAddress())) {
            addressName = address.getFormatAddress();
        } else {
            addressName = address.getCity() + "未知地名";
        }
        tip.setName(addressName);


        LogUtil.i("\naddress.getDistrict()=" + address.getDistrict()
                + "\naddress.getAdCode()=" + address.getAdCode()
                + "\naddress.getBuilding()=" + address.getBuilding()
                + "\naddress.getCity()=" + address.getCity()
                + "\naddress.getCityCode()=" + address.getCityCode()
                + "\naddress.getCountry()=" + address.getCountry()
                + "\naddress.getFormatAddress()=" + address.getFormatAddress()
                + "\naddress.getNeighborhood()=" + address.getNeighborhood()
                + "\naddress.getProvince()=" + address.getProvince()
                + "\naddress.getTowncode()=" + address.getTowncode()
                + "\naddress.getTownship()=" + address.getTownship()
                + "\naddress.getAois().size()=" + address.getAois().size()
                + "\naddress.getBusinessAreas()=" + address.getBusinessAreas()
                + "\naddress.getCrossroads()=" + address.getCrossroads()
                + "\naddress.getTownship().size()=" + address.getPois().size()
                + "\naddress.getStreetNumber()=" + address.getStreetNumber().getStreet()
                + "\naddress.getRoads().size()=" + address.getRoads().size()
        );
//        for (int j = 0; j < address.getAois().size(); j++) {
//            LogUtil.i("aoi name=" + address.getAois().get(j).getAoiName());
//            LogUtil.i("aoi id=" + address.getAois().get(j).getAoiId());
//            LogUtil.i("aoi code=" + address.getAois().get(j).getAdCode());
//
//        }
//        for (int j = 0; j < address.getPois().size(); j++) {
//            LogUtil.i("poi name=" + address.getPois().get(j).getAdName());
//            LogUtil.i("poi id=" + address.getPois().get(j).getAdCode());
//            LogUtil.i("poi code=" + address.getPois().get(j).getPoiId());
//
//        }

        return tip;
    }

    public static int getSpace(double[] start,double[] end){
        int normalSpace=100;
        double mapWidth=UiUtils.getSize(375);
        double mapHeight=UiUtils.getSize(647);

        double showWidth=UiUtils.getSize(375);
        double showHeight=UiUtils.getSize(337);

        double height=calculateLineDistance(start,new double[]{start[0],end[1]});
        double width=calculateLineDistance(start,new double[]{end[0],start[1]});

        double wSlope=showHeight/showWidth;
        double lSlpe=height/width;

        if(wSlope>lSlpe){//横着
            return (int)(mapWidth-showWidth+normalSpace);
        }else {//竖着
            return (int)(mapHeight-showHeight+normalSpace);
        }
    }


    /**
     * 根据经纬度计算两点间距离，此距离为相对较短的距离，单位米。
     * @param start
     *           起点的坐标 [{ 经度，纬度 }]
     * @param end
     *           终点的坐标[{ 经度，纬度 }]
     * @return
     */
    public static double calculateLineDistance(double[] start, double[] end) {
        if ((start == null) || (end == null))
        {
            throw new IllegalArgumentException("非法坐标值，不能为null");
        }
        double d1 = 0.01745329251994329D;

        double d2 = start[0];
        double d3 = start[1];
        double d4 = end[0];
        double d5 = end[1];
        d2 *= d1;
        d3 *= d1;
        d4 *= d1;
        d5 *= d1;
        double d6 = Math.sin(d2);
        double d7 = Math.sin(d3);
        double d8 = Math.cos(d2);
        double d9 = Math.cos(d3);
        double d10 = Math.sin(d4);
        double d11 = Math.sin(d5);
        double d12 = Math.cos(d4);
        double d13 = Math.cos(d5);
        double[] arrayOfDouble1 = new double[3];
        double[] arrayOfDouble2 = new double[3];
        arrayOfDouble1[0] = (d9 * d8);
        arrayOfDouble1[1] = (d9 * d6);
        arrayOfDouble1[2] = d7;
        arrayOfDouble2[0] = (d13 * d12);
        arrayOfDouble2[1] = (d13 * d10);
        arrayOfDouble2[2] = d11;
        double d14 = Math.sqrt((arrayOfDouble1[0] - arrayOfDouble2[0]) * (arrayOfDouble1[0] - arrayOfDouble2[0])
                + (arrayOfDouble1[1] - arrayOfDouble2[1]) * (arrayOfDouble1[1] - arrayOfDouble2[1])
                + (arrayOfDouble1[2] - arrayOfDouble2[2]) * (arrayOfDouble1[2] - arrayOfDouble2[2]));

        return (Math.asin(d14 / 2.0D) * 12742001.579854401D);
    }

}
