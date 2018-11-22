package com.qcx.mini.activity;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.MainClass;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.DriverAndPassengersPageAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.PayDialog;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.ItemsDialog;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.LateEntity;
import com.qcx.mini.entity.PassengerTravelDetailEntity;
import com.qcx.mini.entity.PayInfoEntity;
import com.qcx.mini.entity.TravelDetail_PassengerEntity;
import com.qcx.mini.entity.TravelDetail_noPayEntity;
import com.qcx.mini.entity.TravelsListEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.NetUtil;
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
import com.qcx.mini.utils.mapUtils.QuRoutOverlay;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class TravelDetail_PassengerActivity extends BaseActivity implements AMap.OnMyLocationChangeListener
        , RouteSearch.OnRouteSearchListener, AMap.OnInfoWindowClickListener, AMap.OnMarkerClickListener {
    //    private TravelDetail_PassengerEntity travelDetail;
//    private TravelDetail_noPayEntity noPayTravelDetail;
    private PassengerTravelDetailEntity.OrdersTravelDetail passengerTravel;
    private long travelID;
    private String ordersID;
    //    private long travelId_noDriver;//未被抢单
    private DriverAndPassengersPageAdapter pageAdapter;

    private AMap aMap;
    private MapUtil mMapUtil;
    private RouteSearch routeSearch;
    private boolean isRouteSearch = true;//是否规划线路
    private Marker startMarker, endMarker;
    private Text startText, endText;
    private int startImg, endImg;
    private Marker driverMarker;


    @BindView(R.id.travel_detail_passenger_mapView)
    MapView mMapView;
    @BindView(R.id.travel_detail_passenger_back)
    ImageView iv_back;
    @BindView(R.id.travel_detail_passenger_time)
    TextView tv_time;
    @BindView(R.id.travel_detail_passenger_function_bar)
    View v_function;
    @BindView(R.id.travel_detail_passenger_operation)
    ImageView iv_operation;


    @BindView(R.id.travel_detail_passenger_content_view)
    LinearLayout ll_content;
    @BindView(R.id.travel_detail_passenger_close_travel_img)
    ImageView iv_closeTravel;
    @BindView(R.id.travel_detail_passenger_travel_view)
    View v_travel;
    @BindView(R.id.item_travel_man_info_icon)
    ImageView iv_driverIcon;
    @BindView(R.id.item_travel_man_info_sex)
    ImageView iv_driverSex;
    @BindView(R.id.item_travel_man_info_name)
    TextView tv_driverName;
    @BindView(R.id.item_travel_man_info_carNum)
    TextView tv_driverCarNum;
    @BindView(R.id.item_travel_man_info_describe)
    TextView tv_driverDescribe;
    @BindView(R.id.item_travel_man_info_attention)
    ImageView iv_driverAttention;

    @BindView(R.id.travel_detail_passenger_other_passenger1)
    ImageView iv_otherP1;
    @BindView(R.id.travel_detail_passenger_other_passenger2)
    ImageView iv_otherP2;
    @BindView(R.id.travel_detail_passenger_other_passenger3)
    ImageView iv_otherP3;
    @BindView(R.id.travel_detail_passenger_other_passenger4)
    ImageView iv_otherP4;

    @BindView(R.id.item_travel_start_time)
    TextView tv_getOnTime;
    @BindView(R.id.item_travel_startAddress)
    TextView tv_getOn;
    @BindView(R.id.item_travel_endAddress)
    TextView tv_getOff;
    @BindView(R.id.item_travel_price)
    TextView tv_price;
    @BindView(R.id.item_travel_peoples_view)
    TextView tv_peopleVew;
    @BindView(R.id.item_travel_peoples_text)
    TextView tv_peopleNum;


    private boolean isGetOff = false;
    private boolean isGetUp = false;


    @OnClick(R.id.item_travel_man_info_attention)
    void attention() {
        if (passengerTravel == null) {
            return;
        }
        if (passengerTravel.isAttention()) {
            DialogUtil.unfollowDialog(this, passengerTravel.getDriverPhone(), new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity intEntity = (IntEntity) t;
                    if (intEntity.getStatus() == 200) {
                        passengerTravel.setAttention(false);
                        iv_driverAttention.setImageResource(R.mipmap.btn_follow_mini);
                    } else {
                        onError("操作失败");
                    }
                }
            });
        } else {
            NetUtil.attention(passengerTravel.getDriverPhone(), new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity intEntity = (IntEntity) t;
                    if (intEntity.getStatus() == 200) {
                        passengerTravel.setAttention(true);
                        iv_driverAttention.setImageResource(R.mipmap.btn_followed_mini);
                        new CenterImgDialog().show(getSupportFragmentManager(), "");
                    } else {
                        onError("操作失败");
                    }
                }
            });
        }
    }


    @OnClick(R.id.travel_detail_passenger_time_view)
    void closeTravel() {
        if (v_travel.getVisibility() == View.VISIBLE) {
            v_travel.setVisibility(View.GONE);
            iv_closeTravel.setRotation(180);
        } else {
            v_travel.setVisibility(View.VISIBLE);
            iv_closeTravel.setRotation(0);
        }
    }

    @OnClick(R.id.item_travel_man_info_icon)
    void driverIconClick() {
        if (passengerTravel != null && passengerTravel.getDriverPhone() > 0) {
            toUserInfoActivity(passengerTravel.getDriverPhone());
        }
    }

    @OnClick(R.id.travel_detail_passenger_other_passenger1)
    void otherP1Click() {
        long oPhone = (long) iv_otherP1.getTag();
        if (oPhone > 0) {
            toUserInfoActivity(oPhone);
        }
    }

    @OnClick(R.id.travel_detail_passenger_other_passenger2)
    void otherP2Click() {
        long oPhone = (long) iv_otherP2.getTag();
        if (oPhone > 0) {
            toUserInfoActivity(oPhone);
        }
    }

    @OnClick(R.id.travel_detail_passenger_other_passenger3)
    void otherP3Click() {
        long oPhone = (long) iv_otherP3.getTag();
        if (oPhone > 0) {
            toUserInfoActivity(oPhone);
        }
    }

    @OnClick(R.id.travel_detail_passenger_other_passenger4)
    void otherP4Click() {
        long oPhone = (long) iv_otherP4.getTag();
        if (oPhone > 0) {
            toUserInfoActivity(oPhone);
        }
    }


    @OnClick(R.id.travel_detail_passenger_operation)
    void operation() {
        if (passengerTravel == null) {
            return;
        }
        int ordersStatus = passengerTravel.getOrdersStatus();
        if (passengerTravel.getNoPayOrdersVo() != null) {
            showPay();
            return;
        }
        if (TextUtils.isEmpty(passengerTravel.getOrdersTravelId())) {
            creatOrder();
            return;
        }
        switch (ordersStatus) {
            case ConstantValue.OrdersStatus.PASSENGER_DOWN:
                ToastUtil.showToast("行程已完成");
                break;
            case ConstantValue.OrdersStatus.FINAL:
                ToastUtil.showToast("行程已完成");
                break;
            case ConstantValue.OrdersStatus.PAY:
                new QAlertDialog()
                        .setBTN(QAlertDialog.BTN_TWOBUTTON)
                        .setTitleText("车主未发车，是否确认上车")
                        .setCancelClickListener(new QAlertDialog.OnDialogClick() {
                            @Override
                            public void onClick(QAlertDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setSureClickListener(new QAlertDialog.OnDialogClick() {
                            @Override
                            public void onClick(QAlertDialog dialog) {
                                if (!isGetUp) {
                                    isGetUp = true;
                                    getData();
                                } else {
                                    ToastUtil.showToast("请稍后");
                                }
                                dialog.dismiss();
                            }
                        }).show(getSupportFragmentManager(), "");
                break;
            case ConstantValue.OrdersStatus.UNDER_WAY:
                if (!isGetOff) {
                    isGetOff = true;
                    getData();
                } else {
                    ToastUtil.showToast("请稍后");
                }
                break;
            case ConstantValue.OrdersStatus.NO_PAY:
                break;
            case ConstantValue.OrdersStatus.DEPARTURE://上车倒计时
                if (!isGetUp) {
                    isGetUp = true;
                    getData();
                } else {
                    ToastUtil.showToast("请稍后");
                }
                break;
            case ConstantValue.OrdersStatus.PASSENGER_LATE:
            case ConstantValue.OrdersStatus.DRIVER_LATE:
            default:
                break;
        }
    }

    @OnClick(R.id.travel_detail_passenger_function_phone)
    void phone() {
        if (passengerTravel != null && passengerTravel.getDriverPhone() > 0) {
            DialogUtil.call(this, new String[]{String.valueOf(passengerTravel.getDriverPhone())});
        }
    }

    @OnClick(R.id.travel_detail_passenger_location)
    void showAllLine() {
        if (passengerTravel == null) {
            return;
        }
        double[] start = null;
        double[] end = null;
        int space = UiUtils.getSize(80);
        start = passengerTravel.getStart();
        end = passengerTravel.getEnd();
        if (start != null && end != null) {
            mMapUtil.showAllLine(start, end, space, space, space, ll_content.getHeight(), null);
        }
    }

    @OnClick(R.id.travel_detail_passenger_function_navi)
    void navi() {
        if (passengerTravel == null) return;
        String[] items = {"导航至上车点", "导航至下车点"};
        final double[] start;
        final double[] end;
        final String startAddress;
        final String endAddress;
        if (TextUtils.isEmpty(passengerTravel.getOrdersTravelId())) {
            start = passengerTravel.getStart();
            end = passengerTravel.getEnd();
            startAddress = passengerTravel.getStartAddress();
            endAddress = passengerTravel.getEndAddress();
        } else {
            start = passengerTravel.getOrdersStart();
            end = passengerTravel.getOrdersEnd();
            startAddress = passengerTravel.getStartAddress();
            endAddress = passengerTravel.getOrdersEndAddress();
        }
        final ItemsDialog dialog = new ItemsDialog(this, items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        DialogUtil.navigation(start, startAddress, ConstantValue.NavigationMode.WALK, TravelDetail_PassengerActivity.this);
                        break;
                    case 1:
                        DialogUtil.navigation(end, endAddress, ConstantValue.NavigationMode.WALK, TravelDetail_PassengerActivity.this);
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.travel_detail_passenger_function_more)
    void more() {
        if (travelID < 1) {
            String[] items = {"取消行程", "客服"};
            final ItemsDialog dialog = new ItemsDialog(this, items, null);
            dialog.itemTextColor(Color.BLACK)
                    .cancelText(Color.BLACK)
                    .cancelText("取消")
                    .isTitleShow(false)
                    .layoutAnimation(null).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            cancelTravel();
                            break;
                        case 1:
                            DialogUtil.call(TravelDetail_PassengerActivity.this, null);
                            break;
                    }
                    dialog.dismiss();
                }
            });
        } else {
            String[] items = {"取消行程", "客服"};
            final ItemsDialog dialog = new ItemsDialog(this, items, null);
            dialog.itemTextColor(Color.BLACK)
                    .cancelText(Color.BLACK)
                    .cancelText("取消")
                    .isTitleShow(false)
                    .layoutAnimation(null).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            cancelTravel();
                            break;
                        case 1:
                            DialogUtil.call(TravelDetail_PassengerActivity.this, null);
                            break;
                    }
                    dialog.dismiss();
                }
            });
        }

    }

    void cancelTravel() {
        new QAlertDialog()
                .setBTN(QAlertDialog.BTN_TWOBUTTON)
                .setImg(QAlertDialog.IMG_ALERT)
                .setSureClickListener(new QAlertDialog.OnDialogClick() {
                    @Override
                    public void onClick(QAlertDialog dialog) {
                        cancelTO(true);
                        dialog.dismiss();
                    }
                })
                .setTitleText("您是否要取消该行程？")
                .show(getSupportFragmentManager(), "cancelOrderDialog");
    }

    @OnClick(R.id.travel_detail_passenger_back)
    void back() {
        finish();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_detail__passenger;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        mMapUtil = new MapUtil(this, aMap);
        mMapUtil.iniMap();
        mMapUtil.changeMapStyle();
        aMap.setInfoWindowAdapter(new DarkInfoWindowAdapter(this));
        aMap.setOnInfoWindowClickListener(this);
        aMap.setOnMarkerClickListener(this);
        mMapUtil.location(this, true, 0, true);

        travelID = getIntent().getLongExtra("travelID", 0);
        ordersID = getIntent().getStringExtra("ordersID");
        pageAdapter = new DriverAndPassengersPageAdapter(this);
        if (routeSearch == null) {
            routeSearch = new RouteSearch(this);
            routeSearch.setRouteSearchListener(this);
        }
        if (travelID > 0) {
            pageAdapter.setOnlyOne(true);
            v_function.setVisibility(View.VISIBLE);
            iv_operation.setImageResource(R.mipmap.btn_mapcard_pay);
            startImg = R.mipmap.icon_map_star_driver;
            endImg = R.mipmap.icon_map_end_driver;
        } else if (!TextUtils.isEmpty(ordersID)) {
            pageAdapter.setOnlyOne(false);
            v_function.setVisibility(View.VISIBLE);
            pageAdapter.setOnHeadsClickListener(new DriverAndPassengersPageAdapter.OnHeadIconClickListener() {
                @Override
                public void onClick(long phone) {
                    Intent intent = new Intent(TravelDetail_PassengerActivity.this, UserInfoActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            });
            startImg = R.mipmap.icon_map_up;
            endImg = R.mipmap.icon_map_down;
        }

        LayoutTransition lt = new LayoutTransition();
        lt.setDuration(200);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "alpha", 1F, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0).
                setDuration(lt.getDuration(LayoutTransition.DISAPPEARING));
        lt.setAnimator(LayoutTransition.DISAPPEARING, animator2);
        ll_content.setLayoutTransition(lt);

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
        mMapView.onDestroy();
        if (aMap != null) aMap = null;
        if(timer!=null){
            timer.cancel();
        }
        isStartPayCountDown = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
        RealTimePositionUtil.getInstance().setlistener(locationListener);
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            getData();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        RealTimePositionUtil.getInstance().removeListener(locationListener);
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

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    Polyline driverPath;

    @Override
    public void onDriveRouteSearched(final DriveRouteResult driveRouteResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            NextDrivingRouteOverlay nextDrivingRouteOverlay = mMapUtil.drawPath(driveRouteResult);
                            LogUtil.i("amap drawPath");
                            if (driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {
                                if (driverPath != null) {
                                    LogUtil.i("amap removePath");
                                    driverPath.remove();
                                }
                                driverPath = QuRoutOverlay.drawPath(aMap, QuRoutOverlay.getLatLngs(driveRouteResult.getPaths().get(0)));
                            }
//                    smoothCar1(driveRouteResult.getPaths().get(0).getSteps());
                            showAllLine();
                        }
                    }).start();
                    isRouteSearch = false;
                    return;
                }

            }
        }
        LogUtil.i("amap isRouteSearch=true");
        isRouteSearch = true;
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        DialogUtil.navigation(new double[]{marker.getPosition().longitude, marker.getPosition().latitude}, marker.getTitle(), ConstantValue.NavigationMode.WALK, this);
        return true;
    }

    private void getData() {
        getTravelDetail();
    }

    private void creatOrder() {
        if (passengerTravel == null) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("bookSeats", passengerTravel.getSeats());
        params.put("buyingSafety", false);
        params.put("start", passengerTravel.getStart());
        params.put("end", passengerTravel.getEnd());
        params.put("startAddress", passengerTravel.getStartAddress());
        params.put("endAddress", passengerTravel.getEndAddress());
        params.put("startTime", DateUtil.getTimeStr(passengerTravel.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
        params.put("isPickUp", true);
        params.put("extraDistance", 0);
        params.put("travelId", travelID);
        params.put("isOldApp", MainClass.isOldApp());

        showLoadingDialog();
        Request.post(URLString.createOrder, params, new EntityCallback(PayInfoEntity.class) {
            @Override
            public void onSuccess(Object t) {
                PayInfoEntity payInfo = (PayInfoEntity) t;
                LogUtil.i(payInfo.getStatus() + "");
                if (payInfo.getStatus() == 200) {
                    isShowPayDialog = true;
                    ordersID = payInfo.getOrdersTravelId();
                    getData();
                } else if (payInfo.getStatus() == -101) {
                    isShowPayDialog = true;
                    getData();
                } else if (payInfo.getStatus() == -100) {
                    onError("已有该订单");
                } else if (payInfo.getStatus() == -102) {
                    onError("订座失败 请重新尝试");
                } else if (payInfo.getStatus() == -105) {
                    onError("不可预订自己行程");
                } else if (payInfo.getStatus() == -107) {//已发车
                    onError("车主已发车，不可预定");
                } else if (payInfo.getStatus() == -108) {//没座了
                    onError("座位已被抢完");
                } else if (payInfo.getStatus() == -109) {//行程已取消
                    onError("车主已取消行程");
                } else if (payInfo.getStatus() == -103) {//行程已过期
                    onError("行程已过期");
                } else if (payInfo.getStatus() == -106) {
                    onError("未进行芝麻认证");
                } else {
                    onError("创建订单失败");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
            }
        });
    }

    private boolean isShowPayDialog = true;

    private void getTravelDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        if (travelID != 0) {
            params.put("travelId", travelID);
        }
        if (!TextUtils.isEmpty(ordersID)) {
            params.put("ordersTravelId", ordersID);
        }
        Request.post(URLString.travelOrdersDetail, params, new EntityCallback(PassengerTravelDetailEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                passengerTravel = ((PassengerTravelDetailEntity) t).getOrdersTravelDetailVo();
                if (passengerTravel == null) {
                    return;
                }
                showUI();
                showDriverLocation(passengerTravel.getLocation());

                routeSearch(passengerTravel.getStart(), passengerTravel.getEnd(), passengerTravel.getStrategy(), passengerTravel.getWaypoints());
                if (TextUtils.isEmpty(passengerTravel.getOrdersTravelId())) {
                    showStartorendMarker(passengerTravel.getStart(), passengerTravel.getEnd(), passengerTravel.getStartAddress(), passengerTravel.getEndAddress());
                } else {
                    showStartorendMarker(passengerTravel.getOrdersStart(), passengerTravel.getOrdersEnd(), passengerTravel.getOrdersStartAddress(), passengerTravel.getOrdersEndAddress());
                }
                if (isShowPayDialog) {
                    if (passengerTravel != null && passengerTravel.getNoPayOrdersVo() != null) {
                        isShowPayDialog = false;
                        showPay();
                    }
                }

                if (isGetUp) {
                    getUp();
                    isGetUp = false;
                }
                if (isGetOff) {
                    getOff();
                    isGetOff = false;
                }

            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
            }
        });
    }

    private void showUI() {
        if (passengerTravel != null) {
            showStatus(!TextUtils.isEmpty(passengerTravel.getOrdersTravelId()), passengerTravel.getOrdersStatus());
            showDriverInfo();
            showOtherPassenger(passengerTravel.getFellowTravelerVoList());
            showTravel();
        }
    }

    private void showStatus(boolean isOrders, int ordersStatus) {
        String statusText = "";
        int statusImgId = R.mipmap.btn_mapcard_pay;
        iv_operation.setVisibility(View.VISIBLE);
        if (ordersStatus != ConstantValue.OrdersStatus.NO_PAY) {
//            if (mTimer != null) {
//                mTimer.cancel();
//            }
            if(timer!=null){
                timer.cancel();
            }
            isStartPayCountDown = false;
            if (isOrders) {
                switch (ordersStatus) {
                    case ConstantValue.OrdersStatus.PASSENGER_DOWN:
                    case ConstantValue.OrdersStatus.FINAL:
                        statusImgId = R.mipmap.btn_mapcard_complete;
                        statusText = "行程已完成";
                        break;
                    case ConstantValue.OrdersStatus.PAY:
                        statusImgId = R.mipmap.btn_mapcard_next;
                        statusText = "已支付," + DateUtil.formatDay("MM-dd", passengerTravel.getOrdersStartTime()) + DateUtil.getTimeStr(passengerTravel.getOrdersStartTime(), "HH:mm") + "记得准时上车";
                        break;
                    case ConstantValue.OrdersStatus.UNDER_WAY:
                        statusImgId = R.mipmap.btn_mapcard_complete;
                        statusText = "行程中，请系好安全带";
                        break;
                    case ConstantValue.OrdersStatus.DEPARTURE://上车倒计时
                        long startTime = passengerTravel.getStartTime();
                        long cTime = System.currentTimeMillis();
                        long time;
                        if (cTime > startTime) {
                            time = 30 * DateUtil.MINUTE - (cTime - startTime);
                            if (time > 0) {
                                statusText = DateUtil.getTime(time, false, false, true, true) + "后自动上车";
                            } else {
                                statusText = "即将自动上车";
                            }
                        } else {
                            statusText = String.format("车主发车啦，%s记得准时上车", DateUtil.getTimeStr(passengerTravel.getOrdersStartTime(), "HH:mm"));
                        }
                        statusImgId = R.mipmap.btn_mapcard_next;
                        break;
                    case ConstantValue.OrdersStatus.PASSENGER_LATE:
                        statusText = "已被车主标记迟到";
                        iv_operation.setVisibility(View.INVISIBLE);
                        break;
                    case ConstantValue.OrdersStatus.DRIVER_LATE:
                        statusText = "车主迟到";
                        iv_operation.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        statusText = "未知状态";
                        iv_operation.setVisibility(View.INVISIBLE);
                        break;
                }
            } else {
                statusImgId = R.mipmap.btn_mapcard_pay;
                statusText = "车主抢单，去支付吧";
            }
            iv_operation.setImageResource(statusImgId);
            tv_time.setText(statusText);
        } else {
            if (!isStartPayCountDown) {
                iv_operation.setImageResource(R.mipmap.btn_mapcard_pay);
                isStartPayCountDown = true;
                long timeA = passengerTravel.getNoPayOrdersVo().getNopaySign().getCreatetime();
                long timeB = passengerTravel.getNoPayOrdersVo().getTimeStamp();
//                long timeB = Long.parseLong(passengerTravel.getNoPayOrdersVo().getNopaySign().getTimestamp()) * 1000;
                if ((timeA - timeB) / 1000 + 3 * 60 > 0) {
//                    starCountDown((timeA - timeB) / 1000 + 3 * 60);
                    countDown((timeA - timeB)  + 3 * 60*1000);
                } else {
                    tv_time.setText("支付超时");
                }
            }
        }

    }

    private void showTravel() {
        if (TextUtils.isEmpty(passengerTravel.getOrdersTravelId())) {
            String time = DateUtil.formatDay("MM-dd", passengerTravel.getStartTime()) + DateUtil.getTimeStr(passengerTravel.getStartTime(), "HH:mm");
            tv_getOnTime.setText(time);
            tv_getOn.setText(passengerTravel.getStartAddress());
            tv_getOff.setText(passengerTravel.getEndAddress());
            tv_price.setText(CommonUtil.formatPrice(passengerTravel.getTravelPrice(), 1));
            tv_peopleVew.setVisibility(View.VISIBLE);
            tv_peopleNum.setVisibility(View.VISIBLE);
            tv_peopleNum.setText(String.valueOf(passengerTravel.getSeats()));
        } else {
            String time = DateUtil.formatDay("MM-dd", passengerTravel.getOrdersStartTime()) + DateUtil.getTimeStr(passengerTravel.getOrdersStartTime(), "HH:mm");
            tv_getOnTime.setText(time);
            tv_getOn.setText(passengerTravel.getOrdersStartAddress());
            tv_getOff.setText(passengerTravel.getOrdersEndAddress());
            tv_price.setText(CommonUtil.formatPrice(passengerTravel.getOrdersPrice(), 1));
            tv_peopleVew.setVisibility(View.VISIBLE);
            tv_peopleNum.setVisibility(View.VISIBLE);
            tv_peopleNum.setText(String.valueOf(passengerTravel.getBookSeats()));
        }
    }

    private void showDriverInfo() {
        Picasso.with(this)
                .load(passengerTravel.getDriverPicture())
                .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                .placeholder(R.mipmap.img_me)
                .error(R.mipmap.img_me)
                .into(iv_driverIcon);
        tv_driverName.setText(passengerTravel.getNickName());
        tv_driverCarNum.setText(passengerTravel.getCarNumber());
        tv_driverDescribe.setText(passengerTravel.getCar());
        CommonUtil.setSexImg(passengerTravel.getSex(), iv_driverSex);
        if (passengerTravel.isAttention()) {
            iv_driverAttention.setImageResource(R.mipmap.btn_followed_mini);
        } else {
            iv_driverAttention.setImageResource(R.mipmap.btn_follow_mini);
        }
    }

    private ImageView[] iv_otherPs;

    private void showOtherPassenger(List<PassengerTravelDetailEntity.Picture> passengers) {
        if (iv_otherPs == null) {
            iv_otherPs = new ImageView[]{iv_otherP1, iv_otherP2, iv_otherP3, iv_otherP4};
        }
        if (passengers == null) {
            passengers = new ArrayList<>();
        }
        for (int i = 0; i < iv_otherPs.length; i++) {
            if (i < passengers.size()) {
                Picasso.with(this)
                        .load(passengers.get(i).getPicture())
                        .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                        .placeholder(R.mipmap.img_me)
                        .error(R.mipmap.img_me)
                        .into(iv_otherPs[i]);
                iv_otherPs[i].setTag(passengers.get(i).getPhone());

            } else {
                iv_otherPs[i].setVisibility(View.GONE);
            }

        }
    }

    private void showUI(TravelDetail_PassengerEntity.Travel travel) {
        int status = travel.getOrdersStatus();
        String time = "";
        if (travelID > 0) {
            iv_operation.setImageResource(R.mipmap.btn_mapcard_pay);
            tv_time.setText("车主抢单，去支付吧");

        } else {
            if (status == 4) {//车主发车了
                iv_operation.setImageResource(R.mipmap.btn_mapcard_next);
                time = String.format("车主发车了，%s上车哦", DateUtil.getTimeStr(travel.getRecommendStartTime(), "HH:mm"));
            } else if (status == 2) {//车主未发车
                iv_operation.setImageResource(R.mipmap.btn_mapcard_next);
                time = String.format("%s%s记得准时上车哦", DateUtil.formatDay("MM月dd日", travel.getRecommendStartTime()), DateUtil.getTimeStr(travel.getRecommendStartTime(), "HH:mm"));
            } else if (status == 3) {//行程中
                iv_operation.setImageResource(R.mipmap.btn_mapcard_complete);
                time = "请系好安全带,携带好随身物品";
            } else if (status == 8) {//下车
                iv_operation.setImageResource(R.mipmap.btn_mapcard_complete);
                time = "行程已完成";
            } else if (status == 0) {//完成行程
                iv_operation.setImageResource(R.mipmap.btn_mapcard_complete);
                time = "行程已完成";
            } else {
                iv_operation.setImageResource(R.mipmap.btn_mapcard_complete);
                time = DateUtil.getTimeStr(travel.getRecommendStartTime(), "MM月dd日HH:mm");
            }
            tv_time.setText(time);
        }
//        showDriverInfo(travel);
//        showOtherPassenger(travelDetail.getSameWayPassengers());
//        showTravel(travel);
    }

    private void toUserInfoActivity(long phone) {
        if (phone <= 0) {
            return;
        }
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    //显示行程信息
    private void routeSearch(double[] start, double[] end, int strategy, String points) {
        if (isRouteSearch) {
            LatLonPoint dStart = new LatLonPoint(start[1], start[0]);
            LatLonPoint dEnd = new LatLonPoint(end[1], end[0]);
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(dStart, dEnd);
            List<LatLonPoint> wayPoints = CommonUtil.getLatLonPints(points);
            if (wayPoints != null) {
                for (int i = 0; i < wayPoints.size(); i++) {
                    LogUtil.i("aMap=" + wayPoints.get(i).toString());
                }
            }
            // 第一个参数表示路径规划的起点和终点
            // 第二个参数表示驾车模式
            // 第三个参数表示途经点
            // 第四个参数表示避让区域，第五个参数表示避让道路
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, strategy, wayPoints, null, "");
            routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
            isRouteSearch = false;
        }

    }

    //显示起终点
    private void showStartorendMarker(double[] startLocation, double[] endLocation, String startAddress, String endAddress) {
        if (startLocation != null) {
            if (startMarker != null) {
                mMapUtil.moveMorker(startMarker, startLocation);
            } else {
                startMarker = mMapUtil.addMarkerToMap(startLocation, startImg);
            }
            startText = mMapUtil.addOrChangeText(startText, startLocation, startAddress);

            startMarker.setTitle(startAddress);
        }
        if (endLocation != null) {
            if (endMarker != null) {
                mMapUtil.moveMorker(endMarker, endLocation);
            } else {
                endMarker = mMapUtil.addMarkerToMap(endLocation, endImg);
            }
            endMarker.setTitle(endAddress);
            endText = mMapUtil.addOrChangeText(endText, endLocation, endAddress);
        }
    }

    private double[] lastLocation;

    private void showDriverLocation(double[] location) {
        List<LatLng> points = new ArrayList<>();
        if (lastLocation != null) {
            points.add(new LatLng(lastLocation[1], lastLocation[0]));
        }
        if (location != null) {
            points.add(new LatLng(location[1], location[0]));
        }
        if (points.size() == 1) {
            points.add(new LatLng(points.get(0).latitude, points.get(0).longitude));
        }
        smoothCar(points);
        lastLocation = location;
    }

    private List<LatLng> getLatLng(List<LatLonPoint> points) {
        if (points == null) return null;
        List<LatLng> latLngs = new ArrayList<>();
        int size = points.size();
        for (int i = 0; i < size; i++) {
            latLngs.add(new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude()));
        }
        return latLngs;

    }

    private void smoothCar1(List<DriveStep> steps) {
        if (steps == null) return;
        int size = steps.size();
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            latLngs.addAll(getLatLng(steps.get(i).getPolyline()));
        }
        Log.i("ddddd", "latlngs.size=" + latLngs.size());
        smoothCar(latLngs);
    }

    SmoothMoveMarker smoothMarker;

    private void smoothCar(List<LatLng> points) {
//        LatLngBounds bounds;
//        if(points.size()==1){
//            bounds=new LatLngBounds(points.get(0), points.get(0));
//        }else if(points.size()==2){
//            bounds=new LatLngBounds(points.get(0), points.get(1));
//        }else {
//            bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
//        }
//        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        if (points == null || points.size() == 0) {
            return;
        }
        if (smoothMarker == null) {
            smoothMarker = new SmoothMoveMarker(aMap);
            smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_car));
        }
        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

// 设置滑动的轨迹左边点
        smoothMarker.setPoints(subList);
// 设置滑动的总时间
        smoothMarker.setTotalDuration(10);
// 开始滑动
        smoothMarker.startSmoothMove();
    }

    //下车
    private void getOff() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("ordersTravelId", ordersID);
        Request.post(URLString.ordersDown, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                try {
                    IntEntity intEntity = (IntEntity) t;
                    if (intEntity.getStatus() == 200) {
                        if (passengerTravel != null) {
                            Intent intent = new Intent(TravelDetail_PassengerActivity.this, TravelHistoryDetailActivity.class);
                            intent.putExtra("ordersId", passengerTravel.getOrdersId());
                            intent.putExtra("travelType",ConstantValue.TravelType.PASSENGER);
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        onError("操作失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onError("操作失败");
                }
            }

            @Override
            public void onError(String errorInfo) {
                if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
            }
        });
    }

    //上车
    private void getUp() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("ordersTravelId", ordersID);
        Request.post(URLString.ordersUp, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                try {
                    IntEntity intEntity = (IntEntity) t;
                    if (intEntity.getStatus() == 200) {
//                        getOrderData(ordersID);
                        getData();
                    } else {
                        onError("操作失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onError("操作失败");
                }
            }

            @Override
            public void onError(String errorInfo) {
                if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
            }
        });
    }

    private void cancelTO(final boolean showResult) {
        if (passengerTravel == null) {
            ToastUtil.showToast("没有行程信息");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        String urlStr = "";
        params.put("token", User.getInstance().getToken());
        if (!TextUtils.isEmpty(passengerTravel.getOrdersTravelId())) {
            params.put("ordersTravelId", passengerTravel.getOrdersTravelId());
            params.put("refundReason", "取消行程");
            urlStr = URLString.refund;
        } else {
            params.put("passengerTravelId", passengerTravel.getPassengerTravelId());
            urlStr = URLString.travelCancel;
        }
        Request.post(urlStr, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity = (IntEntity) t;
                if (showResult) {
                    if (intEntity.getStatus() == -110) {
                        ToastUtil.showToast("今日取消行程次数已达上限");
                    } else if (intEntity.getStatus() == 110) {
                        ToastUtil.showToast("取消行程成功");
                        finish();
                    } else if (intEntity.getStatus() == -123) {
                        ToastUtil.showToast("已发车的行程不能取消");
                    }
                }
            }
        });

    }

    private void driverLate() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("ordersTravelId", ordersID);
        Request.post(URLString.ordersLate, params, new EntityCallback(LateEntity.class) {
            @Override
            public void onSuccess(Object t) {
                LateEntity entity = (LateEntity) t;
                if (entity.getStatus() == 200 && entity.isLateStatus()) {
                    new QAlertDialog()
                            .setCancelAble(false)
                            .setImg(QAlertDialog.IMG_CONFIRM)
                            .setBTN(QAlertDialog.BTN_ONEBUTTON)
                            .setTitleText("标记成功")
                            .setContentText("车费稍后退还")
                            .setSureClickListener(new QAlertDialog.OnDialogClick() {
                                @Override
                                public void onClick(QAlertDialog dialog) {
                                    if (passengerTravel != null) {
                                        Intent intent = new Intent(TravelDetail_PassengerActivity.this, TravelHistoryDetailActivity.class);
                                        intent.putExtra("ordersId", passengerTravel.getOrdersId());
                                        intent.putExtra("travelType", ConstantValue.TravelType.PASSENGER);
                                        startActivity(intent);
                                    }
                                    dialog.dismiss();
                                    finish();
                                }
                            }).show(getSupportFragmentManager(), "");

                } else {
                    onError("超过上车时间5分钟才可标记");
                }
            }
        });
    }

    private void showPay() {
        if (passengerTravel == null) {
            return;
        }
        if (passengerTravel.isCurrentTravelOrders()) {

            long timeA = passengerTravel.getNoPayOrdersVo().getNopaySign().getCreatetime();
            long timeB = passengerTravel.getNoPayOrdersVo().getTimeStamp();
            if ((timeA - timeB) / 1000 + 3 * 60 < 0) {
                showPayOvertimeDialog();
            } else {
                toPay();
            }
        } else {
            new QAlertDialog()
                    .setBTN(QAlertDialog.BTN_TWOBUTTON)
                    .setImg(QAlertDialog.IMG_ALERT)
                    .setRightText("去支付")
                    .setTitleText("您有未支付订单")
                    .setCancelClickListener(new QAlertDialog.OnDialogClick() {
                        @Override
                        public void onClick(QAlertDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .setSureClickListener(new QAlertDialog.OnDialogClick() {
                        @Override
                        public void onClick(QAlertDialog dialog) {
                            dialog.dismiss();
                            Intent intent = new Intent(TravelDetail_PassengerActivity.this, TravelDetail_PassengerActivity.class);
                            intent.putExtra("travelID", passengerTravel.getNoPayOrdersVo().getTravelId());
                            intent.putExtra("ordersID", passengerTravel.getNoPayOrdersVo().getOrdersTravelId());
                            startActivity(intent);
                        }
                    })
                    .show(getSupportFragmentManager(), "");
        }
    }

    private void toPay() {
        new PayDialog()
                .setListener(new PayDialog.PayDialogListener() {
                    @Override
                    public void paySuccess(String ordersId) {
                        TravelDetail_PassengerActivity.this.ordersID = ordersId;
                        if(payOvertimeDialog!=null&&payOvertimeDialog.isVisible()){
                            payOvertimeDialog.dismiss();
                        }
                        getData();
                    }

                    @Override
                    public void cancelledOrder() {
                        finish();
                    }

                    @Override
                    public void payOvertime(PayDialog dialog) {
                        dialog.dismiss();
                        showPayOvertimeDialog();
                    }
                })
                .show(getSupportFragmentManager(), "");
    }

    private boolean isStartPayCountDown = false;
    private CountDownTimer timer;
    private void countDown(long time) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(time, 999) {
            @Override
            public void onTick(long millisUntilFinished) {
                long d = millisUntilFinished / 1000;
                final String text;
                if (d > 1800) {
                    text = "时间错误,无法支付";
                } else if (d > 180) {
                    text = "座位已锁定，支付倒计时180+S";
                } else if (d > 0) {
                    text = "座位已锁定，支付倒计时" + d + "S";
                } else {
                    text = "支付超时";
                }
                tv_time.setText(text);
        }

        @Override
        public void onFinish () {
            showPayOvertimeDialog();
        }
    };
        timer.start();
}

    private QAlertDialog payOvertimeDialog;

    private synchronized void showPayOvertimeDialog() {
        if (payOvertimeDialog == null) {
            payOvertimeDialog = new QAlertDialog()
                    .setBTN(QAlertDialog.BTN_ONEBUTTON)
                    .setImg(QAlertDialog.IMG_ALERT)
                    .setTitleText("支付超时")
                    .setCancelAble(false)
                    .setSureClickListener(new QAlertDialog.OnDialogClick() {
                        @Override
                        public void onClick(QAlertDialog dialog) {
                            cancelTO(false);
                            dialog.dismiss();
                            finish();
                        }
                    });
            payOvertimeDialog.show(getSupportFragmentManager(), "");
        } else {
            if (!payOvertimeDialog.isVisible() && !payOvertimeDialog.isAdded() && !isFinishing()) {
                payOvertimeDialog.show(getSupportFragmentManager(), "");
            }
        }
    }

}
