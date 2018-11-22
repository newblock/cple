package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.QuFragmentPagerAdapter;
import com.qcx.mini.adapter.TravelDetailHeadsAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.entity.HeadEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.LateEntity;
import com.qcx.mini.entity.TravelDetail_DriverEntity;
import com.qcx.mini.fragment.TravelDetailPassengerInfoFragment;
import com.qcx.mini.listener.OnMoveListener;
import com.qcx.mini.listener.TravelDetailPassengerClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.share.ShareTravelEntity;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.RealTimePositionUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.utils.mapUtils.DarkInfoWindowAdapter;
import com.qcx.mini.utils.mapUtils.MapUtil;
import com.qcx.mini.utils.mapUtils.NextDrivingRouteOverlay;
import com.qcx.mini.utils.mapUtils.QuMapPoint;
import com.qcx.mini.widget.AllHeightViewPager;
import com.qcx.mini.widget.MonitorMoveLinearLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class TravelDetail_DriverActivity extends BaseActivity implements AMap.OnMyLocationChangeListener,
        RouteSearch.OnRouteSearchListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener {
    private AMap aMap;
    private RouteSearch routeSearch;
    private MapUtil mMapUtil;
    private boolean isRouteSearch = true;//是否规划线路

    private Map<Long, Marker[]> passengerMarkers;//乘客的上下车点
    private Map<Long, Text[]> passengerTexts;//乘客的上下车点文字

    private TravelDetail_DriverEntity mTravelDetail;
    private HeadEntity mPassenger;//当前显示的乘客

    private TravelDetailHeadsAdapter mHeadImaAdapter;

    private long travelID;

    private Marker getOnMarker, getOffMarker;//乘客上下车点


    List<TravelDetailPassengerInfoFragment> fragments = new ArrayList<>();

    List<QuMapPoint> passengersPoints;

    @BindView(R.id.travel_detail_driver_mapView)
    MapView mMapView;
    @BindView(R.id.travel_detail_driver_back)
    ImageView iv_back;
    @BindView(R.id.travel_detail_driver_time)
    TextView tv_time;
    @BindView(R.id.travel_detail_driver_headList)
    RecyclerView rv_headList;
    @BindView(R.id.travel_detail_driver_operation)
    ImageView iv_operation;
    @BindView(R.id.travel_detail_driver_position_line)
    View v_position;

    @BindView(R.id.travel_detail_driver_viewPager)
    AllHeightViewPager vp_passengerInfo;
    @BindView(R.id.travel_detail_driver_control_travel_info_img)
    ImageView iv_showPassenger;
    @BindView(R.id.travel_detail_driver_travel_card)
    MonitorMoveLinearLayout v_travelCard;

    boolean isShowedTravel = true;

    @OnClick(R.id.travel_detail_driver_control_travel_info)
    void showTravelInfo() {
        showTravelInfoView(!isShowedTravel);
    }

    @OnClick(R.id.travel_detail_driver_back)
    void back() {
        finish();
    }

    @OnClick(R.id.travel_detail_driver_location)
    void showAllLine() {
        if (mTravelDetail != null) {
            if (mTravelDetail.getTravel() != null) {
                double[] start = mTravelDetail.getTravel().getStart();
                double[] end = mTravelDetail.getTravel().getEnd();
                int space = UiUtils.getSize(80);
                mMapUtil.showAllLine(start, end, space, space, space, UiUtils.getSize(255) + space, null);
            }

        }
    }

    private boolean isStart = false;
    private boolean isFinish = false;

    @OnClick(R.id.travel_detail_driver_operation)
    void operation() {
        if (mTravelDetail == null) return;
        int status = mTravelDetail.getTravel().getTravelStatus();
        switch (status) {
            case 1://未发车
            case 2://将发车
                isStart = true;
                getData();
//                startTravel();
                break;
            case 3://已发车
                isFinish = true;
                getData();
//                finishTravel();
                break;
            case 4://完成
                ToastUtil.showToast("该行程已结束");
                break;
            case -1://行程取消
                ToastUtil.showToast("该行程已取消");
                break;
            default:
                ToastUtil.showToast("未知行程状态");
                break;
        }

    }

//    @OnClick(R.id.travel_detail_driver_function_phone)
//    void call() {
//        if (mPassenger != null)
//            DialogUtil.call(this, new String[]{String.valueOf(mPassenger.getPhone())});
//    }

//    @OnClick(R.id.travel_detail_driver_function_navi)
//    void navi() {
//        if (mPassenger == null) return;
//        String[] items = {"导航至上车点", "导航至下车点"};
//        final double[] start = mPassenger.getStart();
//        final double[] end = mPassenger.getEnd();
//        final String startAddress = mPassenger.getStartAddress();
//        final String endAddress = mPassenger.getEndAddress();
//
//        final ItemsDialog dialog = new ItemsDialog(this, items, null);
//        dialog.itemTextColor(Color.BLACK)
//                .cancelText(Color.BLACK)
//                .cancelText("取消")
//                .isTitleShow(false)
//                .layoutAnimation(null).show();
//        dialog.setOnOperItemClickL(new OnOperItemClickL() {
//            @Override
//            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        DialogUtil.navigation(start, startAddress, TravelDetail_DriverActivity.this);
//                        break;
//                    case 1:
//                        DialogUtil.navigation(end, endAddress, TravelDetail_DriverActivity.this);
//                        break;
//                }
//                dialog.dismiss();
//            }
//        });
//    }

//    @OnClick(R.id.travel_detail_driver_function_more)
//    void more() {
//        String[] items = {"此乘客迟到", "联系客服", "取消行程"};
//        final ItemsDialog dialog = new ItemsDialog(this, items, null);
//        dialog.itemTextColor(Color.BLACK)
//                .cancelText(Color.BLACK)
//                .cancelText("关闭")
//                .isTitleShow(false)
//                .show();
//        dialog.setOnOperItemClickL(new OnOperItemClickL() {
//            @Override
//            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        if (mPassenger != null) {
//                            passengerLate(mPassenger.getOrdersId());
//                        } else {
//                            ToastUtil.showToast("该乘客失踪了");
//                        }
//                        break;
//                    case 1:
//                        DialogUtil.call(TravelDetail_DriverActivity.this, null);
//                        break;
//                    case 2:
//                        cancelTravel();
//                        break;
//                }
//                dialog.dismiss();
//            }
//        });
//    }

    //    @OnClick(R.id.travel_detail_driver_cancel_travel)
    void cancelTravel() {
        if (mTravelDetail == null) {
            ToastUtil.showToast("请稍后再试");
            return;
        }
        if (mTravelDetail.getTravel().getTravelStatus() == 3) {
            ToastUtil.showToast("已发车行程不可取消");
            return;
        }
        new QAlertDialog().setBTN(QAlertDialog.BTN_TWOBUTTON)
                .setImg(QAlertDialog.IMG_ALERT)
                .setTitleText("是否取消行程？")
                .setSureClickListener(new QAlertDialog.OnDialogClick() {
                    @Override
                    public void onClick(QAlertDialog dialog) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("token", User.getInstance().getToken());
                        params.put("travelId", travelID);
                        Request.post(URLString.travel_cancel_drevier, params, new EntityCallback(IntEntity.class) {
                            @Override
                            public void onSuccess(Object t) {
                                IntEntity intEntity = (IntEntity) t;
                                if (intEntity.getStatus() == 200) {
                                    ToastUtil.showToast("取消成功");
                                    finish();
                                } else {
                                    onError("取消失败");
                                }
                            }

                            @Override
                            public void onError(String errorInfo) {
                                if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
                            }
                        });
                    }
                })
                .show(getSupportFragmentManager(), "");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_detail__driver;
    }

    QuFragmentPagerAdapter<TravelDetailPassengerInfoFragment> pagerAdapter;
    private long selectedPhone = 0;

    @Override
    public void initView(Bundle savedInstanceState) {
        travelID = getIntent().getLongExtra("travelID", 0);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        mMapUtil = new MapUtil(this, aMap);
        mMapUtil.iniMap();
        mMapUtil.changeMapStyle();
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(new DarkInfoWindowAdapter(this));
        aMap.setOnInfoWindowClickListener(this);
        mMapUtil.location(this, true, R.mipmap.icon_map_car, false);

        if (routeSearch == null) {
            routeSearch = new RouteSearch(this);
            routeSearch.setRouteSearchListener(this);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_headList.setLayoutManager(manager);
        mHeadImaAdapter = new TravelDetailHeadsAdapter(this);
        rv_headList.setAdapter(mHeadImaAdapter);
        mHeadImaAdapter.setListener(new TravelDetailHeadsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, HeadEntity passenger) {
                if (position < mTravelDetail.getTravel().getSeats() - mTravelDetail.getTravel().getSurplusSeats()) {
                    if (passenger != null && mTravelDetail.getPassengers() != null && mTravelDetail.getPassengers().size() > 0) {
                        showPassengerInfo(passenger);
                        showSelectedMarker(passenger.getPhone());
                        mHeadImaAdapter.setSelectedPhone(passenger.getPhone());
                    }
                } else if (position < mTravelDetail.getTravel().getSeats()) {
                    mHeadImaAdapter.setSelectedPhone(passenger.getPhone());
                } else {
                    updateTravel(null, mTravelDetail.getTravel().getSeats() + 1);
                }
            }

            @Override
            public void onSelectedPhoneChanged(long phone) {
                int position = mHeadImaAdapter.getPosition(phone);
                selectedPhone = phone;
                LogUtil.i("set phone position=" + position + "   " + phone);
                vp_passengerInfo.setCurrentItem(position);
//                if(position==vp_passengerInfo.getCurrentItem()){
                fragments.get(position).setData(mHeadImaAdapter.getItem(position));
                LogUtil.i("set phone position  item=" + mHeadImaAdapter.getItem(position).getPhone());
//                }
            }
        });
        TravelDetailPassengerInfoFragment fragment = new TravelDetailPassengerInfoFragment();
        TravelDetailPassengerInfoFragment fragment2 = new TravelDetailPassengerInfoFragment();
        TravelDetailPassengerInfoFragment fragment3 = new TravelDetailPassengerInfoFragment();
        TravelDetailPassengerInfoFragment fragment4 = new TravelDetailPassengerInfoFragment();
        fragment.setTag(0);
        fragment2.setTag(1);
        fragment3.setTag(2);
        fragment4.setTag(3);
        fragment.setClickListener(clickListener);
        fragment2.setClickListener(clickListener);
        fragment3.setClickListener(clickListener);
        fragment4.setClickListener(clickListener);
        fragments.add(fragment);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        vp_passengerInfo.setOffscreenPageLimit(3);
        pagerAdapter = new QuFragmentPagerAdapter<>(getSupportFragmentManager(), fragments);
        vp_passengerInfo.setAdapter(pagerAdapter);
        v_travelCard.setListener(new OnMoveListener() {
            int size = UiUtils.getSize(50);

            @Override
            public void onMove(float moveX, float moveY) {
            }

            @Override
            public void onUp(float moveX, float moveY) {

                if (moveY > size) {
                    showTravelInfoView(false);
                } else if (moveY < -size) {
                    showTravelInfoView(true);
                }
            }
        });
        vp_passengerInfo.setScanScroll(false);
        vp_passengerInfo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                HeadEntity entity = mHeadImaAdapter.getData(selectedPhone);
                int listPosition = -1;
                if (entity != null) {
                    listPosition = mHeadImaAdapter.getPosition(entity.getPhone());
//                    mHeadImaAdapter.setSelectedPhone(entity.getPhone());
                } else {
                    listPosition = mHeadImaAdapter.getPosition(-1);
//                    mHeadImaAdapter.setSelectedPhone(-1);
                }
                if (listPosition == -1) {
                    v_position.setVisibility(View.GONE);
                } else {
                    v_position.setVisibility(View.VISIBLE);
                    v_position.animate().translationX(UiUtils.getSize((listPosition * 82)));
                }
                fragments.get(position).setData(entity);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getData();
    }

    @Override
    public boolean setStatusBar() {
//        UiUtils.setStatusBarTransparent(this);
        UiUtils.setStatusBarWHITE(this);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (aMap != null) aMap = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        RealTimePositionUtil.getInstance().setlistener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                getData();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        RealTimePositionUtil.getInstance().setlistener(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMyLocationChange(Location location) {
        double[] loc = new double[]{location.getLongitude(), location.getLatitude()};
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(final DriveRouteResult driveRouteResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DrivePath drivePath = driveRouteResult.getPaths()
                                    .get(0);
                            NextDrivingRouteOverlay nextDrivingRouteOverlay = new NextDrivingRouteOverlay(
                                    TravelDetail_DriverActivity.this, aMap, drivePath,
                                    driveRouteResult.getStartPos(),
                                    driveRouteResult.getTargetPos(), null);
                            nextDrivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                            nextDrivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                            nextDrivingRouteOverlay.removeFromMap();
                            nextDrivingRouteOverlay.addToMap();
                            showAllLine();
                        }
                    }).start();
                    isRouteSearch = false;
                    return;
                }
            }
        }
        isRouteSearch = true;
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (passengerLocationMarkers != null) {
            Set<Long> keys = passengerLocationMarkers.keySet();
            for (Long phone : keys) {
                if (passengerLocationMarkers.get(phone) == null) {
                    continue;
                }
                if (marker.getId().equals(passengerLocationMarkers.get(phone).getId())) {
                    return true;
                }
            }
        }

        DialogUtil.navigation(new double[]{marker.getPosition().longitude, marker.getPosition().latitude}, marker.getTitle(), ConstantValue.NavigationMode.DRIVE, this);
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelID);
        Request.post(URLString.driverTravelDetail, params, new EntityCallback(TravelDetail_DriverEntity.class) {
            @Override
            public void onSuccess(Object t) {
                mTravelDetail = (TravelDetail_DriverEntity) t;
                if (mTravelDetail.getStatus() == 200) {
                    mHeadImaAdapter.setData(mTravelDetail);
                    showPassengersPoints(mTravelDetail.getPassengers());
                    if (mTravelDetail.getTravel() != null && isRouteSearch) {
                        showTravelInfo(mTravelDetail.getTravel());
                    }
                    if (mTravelDetail != null) {
                        showDriverInfo();
                    }
                    if (mTravelDetail.getSeckillTravelNopay() != null) {//显示抢单未支付页面
                        showSeckillView(mTravelDetail.getSeckillTravelNopay());
                    } else if (mTravelDetail.getPassengers() != null && mTravelDetail.getPassengers().size() > 0) {
                        showPassengerLocation(mTravelDetail.getPassengers());
                        showPassengerInfo(mHeadImaAdapter.getItem(0));
                        initPassengerMarker(mHeadImaAdapter.getSelectedPhone(), mTravelDetail.getPassengers());
                    } else {//无乘客订座
                        showNoPassengerUI();
                    }

                    if (isStart) {
                        isStart = false;
                        startTravel();
                    }
                    if (isFinish) {
                        isFinish = false;
                        finishTravel();
                    }
                } else {
                    ToastUtil.showToast("获取行程信息失败，请稍后再试");
                }
                isFinish = false;
                isStart = false;
            }

            @Override
            public void onError(String errorInfo) {
                if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
                isFinish = false;
                isStart = false;
            }
        });
    }

    private void showPassengersPoints(List<TravelDetail_DriverEntity.Passenger> passengers) {
        if ((passengers == null || passengers.size() == 0) && passengersPoints != null) {//没有乘客，移除地图上所有已显示的乘客
            for (QuMapPoint point : passengersPoints) {
                if (point != null) {
                    point.remove();
                }
            }
            return;
        }
    }

    private void showSelectedMarker(long selectedPhone) {
        if (passengerMarkers == null) {
            return;
        }
        Set<Long> keys = passengerMarkers.keySet();
        for (Long key : keys) {
            Marker[] markers = passengerMarkers.get(key);
            for (int i = 0; i < markers.length; i++) {
                Marker marker = markers[i];
                if (marker != null) {
                    int img = 0;
                    if (key == selectedPhone) {
                        if (i == 0) {
                            img = R.mipmap.icon_map_up;
                        }
                        if (i == 1) {
                            img = R.mipmap.icon_map_down;
                        }
                    } else {
                        if (i == 0) {
                            img = R.mipmap.icon_map_scd;
                        }
                        if (i == 1) {
                            img = R.mipmap.icon_map_xcd;
                        }
                    }
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), img)));
                }
            }

        }
    }

    //初始化下单乘客的上下车点
    private void initPassengerMarker(long selectedPhone, List<TravelDetail_DriverEntity.Passenger> passengers) {
        if (passengerMarkers == null) {
            passengerMarkers = new HashMap<>();
            passengerTexts = new HashMap<>();
        } else {
            Set<Long> keys = passengerMarkers.keySet();
            for (Long key : keys) {
                boolean isExist = false;
                for (int i = 0; i < passengers.size(); i++) {
                    if (key == passengers.get(i).getPassengerPhone()) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    try {
                        if (passengerMarkers.get(key)[0] != null) {
                            passengerMarkers.get(key)[0].remove();
                        }
                        if (passengerTexts.get(key)[0] != null) {
                            passengerTexts.get(key)[0].remove();
                        }
                        if (passengerMarkers.get(key)[1] != null) {
                            passengerMarkers.get(key)[1].remove();
                        }
                        if (passengerTexts.get(key)[1] != null) {
                            passengerTexts.get(key)[1].remove();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    passengerMarkers.remove(key);
                    passengerTexts.remove(key);
                }
            }
        }
        if (passengers != null) {
            for (int i = 0; i < passengers.size(); i++) {
                Set<Long> keys = passengerMarkers.keySet();
                boolean isExist = false;
                for (Long key : keys) {
                    if (key == passengers.get(i).getPassengerPhone()) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    Marker[] pMarker = new Marker[2];//0 上车点，1 下车点
                    Text[] pTexts = new Text[2];
                    TravelDetail_DriverEntity.Passenger passenger = passengers.get(i);
                    int startImg;
                    int endImg;
                    if (selectedPhone == passenger.getPassengerPhone()) {
                        startImg = R.mipmap.icon_map_up;
                        endImg = R.mipmap.icon_map_down;
                    } else {
                        startImg = R.mipmap.icon_map_scd;
                        endImg = R.mipmap.icon_map_xcd;
                    }
                    if (passenger.getStart() != null && passenger.getStart().length == 2) {
                        pMarker[0] = mMapUtil.addMarkerToMap(passenger.getStart(), startImg);
                        pMarker[0].setTitle(passenger.getStartAddress());
                        pTexts[0] = mMapUtil.addText(passenger.getStart(), passenger.getStartAddress());
                    }
                    if (passenger.getEnd() != null && passenger.getEnd().length == 2) {
                        pMarker[1] = mMapUtil.addMarkerToMap(passenger.getEnd(), endImg);
                        pMarker[1].setTitle(passenger.getEndAddress());
                        pTexts[1] = mMapUtil.addText(passenger.getEnd(), passenger.getEndAddress());
                    }
                    passengerMarkers.put(passenger.getPassengerPhone(), pMarker);
                    passengerTexts.put(passenger.getPassengerPhone(), pTexts);
                }
            }
        }
    }

    //抢单未付款乘客卡片显示
    private void showSeckillView(TravelDetail_DriverEntity.SeckillTravelNopay seckill) {
        iv_operation.setVisibility(View.GONE);
        showPassenerONorOFFMarker(seckill.getStart(), seckill.getEnd());
    }

    //其正常下单乘客卡片显示
    private void showPassengerInfo(HeadEntity passenger) {
        if (passenger != null) {
            mPassenger = passenger;
        }
    }

    private Map<Long, Marker> passengerLocationMarkers;

    private void showPassengerLocation(List<TravelDetail_DriverEntity.Passenger> passengers) {
        if (passengers != null && passengers.size() > 0) {
            removeMarker(passengers);

            for (int i = 0; i < passengers.size(); i++) {
                final double[] location = passengers.get(i).getLocation();
                if (location == null) {
                    continue;
                }
                final long phone = passengers.get(i).getPassengerPhone();
                String iconStr = passengers.get(i).getPicture();
                boolean isExist = isExist(phone);

                if (!isExist) {
                    if (passengerLocationMarkers == null) {
                        passengerLocationMarkers = new HashMap<>();
                    }
                    Marker marker = null;
                    LayoutInflater inflater = LayoutInflater.from(this);
                    final View view = inflater.inflate(R.layout.view_map_icon, null, false);
                    ImageView icon = view.findViewById(R.id.view_map_icon);
                    MarkerOptions homeOptions = new MarkerOptions()
                            .position(new LatLng(location[1], location[0]))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_me));
                    marker = aMap.addMarker(homeOptions);
                    passengerLocationMarkers.put(phone, marker);
                    Picasso.with(this)
                            .load(iconStr)
                            .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                            .into(icon, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Marker marker1 = passengerLocationMarkers.get(phone);
                                    if (marker1 != null) {
                                        marker1.setIcon(BitmapDescriptorFactory.fromView(view));
                                    }
                                }

                                @Override
                                public void onError() {

                                }
                            });


                } else {
                    mMapUtil.moveMorker(passengerLocationMarkers.get(phone), location);
                }

            }

        } else if (passengerLocationMarkers != null) {
            Set<Long> keys = passengerLocationMarkers.keySet();
            for (Long l : keys) {
                passengerLocationMarkers.get(l).remove();
            }
            passengerLocationMarkers.clear();
        }
    }

    private void removeMarker(List<TravelDetail_DriverEntity.Passenger> passengers) {
        if (passengerLocationMarkers != null) {
            Set<Long> keys = passengerLocationMarkers.keySet();
            for (Long l : keys) {
                boolean isExist = false;
                for (int i = 0; i < passengers.size(); i++) {
                    if (l == passengers.get(i).getPassengerPhone()) {
                        isExist = true;
                    }
                }
                if (!isExist) {
                    passengerLocationMarkers.get(l).remove();
                    passengerLocationMarkers.remove(l);
                }
            }
        }
    }

    private boolean isExist(long phone) {
        if (passengerLocationMarkers != null) {
            return passengerLocationMarkers.containsKey(phone);
        }
        return false;
    }

    private void showNoPassengerUI() {
        if (mTravelDetail != null && (mTravelDetail.getTravel().getTravelStatus() == 1 || mTravelDetail.getTravel().getTravelStatus() == 2)) {
            iv_operation.setVisibility(View.GONE);
//            v_cancelTrave.setVisibility(View.VISIBLE);
//            v_function.setVisibility(View.GONE);
//            v_phone.setVisibility(View.GONE);
        }
    }

    private void showDriverInfo() {
        LogUtil.i(DateUtil.getTimeStr(mTravelDetail.getTravel().getStartTime(), null));
        int status = mTravelDetail.getTravel().getTravelStatus();
        if (status == 1 || status == 2) {
            iv_operation.setImageResource(R.mipmap.btn_mapcard_start);
            long time = mTravelDetail.getTravel().getStartTime();
            long dTime = System.currentTimeMillis() - time;
            if (dTime < 0) {
                if (mTravelDetail.getTravel().getCreateType() == 1) {
                    tv_time.setText(String.format(Locale.CHINA, "抢单成功，%s%s出发", DateUtil.formatDay("MM月dd日", mTravelDetail.getTravel().getStartTime()), DateUtil.getTimeStr(mTravelDetail.getTravel().getStartTime(), "HH:mm")));
                } else {
                    tv_time.setText(String.format(Locale.CHINA, "亲爱的，%s%s出发哦", DateUtil.formatDay("MM月dd日", mTravelDetail.getTravel().getStartTime()), DateUtil.getTimeStr(mTravelDetail.getTravel().getStartTime(), "HH:mm")));
                }
            } else {
                if (mTravelDetail.getSeckillTravelNopay() != null && mTravelDetail.getSeckillTravelNopay().getSeats() > 0) {
                    tv_time.setText("抢单成功,乘客未支付");
                } else if (mTravelDetail.getPassengers() == null || mTravelDetail.getPassengers().size() < 1) {
                    tv_time.setText("没有乘客");
                } else {
                    dTime = 15 * 60 * 1000 - dTime;
                    if (dTime > 0) {
                        tv_time.setText(String.format(Locale.CHINA, "%d分钟后自动发车", dTime / 60 / 1000));
                    } else {
                        tv_time.setText(String.format(Locale.CHINA, "即将自动发车", dTime / 60 / 1000));
                    }
                }
            }
        } else if (status == 3) {
            iv_operation.setImageResource(R.mipmap.btn_mapcard_complete);
            long time = mTravelDetail.getTravel().getStartTime();
            long dTime = 5 * 60 * 60 * 1000 + time - System.currentTimeMillis();
            if (dTime > 0) {
                tv_time.setText(String.format(Locale.CHINA, "%d小时%d分钟后自动结束行程", dTime / DateUtil.HOUR, dTime % DateUtil.HOUR / DateUtil.MINUTE));
            } else {
                tv_time.setText("即将自动结束行程");
            }

        }
    }

    //显示行程信息
    private void showTravelInfo(TravelDetail_DriverEntity.Travel travel) {
        if (isRouteSearch) {
            LatLonPoint dStart = new LatLonPoint(travel.getStart()[1], travel.getStart()[0]);
            LatLonPoint dEnd = new LatLonPoint(travel.getEnd()[1], travel.getEnd()[0]);

            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(dStart, dEnd);
            // 第一个参数表示路径规划的起点和终点
            // 第二个参数表示驾车模式
            // 第三个参数表示途经点
            // 第四个参数表示避让区域，第五个参数表示避让道路
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, travel.getStrategy(), CommonUtil.getLatLonPints(travel.getWaypoints()), null, "");
            routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
            isRouteSearch = false;
        }

    }

    //显示被抢单未支付乘客上下车点
    private void showPassenerONorOFFMarker(double[] onLocation, double[] offLocation) {
        if (onLocation != null) {
            if (getOnMarker != null) {
                mMapUtil.moveMorker(getOnMarker, onLocation);
            } else {
                getOnMarker = mMapUtil.addMarkerToMap(onLocation, R.mipmap.icon_map_up);
            }
        }
        if (offLocation != null) {
            if (getOffMarker != null) {
                mMapUtil.moveMorker(getOffMarker, offLocation);
            } else {
                getOffMarker = mMapUtil.addMarkerToMap(offLocation, R.mipmap.icon_map_down);
            }
        }
    }

    //判断所有乘客是否上车
    private boolean isAllGetOn(List<TravelDetail_DriverEntity.Passenger> e) {
        if (e == null || e.size() < 1) {
            return true;
        }
        for (TravelDetail_DriverEntity.Passenger p : e) {
            if (p.getOrderStatus() == 1 || p.getOrderStatus() == 2 || p.getOrderStatus() == 4) {
                return false;
            }
        }
        return true;
    }

    //判断是否可以发车
    private boolean canStartTravel(List<TravelDetail_DriverEntity.Passenger> e) {
        if (e == null || e.size() < 1) {
            return false;
        }
        for (TravelDetail_DriverEntity.Passenger p : e) {
            if (p.getOrderStatus() != 1) {
                return true;
            }
        }
        return false;
    }

    //发车
    private void startTravel() {
        if (!canStartTravel(mTravelDetail.getPassengers())) {
            ToastUtil.showToast("还没有乘客,不能发车");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelID);
        Request.post(URLString.departure, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                try {
                    IntEntity intEntity = (IntEntity) t;
                    if (intEntity.getStatus() == 200) {
                        iv_operation.setImageResource(R.mipmap.btn_mapcard_complete);
                        mTravelDetail.getTravel().setTravelStatus(3);
                        getData();
                    } else {
                        ToastUtil.showToast("操作失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onError("");
                }
            }

            @Override
            public void onError(String errorInfo) {
                if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
            }
        });
    }

    //结束行程
    private void finishTravel() {
        if (isAllGetOn(mTravelDetail.getPassengers())) {
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("travelId", travelID);
            Request.post(URLString.finishTravel, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity intEntity = (IntEntity) t;
                    if (intEntity.getStatus() == 200) {
                        Intent intent = new Intent(TravelDetail_DriverActivity.this, TravelHistoryDetailActivity.class);
                        intent.putExtra("travelId", travelID);
                        intent.putExtra("travelType",ConstantValue.TravelType.DRIVER);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.showToast("操作失败");
                    }
                }

            });
        } else {
            ToastUtil.showToast("还有乘客未上车");
        }
    }

    void passengerLate(String ordersId) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("ordersTravelId", ordersId);
        Request.post(URLString.passengerLate, params, new EntityCallback(LateEntity.class) {
            @Override
            public void onSuccess(Object t) {
                LateEntity entity = (LateEntity) t;
                if (entity.getStatus() == 200 && entity.isLateStatus()) {
                    getData();
                } else {
                    onError("超过上车时间5分钟才可标记");
                }
            }
        });
    }

    boolean isUpdatingTravel = false;

    void updateTravel(String startTime, int seats) {
        if (isUpdatingTravel) {
            ToastUtil.showToast("您操作太快啦!");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", mTravelDetail.getTravel().getTravelId());
        params.put("type", ConstantValue.TravelType.DRIVER);
        if (!TextUtils.isEmpty(startTime)) {
            params.put("startTime", startTime);
        }
        if (seats >= 0) {
            params.put("seats", seats);
        }
        isUpdatingTravel = true;
        Request.post(URLString.updateTravel, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                isUpdatingTravel = false;
                IntEntity intEntity = (IntEntity) t;
                if (intEntity.getStatus() == 200) {
                    getData();
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                isUpdatingTravel = false;
            }
        });
    }

    void cancelPassengerOrder(String orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("ordersTravelId", orderId);
        Request.post(URLString.cancelPassengerOrder, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity = (IntEntity) t;
                if (intEntity.getStatus() == 200) {
                    getData();
                    ToastUtil.showToast("已删除该乘客");
                }
            }
        });
    }

    private void showTravelInfoView(boolean isShow) {

        LogUtil.i("ssssssssssss " + isShow);
        if (isShow == isShowedTravel) {
            return;
        }
        isShowedTravel = isShow;
        for (int i = 0; i < fragments.size(); i++) {
            fragments.get(i).setShowTravelInfo(isShowedTravel);
        }

        if (isShowedTravel) {
            iv_showPassenger.animate().setDuration(200).rotation(0);
        } else {
            iv_showPassenger.animate().setDuration(200).rotation(180);
        }

    }

    private TravelDetailPassengerClickListener clickListener = new TravelDetailPassengerClickListener() {
        @Override
        public void onLate(HeadEntity entity) {
            passengerLate(entity.getOrdersId());
        }

        @Override
        public void onCancelTravel() {
            cancelTravel();
        }

        @Override
        public void onDeletePassenger(HeadEntity entity) {
            cancelPassengerOrder(entity.getOrdersId());
        }

        @Override
        public void onShare() {
            if (mTravelDetail != null && mTravelDetail.getTravel() != null && mTravelDetail.getDriver() != null) {
                TravelDetail_DriverEntity.Travel travelEntity = mTravelDetail.getTravel();
                TravelDetail_DriverEntity.Driver driver = mTravelDetail.getDriver();
                ShareTravelEntity travel = new ShareTravelEntity();
                travel.setStart(travelEntity.getStart());
                travel.setEnd(travelEntity.getEnd());
                travel.setStartAddress(travelEntity.getStartAddress());
                travel.setEndAddress(travelEntity.getEndAddress());
                travel.setIcon(driver.getPicture());
                travel.setName(driver.getNickName());
                travel.setPrice(travelEntity.getTravelPrice());
                travel.setSeatsNum(String.valueOf(travelEntity.getSeats()));
                travel.setTravelId(travelEntity.getTravelId());
                travel.setTravelType(ConstantValue.TravelType.DRIVER);
                travel.setStartTime(travelEntity.getStartTimeTxt());
                travel.setCar(driver.getCar());
                travel.setSurplusSeats(travelEntity.getSurplusSeats());
                travel.setAge(driver.getAge());
                travel.setSex(driver.getSex());
                travel.setWaypoints(travelEntity.getWaypoints());
                LogUtil.i(travel.toString());
                ShareUtil.shareTravel(getSupportFragmentManager(), User.getInstance().getPhoneNumber(), travel);
            }
        }

        @Override
        public void onDeleteSeat() {
            if (mTravelDetail != null && mTravelDetail.getTravel() != null && mTravelDetail.getTravel().getSeats() > 0) {
                updateTravel(null, mTravelDetail.getTravel().getSeats() - 1);
            }
        }

        @Override
        public void onAttenetionChanged() {
            getData();
        }
    };

}
