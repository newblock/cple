package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.AlmightyRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemHomeAndCompanyViewHolder;
import com.qcx.mini.adapter.viewHolder.ItemSearchAddressTipViewHolder;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.HomeAndCommpanyEntity;
import com.qcx.mini.listener.ItemHomeAndCompanyClickListener;
import com.qcx.mini.listener.ItemSearchAddressTipClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.KeybordS;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.utils.mapUtils.MapUtil;
import com.qcx.mini.utils.mapUtils.NextDrivingRouteOverlay;
import com.qcx.mini.widget.AddWayPointInfoWindowAdapter;
import com.qcx.mini.widget.WayPointEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by zsp on 2017/6/13.
 */

public class SelectRouteStrategyActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener,
        AMap.OnMapLongClickListener,
        AMap.OnMapClickListener,
        AMap.OnInfoWindowClickListener,
        AMap.OnMarkerClickListener,
        GeocodeSearch.OnGeocodeSearchListener,
        Inputtips.InputtipsListener {
    private AlmightyRecyclerViewAdapter addressAdapter;
    private int strategy;
    private ArrayList<Tip> waypoints;
    private MapUtil mMapUtil;
    private AddWayPointInfoWindowAdapter infoWindowAdapter;
    private Marker lastLongClickMarker;
    private GeocodeSearch mGeocoderSearch;

    private Tip startTip;
    private Tip endTip;

    private MapView mapView;
    private AMap aMap;
    private RouteSearch routeSearch;
//    private int f = 1;//第一次进入加载数据
//    private int d=1;//记录规划模式

    //底部信息
    @BindView(R.id.select_route_strategy_shortTime)
    LinearLayout shortTime;
    @BindView(R.id.select_route_strategy_title1)
    TextView title1;
    @BindView(R.id.select_route_strategy_time1)
    TextView time1;
    @BindView(R.id.select_route_strategy_distance1)
    TextView distance1;
    @BindView(R.id.select_route_strategy_shortDistance)
    LinearLayout shortDistance;
    @BindView(R.id.select_route_strategy_title2)
    TextView title2;
    @BindView(R.id.select_route_strategy_time2)
    TextView time2;
    @BindView(R.id.select_route_strategy_distance2)
    TextView distance2;
    @BindView(R.id.select_route_strategy_lessCongestion)
    LinearLayout lessCongestion;
    @BindView(R.id.select_route_strategy_title3)
    TextView title3;
    @BindView(R.id.select_route_strategy_time3)
    TextView time3;
    @BindView(R.id.select_route_strategy_distance3)
    TextView distance3;

    //起终点
    @BindView(R.id.select_route_strategy_points_view)
    LinearLayout v_points;
    @BindView(R.id.select_route_strategy_list)
    RecyclerView rv_list;
    @BindView(R.id.select_route_strategy_line)
    View v_line;
    @BindView(R.id.select_route_strategy_input_start)
    EditText et_start;
    @BindView(R.id.select_route_strategy_input_end)
    EditText et_end;
    @BindView(R.id.select_route_strategy_edit)
    EditText et_edit;
    private List<WayPointEditText> pointViews;

    @OnClick(R.id.select_route_strategy_add_point)
    void addPoint() {
        int size = v_points.getChildCount();
        if (size < 5) {
            addWayPointEditText(null,true);
            checkFocus();
        } else {
            ToastUtil.showToast("最多添加三个途径点");
        }
    }

    @OnClick(R.id.select_route_strategy_back)
    void back() {
        if (et_focus != null) {
            KeybordS.closeKeybord(et_focus, this);
        }
        finish();
    }

    @OnClick(R.id.select_route_strategy_exchange)
    void exchange() {
        Tip tip;
        tip = startTip;
        startTip = endTip;
        endTip = tip;
        et_start.setTag(startTip);
        et_end.setTag(endTip);
        et_start.setText(startTip.getName());
        et_end.setText(endTip.getName());
        if (!isInput && aMap != null) {
            aMap.clear();
            showMarker();
            routeSearchAll();
        }
    }

    private void routeSearchAll(){
        strategy = RouteSearch.DRIVING_SINGLE_DEFAULT;
        routeSearch(strategy);
        routeSearch(RouteSearch.DRIVING_SINGLE_SHORTEST);
        routeSearch(RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION);
        setBackground(1);
    }

    @OnClick(R.id.select_route_strategy_shortTime)
    void shortTime() {
        strategy = RouteSearch.DRIVING_SINGLE_DEFAULT;
        routeSearch(strategy);
        setBackground(1);
    }

    @OnClick(R.id.select_route_strategy_shortDistance)
    void shortDistance() {
        strategy = RouteSearch.DRIVING_SINGLE_SHORTEST;
        routeSearch(strategy);
        setBackground(2);
    }

    @OnClick(R.id.select_route_strategy_lessCongestion)
    void lessCongestion() {
        strategy = RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION;
        routeSearch(strategy);
        setBackground(3);
    }

    @OnClick(R.id.select_route_strategy_complete)
    void complete() {
        Intent intent = new Intent();
        intent.putExtra("strategy", strategy);
        intent.putExtra("start", startTip);
        intent.putExtra("end", endTip);
        if (waypoints != null && waypoints.size() > 0) {
            intent.putExtra("waypoints", waypoints);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showAllPoint() {
        if (startTip == null || endTip == null || aMap == null) {
            return;
        }

        List<Tip> tips = new ArrayList<>();
        tips.add(startTip);
        tips.add(endTip);
        if (waypoints != null) {
            tips.addAll(waypoints);
        }
        mMapUtil.showAllTip(250, null, tips);
    }

    private void routeSearch(int mode) {
        if (startTip == null || endTip == null) {
            return;
        }
        if (routeSearch == null) {
            routeSearch = new RouteSearch(this);
            routeSearch.setRouteSearchListener(this);
        }
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startTip.getPoint(), endTip.getPoint());
        List<LatLonPoint> points = new ArrayList<>();
        for (int i = 0; i < waypoints.size(); i++) {
            points.add(waypoints.get(i).getPoint());
        }
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, points, null, null);
        routeSearch.calculateDriveRouteAsyn(query);
    }

    private void setBackground(int position) {
        if (position == 1) {
            setBackground1(true);
            setBackground2(false);
            setBackground3(false);
        }
        if (position == 2) {
            setBackground1(false);
            setBackground2(true);
            setBackground3(false);
        }
        if (position == 3) {
            setBackground1(false);
            setBackground2(false);
            setBackground3(true);
        }
    }

    private void setBackground1(boolean isSelected) {
        if (isSelected) {
            setBackgroundSelected(shortTime, title1, time1, distance1);
        } else {
            setBackgroundNom(shortTime, title1, time1, distance1);
        }
    }

    private void setBackground2(boolean isSelected) {
        if (isSelected) {
            setBackgroundSelected(shortDistance, title2, time2, distance2);
        } else {
            setBackgroundNom(shortDistance, title2, time2, distance2);
        }
    }

    private void setBackground3(boolean isSelected) {
        if (isSelected) {
            setBackgroundSelected(lessCongestion, title3, time3, distance3);
        } else {
            setBackgroundNom(lessCongestion, title3, time3, distance3);
        }
    }

    private void setBackgroundNom(LinearLayout var1, TextView var2, TextView var3, TextView var4) {
        var1.setBackground(getResources().getDrawable(R.drawable.bg_select_route_layout_normal));
        var2.setBackground(getResources().getDrawable(R.drawable.bg_select_route_textview_normal));
        var2.setTextColor(0xffffffff);
        var3.setTextColor(0xff404040);
        var4.setTextColor(0xff9BA7BA);
    }

    private void setBackgroundSelected(LinearLayout var1, TextView var2, TextView var3, TextView var4) {
        var1.setBackground(getResources().getDrawable(R.drawable.bg_select_route_layout_selected));
        var2.setBackground(getResources().getDrawable(R.drawable.bg_select_route_textview_selected));
        var2.setTextColor(0xffffffff);
        var3.setTextColor(0xFF499EFF);
        var4.setTextColor(0xFF499EFF);
    }

    private String dateFormate(long time) {
        String timeStr;
        long hour;
        long minute;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                timeStr = minute + "分";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return hour + "小时";
                minute = minute % 60;
                timeStr = hour + "小时" + minute + "分";
            }
        }
        return timeStr;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_select_route_strategy;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("");
        pointViews = new ArrayList<>();

        mapView = findViewById(R.id.select_route_strategy_mapView);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            mMapUtil = new MapUtil(this, aMap);
        }
        if (aMap != null) {
            aMap.setOnMapLongClickListener(this);
            aMap.setOnMapClickListener(this);
            aMap.setOnInfoWindowClickListener(this);
            aMap.setOnMarkerClickListener(this);
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.getUiSettings().setLogoLeftMargin(20000);
            infoWindowAdapter = new AddWayPointInfoWindowAdapter(this);
            infoWindowAdapter.setListener(addInfoWindowClickListener);
            aMap.setInfoWindowAdapter(infoWindowAdapter);
            MapUtil mapUtil = new MapUtil(this, aMap);
            mapUtil.changeMapStyle();
        }

        initPoint();
        strategy = RouteSearch.DRIVING_SINGLE_DEFAULT;
        setBackground(1);
        routeSearch(strategy);
        initAddressListView();

        et_start.addTextChangedListener(watcher);
        et_end.addTextChangedListener(watcher);

        et_start.setOnFocusChangeListener(onFocusChangeListener);
        et_end.setOnFocusChangeListener(onFocusChangeListener);

        et_start.setOnTouchListener(editTextClickListener);
        et_end.setOnTouchListener(editTextClickListener);

        mGeocoderSearch = new GeocodeSearch(this);
        mGeocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 初始化页面点数据
     */
    private void initPoint() {
        startTip = getIntent().getParcelableExtra("startTip");
        endTip = getIntent().getParcelableExtra("endTip");
        waypoints = getIntent().getParcelableArrayListExtra("waypoints");

        if (waypoints == null) {
            waypoints = new ArrayList<>();
        }
        if (startTip != null) {
            et_start.setTag(startTip);
            et_start.setText(startTip.getName());
        }
        if (endTip != null) {
            et_end.setTag(endTip);
            et_end.setText(endTip.getName());
        }
        for (int i = 0; i < waypoints.size(); i++) {
            addWayPointEditText(waypoints.get(i),true);
        }
        et_focus = et_start;
    }

    /**
     * 添加一个WayPointEditText 到v_points
     *
     */
    private void addWayPointEditText(@Nullable Tip tip,boolean isVisibility) {
        final WayPointEditText view = new WayPointEditText(this);
        view.setImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInput) {
                    v_points.removeView(view);
                    pointViews.remove(view);
                    initPoints();
                    checkFocus();
                    LogUtil.i("size="+pointViews.size()+"    wayPoints.size="+waypoints.size());
                }
            }
        });
        view.getEditText().setOnTouchListener(editTextClickListener);
        view.getEditText().setOnFocusChangeListener(onFocusChangeListener);
        if (tip != null) {
            view.getEditText().setText(tip.getName());
            view.getEditText().setTag(tip);
        }
        view.addTextChangedListener(watcher);
        if(isVisibility){
            view.setVisibility(View.VISIBLE);
        }else {
            view.setVisibility(View.GONE);
        }
        v_points.addView(view, v_points.getChildCount() - 1);
        pointViews.add(view);
    }

    /**
     * 初始化地址列表和 地址数据
     */
    private void initAddressListView() {
        addressAdapter = new AlmightyRecyclerViewAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(addressAdapter);
        getAddress();
    }

    /**
     * 检查是否有未输入的输入框
     * 全部输入则显示地图
     * 有未输入的显示输入View
     */
    private void checkFocus() {
        if (et_start.getTag() == null) {
            et_start.requestFocus();
            et_focus = et_start;
            KeybordS.openKeybord(et_focus, this);
            showList(true);
            return;
        }

        if (pointViews != null) {
            for (int i = 0; i < pointViews.size(); i++) {
                if (pointViews.get(i).getVisibility() == View.VISIBLE) {
                    EditText et = pointViews.get(i).getEditText();
                    if (et.getTag() == null) {
                        et.requestFocus();
                        et_focus = et;
                        KeybordS.openKeybord(et_focus, this);
                        showList(true);
                        return;
                    }
                }
            }
        }
        if (et_end.getTag() == null) {
            et_end.requestFocus();
            et_focus = et_end;
            KeybordS.openKeybord(et_focus, this);
            showList(true);
            return;
        }
        et_focus = et_edit;
        et_edit.requestFocus();
        KeybordS.closeKeybord(et_edit, this);
        showList(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkFocus();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    private String getDistanceDescribe(float distance){
        return String.valueOf((int)distance / 1000).concat("公里");
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
//                        mDriveRouteResult = result;
                    int mode = driveRouteResult.getDriveQuery().getMode();
                    final DrivePath drivePath = driveRouteResult.getPaths().get(0);
                    switch (mode) {
                        case RouteSearch.DRIVING_SINGLE_DEFAULT:
                            time1.setText(dateFormate(drivePath.getDuration()));
                            distance1.setText(getDistanceDescribe(drivePath.getDistance()));
                            break;
                        case RouteSearch.DRIVING_SINGLE_SHORTEST:
                            time2.setText(dateFormate(drivePath.getDuration()));
                            distance2.setText(getDistanceDescribe(drivePath.getDistance()));
                            break;
                        case RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION:
                            time3.setText(dateFormate(drivePath.getDuration()));
                            distance3.setText(getDistanceDescribe(drivePath.getDistance()));
                            break;
                        default:
                            break;
                    }
                    if(mode==strategy){
                        hideLoadingDialog();
                        aMap.clear();
                        showMarker();
                        final DriveRouteResult driveRouteResult1 = driveRouteResult;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                NextDrivingRouteOverlay nextDrivingRouteOverlay = new NextDrivingRouteOverlay(
                                        SelectRouteStrategyActivity.this, aMap, drivePath,
                                        driveRouteResult1.getStartPos(),
                                        driveRouteResult1.getTargetPos(), null);
                                nextDrivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                                nextDrivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                                nextDrivingRouteOverlay.removeFromMap();
                                nextDrivingRouteOverlay.setShowStartAndEndMarker(false);
                                nextDrivingRouteOverlay.addToMap();
                                showAllPoint();
                            }
                        }).start();

                    }
                } else {
                    hideLoadingDialog();
                }

            } else {
                hideLoadingDialog();
            }
        } else {
            hideLoadingDialog();
        }

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (lastLongClickMarker != null) {
            lastLongClickMarker.remove();
        }
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 5, GeocodeSearch.AMAP);
        mGeocoderSearch.getFromLocationAsyn(regeocodeQuery);

        lastLongClickMarker = mMapUtil.addMarkerToMap(new double[]{latLng.longitude, latLng.latitude}, R.mipmap.way_point_normal);
        infoWindowAdapter.setWayPointMarker(false);
        lastLongClickMarker.showInfoWindow();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (Marker mk : mapScreenMarkers) {
            mk.hideInfoWindow();
        }
        if (lastLongClickMarker != null) {
            LogUtil.i("onMarkerClick======================remove");
            lastLongClickMarker.remove();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        LatLonPoint lonPoint = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
//        if (marker.getTitle().equals("删除途经点")) {
//            marker.hideInfoWindow();
//            waypoints.remove(lonPoint);
//            marker.remove();
//        }
//        if (marker.getTitle().equals("添加途经点")) {
//            waypoints.add(lonPoint);
//        }
//        routeSearch(strategy);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(lastLongClickMarker)) {
            infoWindowAdapter.setWayPointMarker(false);
        } else {
            infoWindowAdapter.setWayPointMarker(true);
        }
        marker.showInfoWindow();
        return true;
    }

    private void getAddress() {
        if (User.getInstance().isLogin()) {
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            Request.post(URLString.homeAndCompanyAddress, params, new EntityCallback(HomeAndCommpanyEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    hideLoadingDialog();
                    HomeAndCommpanyEntity entity = (HomeAndCommpanyEntity) t;
                    List<HomeAndCommpanyEntity> entities = new ArrayList<>();
                    entities.add(entity);
                    addressAdapter.addData(0, ItemHomeAndCompanyViewHolder.class, entities, R.layout.item_home_and_company, homeAndCompanyClickListener);
                }

                @Override
                public void onError(String errorInfo) {
                    super.onError(errorInfo);
                    hideLoadingDialog();
                }
            });
        }

    }

    private ItemHomeAndCompanyClickListener homeAndCompanyClickListener = new ItemHomeAndCompanyClickListener() {
        @Override
        public void onHomeClick(Tip homeTip) {
            if (homeTip != null) {
                selectedAddress(homeTip);
            }
        }

        @Override
        public void onCompanyClick(Tip companyTip) {
            if (companyTip != null) {
                selectedAddress(companyTip);
            }
        }
    };

    private ItemSearchAddressTipClickListener addressListener = new ItemSearchAddressTipClickListener() {
        @Override
        public void onAddressClick(Tip tip) {
            addressAdapter.clear(ItemSearchAddressTipViewHolder.class);
            selectedAddress(tip);
        }

        @Override
        public void onClearClick() {

        }
    };

    private void showList(boolean isShow) {
        isInput = isShow;
        try {
            if (isShow) {
                rv_list.animate().translationY(0).setDuration(300);
                rv_list.setVisibility(View.VISIBLE);
                v_line.setVisibility(View.VISIBLE);
                openPointsView();
            } else {
                rv_list.animate().translationY(UiUtils.getPixelV()).setDuration(300);
                v_line.setVisibility(View.GONE);
                closePointsView();
                if (aMap != null) {
                    aMap.clear();
                    showMarker();
                    routeSearchAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closePointsView(){
        isQueryTips=false;
        StringBuilder stringBuilder = new StringBuilder();
        if (pointViews.size() > 1) {
            stringBuilder.append("经");
            stringBuilder.append(waypoints.size());
            stringBuilder.append("地：");
            for (int i = 0; i < pointViews.size(); i++) {
                if (i < waypoints.size()) {
                    stringBuilder.append(waypoints.get(i).getName());
                    stringBuilder.append("、");
                }
                if (i > 0) {
                    pointViews.get(i).setVisibility(View.GONE);
                }
            }
        }
        if (pointViews.size() > 0) {
            pointViews.get(0).setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(stringBuilder)) {
                if (waypoints.size() > 0) stringBuilder.append(waypoints.get(0).getName());
            }
            pointViews.get(0).getEditText().setText(stringBuilder);
            pointViews.get(0).setImageResource(R.mipmap.btn_dot);
        }
        isQueryTips=true;
    }

    private void openPointsView(){
        if (pointViews.size() > 0) {
            for (int i = 0; i < pointViews.size(); i++) {
                pointViews.get(i).setVisibility(View.VISIBLE);
            }
            EditText et = pointViews.get(0).getEditText();
            pointViews.get(0).setImageResource(R.mipmap.btn_close_mini);
            if (et.getTag() != null) {
                isQueryTips=false;
                et.setText(((Tip) et.getTag()).getName());
                if (et_focus == et) {
                    et.setSelection(0, et.getText().length());
                }
                isQueryTips=true;
            }
        }
    }

    @Override
    public void onGetInputtips(List<Tip> list, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPoint() == null) {
                    list.remove(i);
                    i--;
                }
                if (rv_list.getAdapter() != addressAdapter) {
                    rv_list.setAdapter(addressAdapter);
                }
                addressAdapter.clear(ItemSearchAddressTipViewHolder.class);
                addressAdapter.addData(ItemSearchAddressTipViewHolder.class, list, R.layout.item_set_address, addressListener);
            }
        }
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChangeAddress) {
//                type = INPUT_TYPE_START;
                queryTips(s);
            } else {
                isChangeAddress = true;
            }
            if (TextUtils.isEmpty(s)) {
                et_focus.setTag(null);
            }
        }
    };

    private EditText et_focus;
    View.OnTouchListener editTextClickListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            showList(true);
            v.performClick();
            return false;
        }
    };

    View.OnFocusChangeListener onFocusChangeListener=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus&&v instanceof EditText){
                et_focus= (EditText) v;
            }
        }
    };

    private boolean isChangeAddress = true;

    public void queryTips(Editable s) {
        if (isQueryTips) {
            String newText = s.toString().trim();
            LogUtil.i("aaaaaaaaaaaaaaaa queryTips="+newText);
            if (!TextUtils.isEmpty(newText)) {
                InputtipsQuery inputquery = new InputtipsQuery(newText, SharedPreferencesUtil.getAppSharedPreferences().getString(ConstantString.SharedPreferencesKey.SP_LOCATION_CITY));
                Inputtips inputTips = new Inputtips(this, inputquery);
                inputTips.setInputtipsListener(this);
                inputTips.requestInputtipsAsyn();
            }
        }
    }

    boolean isQueryTips = true;
    boolean isInput = false;

    private void selectedAddress(Tip tip) {
        isQueryTips = false;
        setPointToEditTextt(et_focus, tip);
        isQueryTips = true;
        checkFocus();
    }

    private void setPointToEditTextt(EditText editText, Tip tip) {
        if (tip == null || TextUtils.isEmpty(tip.getName())) {
            editText.setText("");
        } else {
            editText.setText(tip.getName());
            editText.setSelection(tip.getName().length());
            editText.setTag(tip);
        }
        initPoints();
    }

    private void initPoints() {
        if (waypoints != null) {
            waypoints.clear();
        } else {
            waypoints = new ArrayList<>();
        }

        startTip = (Tip) et_start.getTag();
        endTip = (Tip) et_end.getTag();

        for (int i = 0; i < pointViews.size(); i++) {
            Tip p = (Tip) pointViews.get(i).getEditText().getTag();
            waypoints.add(p);
        }
    }

    private void showMarker() {
        if (mMapUtil == null) return;

        if (startTip != null) {
            Marker startMarker = addMarker(startTip, R.mipmap.icon_map_star_driver);
            if (startMarker != null) {
                startMarker.setInfoWindowEnable(false);
            }
        }
        if (endTip != null) {
            Marker endMarker = addMarker(endTip, R.mipmap.icon_map_end_driver);
            if (endMarker != null) {
                endMarker.setInfoWindowEnable(false);
            }

        }
        if (waypoints != null && waypoints.size() > 1) {
            int imgId;
            for (int i = 0; i < waypoints.size(); i++) {
                switch (i) {
                    case 0:
                        imgId = R.mipmap.way_point_1;
                        break;
                    case 1:
                        imgId = R.mipmap.way_point_2;
                        break;
                    case 2:
                        imgId = R.mipmap.way_point_3;
                        break;
                    default:
                        imgId = R.mipmap.way_point;
                        break;
                }
                addMarker(waypoints.get(i), imgId);
            }
        } else if (waypoints != null && waypoints.size() == 1) {
            addMarker(waypoints.get(0), R.mipmap.way_point);
        }
    }

    private Marker addMarker(Tip tip, int img) {
        if (mMapUtil == null || tip == null) {
            return null;
        }
        Marker marker = mMapUtil.addMarkerToMap(new double[]{tip.getPoint().getLongitude(), tip.getPoint().getLatitude()}, img);
        marker.setTitle(tip.getName());
        return marker;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        if (regeocodeResult != null) {
            RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
            if (address != null) {
                Tip tip = mMapUtil.getTip(regeocodeResult);
                if (tip.getPoint().getLatitude() == lastLongClickMarker.getPosition().latitude
                        && tip.getPoint().getLongitude() == lastLongClickMarker.getPosition().longitude)
                    lastLongClickMarker.setTitle(tip.getName());
                infoWindowAdapter.notifyDataChanged();
            }
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


    private AddWayPointInfoWindowAdapter.OnInfoWindowClickListener addInfoWindowClickListener = new AddWayPointInfoWindowAdapter.OnInfoWindowClickListener() {
        @Override
        public void onDelete(Marker marker) {
            for (int i = 0; i < waypoints.size(); ) {
                if (waypoints.get(i).getPoint().getLongitude() == marker.getPosition().longitude
                        && waypoints.get(i).getPoint().getLatitude() == marker.getPosition().latitude) {
                    v_points.removeView(pointViews.get(i));
                    pointViews.remove(i);
                    waypoints.remove(i);
                    initPoints();
                    showList(false);
                } else {
                    i++;
                }
            }
        }

        @Override
        public void onAdd(Marker marker) {
            int size = v_points.getChildCount();
            if (size < 5) {
                if(!TextUtils.isEmpty(marker.getTitle())){
                    Tip tip = new Tip();
                    tip.setName(marker.getTitle());
                    tip.setPostion(new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude));
                    addWayPointEditText(tip,true);
                    initPoints();
                    showList(false);
                }else {
                    ToastUtil.showToast("正在获取位置信息，请稍后...");
                }
            } else {
                ToastUtil.showToast("最多添加三个途径点");
            }
        }

        @Override
        public void onEnd(Marker marker) {
            if(!TextUtils.isEmpty(marker.getTitle())){
                Tip tip = new Tip();
                tip.setName(marker.getTitle());
                tip.setPostion(new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude));
                endTip = tip;
                setPointToEditTextt(et_end, endTip);
                initPoints();
                showList(false);
            }else {
                ToastUtil.showToast("正在获取位置信息，请稍后...");
            }
        }
    };
}
