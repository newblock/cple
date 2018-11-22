package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.DriverReleaseTimeDialog;
import com.qcx.mini.dialog.PassengerReleaseTimeDialog;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.dialog.ReleaseSuccessDialog;
import com.qcx.mini.dialog.ReminderSingleWheelDialog;
import com.qcx.mini.dialog.SingleWheelDialog;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.PriceEntity;
import com.qcx.mini.entity.PriceListEntity;
import com.qcx.mini.entity.ReleaseLineInfoEntity;
import com.qcx.mini.entity.ReleaseResultEntity;
import com.qcx.mini.entity.WheelIntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.GsonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.utils.mapUtils.MapUtil;
import com.qcx.mini.verify.VerifyException;
import com.qcx.mini.verify.VerifyUtil;
import com.qcx.mini.widget.calendar.DateEntity;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class ReleaseTravelActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener, RouteSearch.OnRouteSearchListener {
    public final static int RELEASE_SUCCESS = 20;
    private final int REQUEST_END_ADDRESS_CODE = 10;
    private final int REQUEST_START_ADDRESS_CODE = 11;
    private final int REQUEST_STRATEGY_CODE = 23;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient = null;
    private Marker startAddressMarker;
    private Marker endAddressMarker;
    private MapView mMapView = null;
    private AMap aMap;
    private boolean startMarkerisScreenCenter = false;
    private int startImgID;
    private int endImgID;
    private boolean isMove;
    private GeocodeSearch mGeocoderSearch;
    boolean needRegeocodeQuery = false;//判断地图状态变化是否由手势引起的
    private MapUtil mMapUtil;
    private ArrayList<Tip> waypoints;

    private int type;// 0 人找车，1车找人;

    private Tip startAddress;
    private Tip endAddress;
    private int seats;
    private int price;
    private long startTime;
    private List<DateEntity> dates;
    private int strategy = -1;
    private double redPacketPrice;

    private List<PriceEntity> priceList;

    @BindView(R.id.release_view_bottomView)
    View bottomView;
    @BindView(R.id.release_view_time)
    View v_time;
    @BindView(R.id.release_view_cTime)
    View v_cTime;
    @BindView(R.id.release_view_passenger)
    View v_passenger;
    @BindView(R.id.release_view_driver)
    View v_driver;

    @BindView(R.id.release_text_stratAddress)
    TextView tv_startAddress;
    @BindView(R.id.release_text_endAddress)
    TextView tv_endAddress;
    @BindView(R.id.release_text_seatNum)
    TextView tv_seatNum;
    @BindView(R.id.release_text_price)
    TextView tv_price;
    @BindView(R.id.release_text_cprice)
    TextView tv_cprice;
    @BindView(R.id.release_text_cStartTime)
    TextView tv_cTime;
    @BindView(R.id.release_text_startTime)
    TextView tv_time;
    @BindView(R.id.release_text_line)
    TextView tv_line;
    @BindView(R.id.release_text_manNum)
    TextView tv_manNum;
    @BindView(R.id.release_view_way_point_line)
    View v_line;
    @BindView(R.id.release_view_way_point_view)
    View v_wayPoint;
    @BindView(R.id.release_view_way_point_text)
    TextView tv_wayPoint;

    @Override
    public int getLayoutID() {
        return R.layout.activity_release_travel;
    }

    @OnClick(R.id.release_close)
    void close() {
        finish();
    }

    private boolean isLoading=false;
    @OnClick(R.id.release_submit)
    void submit() {
        if (isLoading){
            return;
        }
        try {
            VerifyUtil.verifyLogin();
            VerifyUtil.verifyStartAddress(startAddress);
            VerifyUtil.verifyEndAddress(endAddress);
            if (type == 1) {
                VerifyUtil.verifyDriverReleaseTime(dates, System.currentTimeMillis());
                VerifyUtil.verifyStrategy(strategy);
                VerifyUtil.verifySeats(seats);
            } else {
                VerifyUtil.verifyTime(startTime, System.currentTimeMillis());
                VerifyUtil.verifyPeoples(seats);
            }
            VerifyUtil.verifyPrice(price);

            Map<String, Object> params = new HashMap<>();
            String url;
            if (type == 0) {
                url = URLString.travelPassengerCreate;
                params.put("startTimes", new String[]{DateUtil.getTimeStr(startTime, "yyyy-MM-dd HH:mm:ss")});
                params.put("seatsNum", seats);
            } else {
                url = URLString.travelCreate;
                List<String> startTimes = new ArrayList<>();
                for (int i = 0; i < dates.size(); i++) {
                    startTimes.add(DateUtil.getTimeStr(dates.get(i).getMillion(), "yyyy-MM-dd HH:mm:ss"));
                    params.put("startTimes", startTimes);
                }
                params.put("seats", seats);
                params.put("strategy", strategy);
                params.put("waypoints", null);
                List<double[]> points = new ArrayList<>();
                List<String> pointsName = new ArrayList<>();
                if (waypoints != null) {
                    for (int i = 0; i < waypoints.size(); i++) {
                        Tip tip = waypoints.get(i);
                        if (tip != null) {
                            points.add(new double[]{tip.getPoint().getLongitude(), tip.getPoint().getLatitude()});
                            pointsName.add(tip.getName());
                        }
                    }
                }
                params.put("waypoints", points);
                params.put("waypointsAddress", pointsName);
            }

            params.put("token", User.getInstance().getToken());
            params.put("start", new double[]{startAddress.getPoint().getLongitude(), startAddress.getPoint().getLatitude()});
            params.put("end", new double[]{endAddress.getPoint().getLongitude(), endAddress.getPoint().getLatitude()});
            params.put("startAddress", startAddress.getName());
            params.put("endAddress", endAddress.getName());
            params.put("travelPrice", price);
            params.put("startCity", startAddress.getDistrict());
            params.put("endCity", endAddress.getDistrict());
            params.put("redPacketPrice", redPacketPrice);
            showLoadingDialog();
            isLoading=true;
            Request.post(url, params, new EntityCallback(ReleaseResultEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    hideLoadingDialog();
                    isLoading=false;
                    final ReleaseResultEntity data = (ReleaseResultEntity) t;
                    if (data.getStatus() == 200) {
                        ToastUtil.showToast("成功发布行程！");
                        if(type==0){
                            data.setTravelType(ConstantValue.TravelType.PASSENGER);
                        }else {
                            data.setTravelType(ConstantValue.TravelType.DRIVER);
                        }
//                        Intent intent = new Intent(ReleaseTravelActivity.this, MainActivity.class);
//                        intent.putExtra("release", RELEASE_SUCCESS);
                        EventBus.getDefault().post(EventStatus.SHOW_MAIN_RED_POINT);
                        if(data.getJoinedGroups()!=null&&data.getJoinedGroups().size()>0){
                            new ReleaseSuccessDialog()
                                    .setResult(data)
                                    .setClick(new ReleaseSuccessDialog.OnDialogClick() {
                                        @Override
                                        public void onDismiss() {
                                            finish();
                                        }
                                    })
                                    .show(getSupportFragmentManager(),"");
                        }else {
                            finish();
                        }

                    } else if (data.getStatus() == -122) {
                        ToastUtil.showToast("今日发布行程已达3次！");
                    } else if (data.getStatus() == -106) {//未进行芝麻认证
                        ToastUtil.showToast("未进行芝麻认证！");

                    } else if (data.getStatus() == -5) {
                        Intent intent = new Intent(ReleaseTravelActivity.this, AuthenticationActivity.class);
                        startActivity(intent);
                    } else if (data.getStatus() == -361) {
                        new QAlertDialog().setImg(QAlertDialog.IMG_ALERT)
                                .setBTN(QAlertDialog.BTN_ONEBUTTON)
                                .setTitleText("正在审核中，请稍后")
                                .setSureClickListener(new QAlertDialog.OnDialogClick() {
                                    @Override
                                    public void onClick(QAlertDialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .show(getSupportFragmentManager(), "");
                    }
                }

                @Override
                public void onError(String errorInfo) {
                    super.onError(errorInfo);
                    hideLoadingDialog();
                    isLoading=false;
                }
            });
        } catch (VerifyException e) {
            e.printStackTrace();
            ToastUtil.showToast(e.getMessage());
        }

    }

    @OnClick(R.id.release_view_stratAddress)
    void stratAddress() {
        if(type==0){
            Intent intent = new Intent(this, SetAddressActivity.class);
            intent.putExtra("inputHint", "请输入起点地址");
            startActivityForResult(intent, REQUEST_START_ADDRESS_CODE);
        }else {
            line();
        }
    }

    @OnClick(R.id.release_view_endAddress)
    void endAddress() {
        if(type==0){
            Intent intent = new Intent(this, SetAddressActivity.class);
            intent.putExtra("inputHint", "请输入终点地址");
            startActivityForResult(intent, REQUEST_END_ADDRESS_CODE);
        }else {
            line();
        }
    }
    @OnClick(R.id.release_view_way_point_view)
    void watPoint() {
        line();
    }

    @OnClick(R.id.release_view_line)
    void line() {
        Intent intent = new Intent(this, SelectRouteStrategyActivity.class);
        intent.putExtra("startTip", startAddress);
        intent.putExtra("endTip", endAddress);
        intent.putExtra("waypoints", waypoints);
        startActivityForResult(intent, REQUEST_STRATEGY_CODE);
    }

    @OnClick(R.id.release_view_price)
    void price() {
        if (startAddress == null) {
            ToastUtil.showToast("请输入起点位置");
            return;
        }
        if (endAddress == null) {
            ToastUtil.showToast("请输入终点位置");
            return;
        }

        if (priceList == null || priceList.size() < 1) {
            ToastUtil.showToast("正在获取价格信息");
            return;
        }
        PriceEntity.itemTextInfo = "元/位";
        new ReminderSingleWheelDialog<PriceEntity>()
                .setData(priceList)
                .setListener(new ReminderSingleWheelDialog.OnSelectPriceDialogListener<PriceEntity>() {
                    @Override
                    public void onRightClick(PriceEntity priceEntity, ReminderSingleWheelDialog dialog, int position) {
                        tv_price.setText(priceEntity.getPickerViewText());
                        price = (int) priceEntity.getPrice();
                        redPacketPrice = priceEntity.getRedPacketPrice();
                        dialog.dismiss();
                    }

                    @Override
                    public void onTopViewClick() {
                        ToastUtil.showToast("什么是黄牛红包");
                    }
                })
                .setPosition(price + "元/位")
                .show(getSupportFragmentManager(), "ReminderSingleWheelDialog");
    }

    @OnClick(R.id.release_view_passenger)
    void cprice() {
        if (startAddress == null) {
            ToastUtil.showToast("请输入起点位置");
            return;
        }
        if (endAddress == null) {
            ToastUtil.showToast("请输入终点位置");
            return;
        }

        if (seats < 1) {
            ToastUtil.showToast("请选择乘车人数");
            return;
        }

        if (priceList == null || priceList.size() < 1) {
            ToastUtil.showToast("正在获取价格信息");
            return;
        }
        PriceEntity.itemTextInfo = "";
        new ReminderSingleWheelDialog<PriceEntity>()
                .setData(priceList)
                .setListener(new ReminderSingleWheelDialog.OnSelectPriceDialogListener<PriceEntity>() {
                    @Override
                    public void onRightClick(PriceEntity priceEntity, ReminderSingleWheelDialog dialog, int position) {
                        tv_cprice.setText(priceEntity.getPickerViewText());
                        price = (int) priceEntity.getPrice();
                        redPacketPrice = priceEntity.getRedPacketPrice();
                        dialog.dismiss();
                    }

                    @Override
                    public void onTopViewClick() {
                        ToastUtil.showToast("什么是雷锋红包");
                    }
                })
                .show(getSupportFragmentManager(), "ReminderSingleWheelDialog");
    }

    @OnClick({R.id.release_view_cStartTime})
    void startCTime() {
        new PassengerReleaseTimeDialog()
                .setSelectTime(startTime < 10 ? System.currentTimeMillis() : startTime)
                .setLisenter(new PassengerReleaseTimeDialog.OnTimeSelectLisenter() {
                    @Override
                    public void date(long date, PassengerReleaseTimeDialog dialog, String text) {
                        startTime = date;
                        LogUtil.i(DateUtil.getTimeStr(startTime, null));
                        String[] time = text.split(" ");
                        if (time.length > 0) {
                            tv_cTime.setText(time[time.length - 1]);
                        } else {
                            tv_cTime.setText(text);
                        }
                        dialog.dismiss();
                        if (seats < 1) {
                            manNum();
                        }
                    }
                }).show(getSupportFragmentManager(), "setTimeDialog");
    }

    @OnClick(R.id.release_view_startTime)
    void startTime() {
        try {
            new DriverReleaseTimeDialog().setListener(new DriverReleaseTimeDialog.OnDatesSelectedListener() {
                @Override
                public void onSelected(List<DateEntity> dateEntities,DriverReleaseTimeDialog dialog) {
                    dates = dateEntities;
                    if (dates != null && dates.size() > 0) {
                        String text = DateUtil.getTimeStr(dateEntities.get(0).getMillion(), "HH:mm") + "(" + dates.size() + "天)";
                        tv_time.setText(text);
                        if (startAddress != null && endAddress != null && strategy == -1) {
                            line();
                        }
                    } else {
                        tv_time.setText("出发时间");
                    }
                    dialog.dismiss();
                }
            })
                    .setSelectedDays(dates)
                    .show(getSupportFragmentManager(), "DriverReleaseTimeDialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.release_view_seatNum)
    void seatNum() {
        new SingleWheelDialog<WheelIntEntity>()
                .setData(getSeatsData())
                .setListener(new SingleWheelDialog.OnSingleWheelDialogListener<WheelIntEntity>() {
                    @Override
                    public void onRightClick(WheelIntEntity wheelIntEntity, SingleWheelDialog dialog, int position) {
                        seats = wheelIntEntity.getData();
                        tv_seatNum.setText(wheelIntEntity.getPickerViewText());
                        dialog.dismiss();
                        if (price <= 0) {
                            price();
                        }
                    }
                })
                .show(getSupportFragmentManager(), "SingleWheelDialog seat");
    }

    @OnClick(R.id.release_view_manNum)
    void manNum() {
        new SingleWheelDialog<WheelIntEntity>()
                .setData(getPeopleData())
                .setListener(new SingleWheelDialog.OnSingleWheelDialogListener<WheelIntEntity>() {
                    @Override
                    public void onRightClick(WheelIntEntity wheelIntEntity, SingleWheelDialog dialog, int position) {
                        seats = wheelIntEntity.getData();
                        tv_manNum.setText(wheelIntEntity.getPickerViewText());
                        dialog.dismiss();
                        getPrice(true);
                    }
                })
                .show(getSupportFragmentManager(), "SingleWheelDialog seat");
    }

    @OnClick(R.id.release_view_locate)
    void locate() {
        startLocation();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            v_time.setVisibility(View.GONE);
            v_driver.setVisibility(View.GONE);
            v_cTime.setVisibility(View.VISIBLE);
            v_passenger.setVisibility(View.VISIBLE);
            startImgID = R.mipmap.icon_map_up;
            endImgID = R.mipmap.icon_map_down;

        } else {
            v_time.setVisibility(View.VISIBLE);
            v_driver.setVisibility(View.VISIBLE);
            v_cTime.setVisibility(View.GONE);
            v_passenger.setVisibility(View.GONE);
            startImgID = R.mipmap.icon_map_star_driver;
            endImgID = R.mipmap.icon_map_end_driver;
        }
        //获取地图控件引用
        mMapView = findViewById(R.id.release_map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (mMapView != null) {
            aMap = mMapView.getMap();
        } else {
            finish();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoLeftMargin(20000);
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    needRegeocodeQuery = true;
                    isMove = true;
                } else {
                    isMove = false;
                }
            }
        });
        mGeocoderSearch = new GeocodeSearch(ReleaseTravelActivity.this);
        mGeocoderSearch.setOnGeocodeSearchListener(this);
        aMap.setOnCameraChangeListener(this);
        mMapUtil = new MapUtil(this, aMap);
        mMapUtil.changeMapStyle();
        String lineInfo=getIntent().getStringExtra("lineInfo");
        showWayPoint();
        if(!TextUtils.isEmpty(lineInfo)){
            showLineInfo(lineInfo);
        }else {
            startMarkerisScreenCenter = true;
            startLocation();
            addMyLocationMarker();
        }
    }

    @Override
    public boolean setStatusBar() {
        UiUtils.setStatusBarTransparent(this);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.i("aaaaaaaaaaaaa onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i("aaaaaaaaaaaaa onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i("aaaaaaaaaaaaa onStop");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i("aaaaaaaaaaaaa onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i("aaaaaaaaaaaaa onDestroy");
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图

        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            LogUtil.i("aaaaaaaaaaaaa onSaveInstanceState3="+DateUtil.getTimeStr(System.currentTimeMillis(),null));
        }
        mMapView.onDestroy();
        aMap = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("aaaaaaaaaaaaa onResume");
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.i("aaaaaaaaaaaaa onPause="+DateUtil.getTimeStr(System.currentTimeMillis(),null));
        super.onPause();
        LogUtil.i("aaaaaaaaaaaaa onPause1="+DateUtil.getTimeStr(System.currentTimeMillis(),null));
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        LogUtil.i("aaaaaaaaaaaaa onPause2="+DateUtil.getTimeStr(System.currentTimeMillis(),null));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtil.i("aaaaaaaaaaaaa onSaveInstanceState="+DateUtil.getTimeStr(System.currentTimeMillis(),null));
        super.onSaveInstanceState(outState);
        LogUtil.i("aaaaaaaaaaaaa onSaveInstanceState1="+DateUtil.getTimeStr(System.currentTimeMillis(),null));
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
        LogUtil.i("aaaaaaaaaaaaa onSaveInstanceState2="+DateUtil.getTimeStr(System.currentTimeMillis(),null));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("aaaaaaaaaaaaa onActivityResult");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_START_ADDRESS_CODE:
                    startAddress = data.getParcelableExtra("tip");
                    if (startAddress != null) {
                        tv_startAddress.setText(startAddress.getName());
//                        pointToCenter(startAddress.getPoint().getLatitude(),startAddress.getPoint().getLongitude());
                        getCityFromTip(startAddress);
                        changeStartAddressMarker();
                        getPrice(true);
                    }
                    break;
                case REQUEST_END_ADDRESS_CODE:
                    endAddress = data.getParcelableExtra("tip");
                    if (endAddress != null) {
                        getCityFromTip(endAddress);
                        tv_endAddress.setText(endAddress.getName());
                        changeEndAddressMarker();
                        getPrice(true);
                    }
                    break;
                case REQUEST_STRATEGY_CODE:
                    strategy = data.getIntExtra("strategy", -1);
                    waypoints = data.getParcelableArrayListExtra("waypoints");
                    startAddress = data.getParcelableExtra("start");
                    if (startAddress != null) {
                        tv_startAddress.setText(startAddress.getName());
                    }
                    endAddress = data.getParcelableExtra("end");
                    if (endAddress != null) {
                        tv_endAddress.setText(endAddress.getName());
                    }

                    if (strategy == RouteSearch.DRIVING_SINGLE_DEFAULT) tv_line.setText("速度最快");
                    if (strategy == RouteSearch.DRIVING_SINGLE_SHORTEST) tv_line.setText("距离最短");
                    if (strategy == RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION)
                        tv_line.setText("少收费避拥堵");
                    if (strategy != -1 && seats == 0) {
//                        seatNum();
                    }
                    changeStartAddressMarker();
                    changeEndAddressMarker();
                    showWayPoint();
                    getPrice(true);
                    routeSearch();
                    break;
            }
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        startAddress = mMapUtil.getTip(regeocodeResult);
        tv_startAddress.setText(startAddress.getName());
        if (regeocodeResult != null) {
            RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
            if (address != null) {
                startAddress.setDistrict(address.getCity());
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (startMarkerisScreenCenter && !isMove && needRegeocodeQuery) {
            LatLng latLng = cameraPosition.target;
            RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 5, GeocodeSearch.AMAP);
            mGeocoderSearch.getFromLocationAsyn(regeocodeQuery);
        }
        needRegeocodeQuery = false;
    }


    //将startAddressMarker添加在屏幕中间
    private void addMyLocationMarker() {
        if (startAddressMarker == null) {
            bottomView.post(new Runnable() {
                @Override
                public void run() {
                    MarkerOptions markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), startImgID)));
                    startAddressMarker = aMap.addMarker(markerOptions);
                    int w = UiUtils.getPixelH() / 2;
                    int h = (UiUtils.getPixelV() - bottomView.getHeight()) / 2;
                    aMap.setPointToCenter(w, h);
                    startAddressMarker.setPositionByPixels(w, h);
                }
            });
        }
    }

    private void startLocation() {
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            mLocationOption.setOnceLocationLatest(true);
            mLocationClient = new AMapLocationClient(this);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.setLocationListener(mLocationListener);
        }
        new RxPermissions(this)
                .request("android.permission.ACCESS_COARSE_LOCATION")
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            mLocationClient.startLocation();
                        } else {
                            DialogUtil.showSetPermissionDialog(ReleaseTravelActivity.this, getSupportFragmentManager(), "定位权限");
                        }
                    }
                });
    }

    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LogUtil.i(aMapLocation.getLatitude() + "  " + aMapLocation.getLongitude() + " " + aMapLocation.getPoiName());
            LogUtil.i(aMapLocation.toStr());
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    startAddress = getTipFromAMapLocation(aMapLocation);
                    getCityFromTip(startAddress);
                    if (startMarkerisScreenCenter) {
                        pointToCenter(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        if (startAddress != null) tv_startAddress.setText(startAddress.getName());
                        else tv_startAddress.setHint("请输入您的起点");
                    } else {
                        tv_startAddress.setText(startAddress.getName());
                        changeStartAddressMarker();
                        routeSearch();
                    }
                } else {
                    Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    if(startMarkerisScreenCenter){
                        tv_startAddress.setHint("请输入您的起点");
                    }
                }
            }
        }
    };

    //移动某点到地图中心点
    private void pointToCenter(double lat, double lon) {
        LatLng latLng = new LatLng(lat, lon);
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng), new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMapUtil.setZoom(100);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void changeEndAddressMarker() {
        if (endAddress == null && endAddressMarker != null) {
            endAddressMarker.remove();
            return;
        }
        if (endAddressMarker != null) {
            endAddressMarker.setPosition(new LatLng(endAddress.getPoint().getLatitude(), endAddress.getPoint().getLongitude()));
        } else {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(endAddress.getPoint().getLatitude(), endAddress.getPoint().getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), endImgID)));
            endAddressMarker = aMap.addMarker(markerOptions);
        }
        if (startAddress != null) {
            showAllMarkers();
        }
    }

    private void changeStartAddressMarker() {
        if (startAddress == null && startAddressMarker != null) {
            startAddressMarker.remove();
            return;
        }
        if (startMarkerisScreenCenter) {
            if (startAddress.getPoint() != null)
                pointToCenter(startAddress.getPoint().getLatitude(), startAddress.getPoint().getLongitude());
        } else if (startAddressMarker != null) {
            startAddressMarker.setPosition(new LatLng(startAddress.getPoint().getLatitude(), startAddress.getPoint().getLongitude()));
        } else {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(startAddress.getPoint().getLatitude(), startAddress.getPoint().getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), startImgID)));
            startAddressMarker = aMap.addMarker(markerOptions);
        }
        if (endAddress != null) {
            showAllMarkers();
        }
    }

    private void showAllMarkers() {
        if (startMarkerisScreenCenter&&startAddressMarker!=null) {
            startAddressMarker.setPosition(new LatLng(startAddress.getPoint().getLatitude(), startAddress.getPoint().getLongitude()));
            startMarkerisScreenCenter = false;
        }
//        mMapUtil.showAllMarkers(250, startAddressMarker, endAddressMarker);
        List<Tip> tips=new ArrayList<>();
        tips.add(startAddress);
        tips.add(endAddress);
        if(waypoints!=null){
            tips.addAll(waypoints);
        }
        mMapUtil.showAllTip(250,null,tips);
    }

    private void getPrice(final boolean isInitPrice) {
        if(isInitPrice){
            priceList = null;
            price = 0;
            tv_cprice.setText("0.00");
            redPacketPrice = 0;
            if (type == 1) {
                tv_price.setText("票价");
            } else {
                tv_price.setText("车费（元）");
            }
        }
        if (startAddress != null && endAddress != null) {
            if (type == 0 && seats < 1) {
                return;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("start", new double[]{startAddress.getPoint().getLongitude(), startAddress.getPoint().getLatitude()});
            params.put("end", new double[]{endAddress.getPoint().getLongitude(), endAddress.getPoint().getLatitude()});
            params.put("role", type);
            if (type == 0) params.put("seats", seats);
            Request.post(URLString.publishPrice, params, new EntityCallback(PriceListEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    try {
                        PriceListEntity p = (PriceListEntity) t;
                        priceList = p.getPriceList();
                        if (isInitPrice&&type == 0) {
                            cprice();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String errorInfo) {

                }
            });
        }
    }

    private List<WheelIntEntity> getSeatsData() {
        List<WheelIntEntity> datas = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            datas.add(new WheelIntEntity(i + "个座位", i));
        }
        return datas;
    }

    private List<WheelIntEntity> getPeopleData() {
        List<WheelIntEntity> datas = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            datas.add(new WheelIntEntity(i + "人", i));
        }
        return datas;
    }

    private Tip getTipFromAMapLocation(AMapLocation amapLocation) {
        double latitude = amapLocation.getLatitude();
        double longitude = amapLocation.getLongitude();
        String address = amapLocation.getAoiName();
        if (TextUtils.isEmpty(address)) {
            address = amapLocation.getStreet() + amapLocation.getStreetNum();
        }
        if (TextUtils.isEmpty(address)) {
            address = amapLocation.getAddress();
        }
        if (!TextUtils.isEmpty(address)) {
            Tip tip = new Tip();
            tip.setName(address);
            tip.setPostion(new LatLonPoint(latitude, longitude));
            LogUtil.i("name="+tip.getName());
            return tip;
        }
        LogUtil.i("return null");
        return null;
    }

    private void getCityFromTip(final Tip tip) {
        if (tip == null || tip.getPoint() == null) {
            return;
        }
        GeocodeSearch geocodeSearch = new GeocodeSearch(ReleaseTravelActivity.this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (regeocodeResult != null) {
                    RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
                    if (address != null) {
                        tip.setDistrict(address.getCity());
                    }
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        LatLonPoint latLng = new LatLonPoint(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLng, 5, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

    private RouteSearch routeSearch;

    private void routeSearch() {
        if(type==0){
            return;
        }
        if (routeSearch == null) {
            routeSearch = new RouteSearch(this);
            routeSearch.setRouteSearchListener(this);
        }
        if (startAddress == null || endAddress == null || strategy == -1||type== 0) {
            return;
        }

        LatLonPoint dStart = new LatLonPoint(startAddress.getPoint().getLatitude(), startAddress.getPoint().getLongitude());
        LatLonPoint dEnd = new LatLonPoint(endAddress.getPoint().getLatitude(), endAddress.getPoint().getLongitude());
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(dStart, dEnd);

        List<LatLonPoint> points = new ArrayList<>();
        if(waypoints!=null){
            for (int i = 0; i < waypoints.size(); i++) {
                points.add(waypoints.get(i).getPoint());
            }
        }
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, strategy, points, null, "");
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(final DriveRouteResult driveRouteResult, int i) {
        aMap.clear();
        startAddressMarker = null;
        endAddressMarker = null;

        changeStartAddressMarker();
        changeEndAddressMarker();
        showWayPointMarker();
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mMapUtil.drawPath(driveRouteResult, 0 ,false);
                            showAllMarkers();
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


    private void showWayPoint() {
        if (type==1
                && waypoints != null
                &&waypoints.size()>0) {
            v_wayPoint.setVisibility(View.VISIBLE);
            v_line.setVisibility(View.GONE);
            StringBuilder stringBuilder = new StringBuilder();
            if (waypoints.size() > 1) {
                stringBuilder.append("经");
                stringBuilder.append(waypoints.size());
                stringBuilder.append("地：");
                for (int i = 0; i < waypoints.size(); i++) {
                    if (i < waypoints.size()) {
                        stringBuilder.append(waypoints.get(i).getName());
                        stringBuilder.append("、");
                    }
                }
            }else if(waypoints.size() == 1){
                stringBuilder.append(waypoints.get(0).getName());
                stringBuilder.append("、");
            }
            if(stringBuilder.length()>0){
                stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
                tv_wayPoint.setText(stringBuilder);
            }
        }else {
            v_wayPoint.setVisibility(View.GONE);
            v_line.setVisibility(View.VISIBLE);
        }
    }

    private void showWayPointMarker(){

        if (mMapUtil == null) return;

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
        } else if (waypoints!=null&&waypoints.size() == 1) {
            addMarker(waypoints.get(0), R.mipmap.way_point);
        }
    }

    private void addMarker(Tip tip, int img) {
        if (mMapUtil == null || tip == null) {
            return;
        }
        mMapUtil.addMarkerToMap(new double[]{tip.getPoint().getLongitude(), tip.getPoint().getLatitude()}, img);
    }

    private void showLineInfo(String lineInfo){
        try {
            ReleaseLineInfoEntity line= GsonUtil.create().fromJson(lineInfo,ReleaseLineInfoEntity.class);

            strategy =line.getStrategy();
            if(line.getWaypoints()!=null){
                if (waypoints == null) {
                    waypoints = new ArrayList<>();
                } else {
                    waypoints.clear();
                }
                if (line.getWaypoints() != null
                        && line.getWaypoints().size() > 0
                        && line.getWaypointsAddress() != null
                        && line.getWaypoints().size() == line.getWaypointsAddress().length) {
                    for (int i = 0; i < line.getWaypoints().size(); i++) {
                        Tip tip = new Tip();
                        tip.setPostion(new LatLonPoint(line.getWaypoints().get(i)[1], line.getWaypoints().get(i)[0]));
                        tip.setName(line.getWaypointsAddress()[i]);
                        waypoints.add(tip);
                    }
                }

            }
            startAddress = new Tip();
            startAddress.setPostion(new LatLonPoint(line.getStart()[1],line.getStart()[0]));
            startAddress.setName(line.getStartAddress());
            if (startAddress != null) {
                tv_startAddress.setText(startAddress.getName());
            }
            endAddress=new Tip();
            endAddress.setPostion(new LatLonPoint(line.getEnd()[1],line.getEnd()[0]));
            endAddress.setName(line.getEndAddress());
            if (endAddress != null) {
                tv_endAddress.setText(endAddress.getName());
            }

            if (strategy == RouteSearch.DRIVING_SINGLE_DEFAULT) tv_line.setText("速度最快");
            if (strategy == RouteSearch.DRIVING_SINGLE_SHORTEST) tv_line.setText("距离最短");
            if (strategy == RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION)
                tv_line.setText("少收费避拥堵");

            seats=line.getSeats();
            price=(int)line.getPrice();
            redPacketPrice=line.getRedPacketPrice();

            if (line.getType() == ConstantValue.TravelType.PASSENGER) {
                startTime=line.getStartTime();
                String time=DateUtil.formatDayInWeek(startTime)+DateUtil.getTimeStr(startTime,"HH点mm分");
                tv_cTime.setText(time);
                tv_manNum.setText(String.format(Locale.CHINA,"%d人",seats));
                tv_cprice.setText(String.format(Locale.CHINA,"%d",price));
            } else {
                strategy=line.getStrategy();
                dates=new ArrayList<>();
                DateEntity entity=new DateEntity();
                entity.setMillion(line.getStartTime());
                entity.setDate(getDate(line.getStartTime()));
                dates.add(entity);
                tv_time.setText(String.format(Locale.CHINA,"%s(1天)",DateUtil.getTimeStr(line.getStartTime(),"HH:mm")));
                tv_seatNum.setText(String.format(Locale.CHINA,"%d个座位",seats));
                tv_price.setText(String.format(Locale.CHINA,"%d元/位",price));
                routeSearch();
            }
            changeStartAddressMarker();
            changeEndAddressMarker();
            showWayPoint();
            getPrice(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getDate(long time){

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));

        return getValue(cal.get(cal.YEAR)) + "-" + getValue(cal.get(cal.MONTH) + 1) + "-" + getValue(cal.get(cal.DATE));
    }

    public String getValue(int num) {
        return String.valueOf(num > 9 ? num : ("0" + num));
    }
}
