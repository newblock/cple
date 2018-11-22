package com.qcx.mini.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.share.ShareTravelEntity;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.utils.mapUtils.MapUtil;
import com.qcx.mini.utils.mapUtils.NextDrivingRouteOverlay;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class ShareTravelAvtivity extends BaseActivity implements RouteSearch.OnRouteSearchListener {
    private static String tokenURL = "https://api.weixin.qq.com/cgi-bin/token?";//grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static String appid = "wx63cb1dcf8f5b1d6d";
    private static String secret = "d13c9406f70013fc5303e503ba1072f8";
    private static String URLStr = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";
    private static String travel_img_name = "travel.jpg";
    private ShareTravelEntity travel;
    private RouteSearch routeSearch;
    private Marker startMarker;
    private Marker endMarker;
    private boolean isShare=false;
    private boolean isLoading=false;

    private MapUtil mMapUtil;
    private AMap aMap;
    @BindView(R.id.share_travel_mapView)
    MapView mapView;

    @BindView(R.id.share_travel_miniPro)
    ImageView iv_miniPro;
    @BindView(R.id.share_travel_mapimg)
    ImageView iv_mapImg;
    @BindView(R.id.share_travel_shareView)
    ViewGroup v_share;
    @BindView(R.id.share_travel_startAddress)
    TextView tv_startAddress;
    @BindView(R.id.share_travel_endAddress)
    TextView tv_endAddress;

    @BindView(R.id.share_travel_type_text)
    TextView tv_typeText;
    @BindView(R.id.share_travel_startTime)
    TextView tv_startTime;
    @BindView(R.id.share_travel_seatsNum)
    TextView tv_seatsNum;
    @BindView(R.id.share_travel_price)
    TextView tv_price;
    @BindView(R.id.share_travel_seatsText)
    TextView tv_seatsText;
    @BindView(R.id.share_travel_price_text)
    TextView tv_priceText;
    @BindView(R.id.share_travel_icon)
    ImageView iv_icon;
    @BindView(R.id.share_travel_addressView)
    View v_address;
    @BindView(R.id.share_travel_bottom_info)
    View v_bottom;
    @BindView(R.id.share_travel_cardView)
    View v_card;
    @OnClick(R.id.share_travel_cancel)
    void cancel(){
        finish();
    }


    @OnClick(R.id.share_travel_submit)
    void submit() {
        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {
                iv_mapImg.setImageBitmap(bitmap);
                saveBitmap(MapUtil.getMapAndViewScreenShot(bitmap, v_share, mapView, v_share));
                ShareUtil.shareTravelImg(ShareTravelAvtivity.this, ConstantString.FilePath.getImageFilePath() + File.separator + travel_img_name);
                isShare=true;
                shareTravel(travel);
            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {

            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_share_travel;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        mMapUtil = new MapUtil(this, aMap);
        mMapUtil.iniMap();
        mMapUtil.changeMapStyle();
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        travel = getIntent().getParcelableExtra("travel");
        if (travel != null) {
            showLoadingDialog();
            isLoading=true;
            LogUtil.i(travel.toString());
            getToken();
            showTravelInfo(travel);
            showInfo(travel);
        } else {
            ToastUtil.showToast("分享失败");
            finish();
        }
    }

    @Override
    public boolean setStatusBar() {
        UiUtils.setStatusBarTransparent(this);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        if (aMap!=null) aMap=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(final DriveRouteResult driveRouteResult, int rCode) {
        hideLoading();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DrivePath drivePath = driveRouteResult.getPaths()
                                    .get(0);
                            NextDrivingRouteOverlay nextDrivingRouteOverlay = new NextDrivingRouteOverlay(
                                    ShareTravelAvtivity.this, aMap, drivePath,
                                    driveRouteResult.getStartPos(),
                                    driveRouteResult.getTargetPos(), null);
                            nextDrivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                            nextDrivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                            nextDrivingRouteOverlay.removeFromMap();
                            nextDrivingRouteOverlay.setShowStartAndEndMarker(false);
                            nextDrivingRouteOverlay.addToMap();
                            nextDrivingRouteOverlay.zoomToSpan(400);
                            mMapUtil.showAllMarkers(150,startMarker,endMarker);
                        }
                    }).start();
                }

            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private void showInfo(ShareTravelEntity travel){
        tv_startAddress.setText(travel.getStartAddress());
        tv_endAddress.setText(travel.getEndAddress());
        tv_startTime.setText(travel.getStartTime());
        tv_price.setText(CommonUtil.formatPrice(travel.getPrice(),2));
        tv_seatsNum.setText(travel.getSeatsNum());
        if(travel.getTravelType()==0){//车主行程
            v_card.setBackgroundResource(R.mipmap.img_chezhaorencard);
            tv_typeText.setText(String.format("我是车主%s，快来订座吧",travel.getName()));
            tv_seatsText.setText("座");
            tv_priceText.setText("元/座");
        }else {
            v_card.setBackgroundResource(R.mipmap.img_renzhaochecard);
            tv_typeText.setText(String.format("我是乘客%s，车主快来抢单吧",travel.getName()));
            tv_seatsText.setText("人乘车");
            tv_priceText.setText("元(合计)");
        }

        if(!TextUtils.isEmpty(travel.getIcon())){
            Picasso.with(this)
                    .load(travel.getIcon())
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
        }
    }

    //显示行程信息
    private void showTravelInfo(ShareTravelEntity travel) {
        if(travel.getStart()!=null&&travel.getEnd()!=null&&travel.getStart().length==2&&travel.getEnd().length==2){
            LatLonPoint dStart = new LatLonPoint(travel.getStart()[1], travel.getStart()[0]);
            LatLonPoint dEnd = new LatLonPoint(travel.getEnd()[1], travel.getEnd()[0]);

            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(dStart, dEnd);
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, 0, CommonUtil.getLatLonPints(travel.getWaypoints()), null, "");
            routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
            startMarker=mMapUtil.addMarkerToMap(travel.getStart(), R.mipmap.icon_map_star_driver);
            endMarker=mMapUtil.addMarkerToMap(travel.getEnd(), R.mipmap.icon_map_end_driver);
            mMapUtil.addText(travel.getStart(),travel.getStartAddress());
            mMapUtil.addText(travel.getEnd(),travel.getEndAddress());
        }else {
            ToastUtil.showToast("获取线路信息失败");
            hideLoading();
        }

    }

    void hideLoading(){
        if(!isLoading){
            hideLoadingDialog();
        }else {
            isLoading=false;
        }
    }

    private void getToken() {
        String url = String.format(Locale.CHINA, "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid, secret);
        OkGo.<String>get(url).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    String s = response.body();
                    JSONObject jsonObject = new JSONObject(s);
                    String token = jsonObject.getString("access_token");
                    if (token != null) {
                        getMiniPro(token);
                    }else {
                        hideLoading();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoading();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                hideLoading();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        if (isShare){
//            shareTravel(travel);
//        }
        finish();
    }

    void getMiniPro(String token) {
        String url = URLStr.concat(token);
        Map<String, Object> params = new HashMap<>();
        params.put("scene", String.format(Locale.CHINA, "%s,%s,%d", travel.getTravelId(), User.getInstance().getPhoneNumber(), travel.getTravelType()));
//        params.put("page", "src/shareTravelDetails/shareTravelDetails");
        params.put("page", ShareUtil.SHARE_TRAVEL_PATH);
        params.put("auto_color", true);
        try {
            OkGo.<Bitmap>post(url)
                    .upJson(new JSONObject(params))
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Response<Bitmap> response) {
                            iv_miniPro.setImageBitmap(response.body());
                            hideLoading();
                        }

                        @Override
                        public void onError(Response<Bitmap> response) {
                            super.onError(response);
                            hideLoading();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            hideLoading();
        }
    }

    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    public void saveBitmap(final Bitmap bm) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request("android.permission.WRITE_EXTERNAL_STORAGE")
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            File f = new File(ConstantString.FilePath.getImageFilePath(), travel_img_name);
                            if (f.exists()) {
                                f.delete();
                            } else {
                                try {
                                    f.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                FileOutputStream out = new FileOutputStream(f);
                                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                                out.flush();
                                out.close();
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            DialogUtil.showSetPermissionDialog(ShareTravelAvtivity.this,getSupportFragmentManager(), "SD卡读写权限");
                        }
                    }
                });


    }


    private void shareTravel(ShareTravelEntity travel){
        if(travel!=null){
            Map<String,Object> params=new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("travelId",travel.getTravelId());
            params.put("travelType",travel.getTravelType());
            Request.post(URLString.travelShare, params, new EntityCallback(null) {
                @Override
                public void onSuccess(Object t) {
                    finish();
                }

                @Override
                public void onError(String errorInfo) {
                    super.onError(errorInfo);
                }
            });

        }
    }
}
