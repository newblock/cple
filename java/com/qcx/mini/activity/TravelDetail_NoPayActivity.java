package com.qcx.mini.activity;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.Text;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.viewHolder.ItemTravelViewHolder;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.ItemsDialog;
import com.qcx.mini.dialog.NavigationDialog;
import com.qcx.mini.dialog.PayDialog;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.dialog.SubmitOrderDialog;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.SeckillEntity;
import com.qcx.mini.entity.TravelDetail_noPayEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.entity.UnOrderDriverTravelDetailEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.Navigation;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.utils.mapUtils.MapUtil;
import com.qcx.mini.utils.mapUtils.QuRoutOverlay;
import com.qcx.mini.widget.DragView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amap.api.services.core.AMapException.CODE_AMAP_OVER_DIRECTION_RANGE;
import static com.qcx.mini.ConstantValue.ReservedTravelType.RESERVED_TRAVEL_PICKUP;
import static com.qcx.mini.ConstantValue.ReservedTravelType.RESERVED_TRAVEL_STATION;
import static com.qcx.mini.utils.CommonUtil.getLatLonPints;

public class TravelDetail_NoPayActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, RouteSearch.OnRouteSearchListener {
    private final static int REQUEST_CODE_CHANGE_START_LOCATION = 1;
    private final static int REQUEST_CODE_CHANGE_END_LOCATION = 2;
    private final static int REQUEST_CODE_MY_START_CHANGE = 11;
    private final static int REQUEST_CODE_MY_END_CHANGE = 12;
    private final static int REQUEST_PASSENGER_INPUT_ADDRESS = 20;

    private Tip myStart;//我的起点
    private Tip myEnd;//我的终点
    private Polyline myStartLine;//起点到上车点的线路
    private Polyline myEndLine;//下车点到我的终点的线路
    private Polyline travelLine;//行程的线路

    private int startDistance = -1;//我的起点到行程的起点距离
    private int travelDistance = -1;//行程距离
    private int endDistance = -1;//行程的终点到我的终点的距离

    private long startDuration =0;//我的起点到行程的起点的用时
    private long travelDuration =0;//行程用时
    private long endDuration =0;//行程的终点到我的终点的用时


    private Marker rStartMarker;//乘客行程车主的起点  车主行程乘客的上车点
    private Marker rEndMarker;
    private Text startText;
    private Text endText;
    private Marker myStartMarker;
    private Marker myEndMarker;
    private Text myStartText;
    private Text myEndText;

    private TravelDetail_noPayEntity.TravelDetail travelDetail;
    private UnOrderDriverTravelDetailEntity.UnOrderTravelDetail travel;
    private int reserveTravelType;

    private boolean isLocationStartAddress = false;//是否定位起点
    private GeocodeSearch geocoderSearch;

    private int startImg;
    private int endImg;
    private RouteSearch routeSearch;
    private MapUtil mMapUtil;
    private double[] myLocation;


    private int travelType;//0：车主行程；1：乘客行程
    private long travelId;

    private AMap aMap;

    @BindView(R.id.travel_detail_no_pay_mapView)
    MapView mMapView;


    //乘客行程的UI
    @BindView(R.id.travel_detail_no_pay_passenger_travel_layout)
    LinearLayout ll_passengerTravel;
    @BindView(R.id.travel_detail_no_pay_change_content_view)
    View v_passengerTravelView;
    @BindView(R.id.travel_detail_no_pay_passenger_travel_close_img)
    ImageView iv_PCloseTravel;
    @BindView(R.id.travel_detail_no_pay_change_dragView)
    DragView mDragView;
    @BindView(R.id.travel_detail_no_pay_icon)
    ImageView iv_icon;
    @BindView(R.id.travel_detail_no_pay_name)
    TextView tv_name;
    @BindView(R.id.travel_detail_no_pay_info)
    TextView tv_info;
    @BindView(R.id.travel_detail_no_pay_sex)
    ImageView iv_sex;
    @BindView(R.id.travel_detail_no_pay_dingzuo)
    TextView tv_dingzuo;
    @BindView(R.id.travel_detail_no_pay_passenger_travel_view)
    View v_passengerTravel;

    @BindView(R.id.item_travel_start_time)
    TextView tv_PStartTime;
    @BindView(R.id.item_travel_startAddress)
    TextView tv_PStartAddress;
    @BindView(R.id.item_travel_endAddress)
    TextView tv_PEndAddress;
    @BindView(R.id.item_travel_peoples_view)
    TextView tv_PPeopleView;
    @BindView(R.id.item_travel_peoples_text)
    TextView tv_PPeopleText;
    @BindView(R.id.item_travel_price)
    TextView tv_PPrice;
    @BindView(R.id.item_travel_price_type)
    TextView tv_PPriceType;

    @BindView(R.id.travel_detail_no_pay_start_text_p)
    TextView tv_PMyStart;
    @BindView(R.id.travel_detail_no_pay_start_distance_text_p)
    TextView tv_PStartDistance;
    @BindView(R.id.travel_detail_no_pay_up_text_p)
    TextView tv_PUp;
    @BindView(R.id.travel_detail_no_pay_down_text_p)
    TextView tv_PDwon;

    @BindView(R.id.travel_detail_no_pay_end_text_p)
    TextView tv_PMyEnd;
    @BindView(R.id.travel_detail_no_pay_end_distance_text_p)
    TextView tv_PEndDistance;
    @BindView(R.id.travel_detail_no_pay_end_extra_distance_text)
    TextView tv_PAllDistance;
    @BindView(R.id.travel_detail_no_pay_end_extra_distance_view)
    View v_PAllDistance;
    @BindView(R.id.travel_detail_no_pay_passenger_status_seckill_view)
    View v_seckill;
    @BindView(R.id.travel_detail_no_pay_passenger_status_no_seckill_view)
    View v_noSeckill;

    //车主行程
    @BindView(R.id.travel_detail_no_pay_driver_travel_dragView)
    DragView dv_DDragView;
    @BindView(R.id.travel_detail_no_pay_driver_travel_view)
    LinearLayout ll_DTravelInfo;
    @BindView(R.id.travel_detail_no_pay_driver_travel_view_1)
    View v_DTravel1;
    @BindView(R.id.travel_detail_no_pay_driver_travel_view_2)
    View v_DTravel2;
    @BindView(R.id.travel_detail_no_pay_driver_travel_distance_view)
    View v_DDistance;
    @BindView(R.id.travel_detail_no_pay_driver_travel_type_view)
    LinearLayout ll_DType;
    @BindView(R.id.travel_detail_no_pay_driver_travel_type_1_describe_text)
    TextView tv_DType1Des;
    @BindView(R.id.travel_detail_no_pay_driver_travel_type_line)
    View v_DtypeLine;
    @BindView(R.id.travel_detail_no_pay_driver_travel_type_1)
    TextView tv_DType1;
    @BindView(R.id.travel_detail_no_pay_driver_travel_type_2)
    TextView tv_DType2;
    @BindView(R.id.travel_detail_no_pay_driver_travel_type_1_line)
    View v_DType1Line;
    @BindView(R.id.travel_detail_no_pay_driver_travel_type_2_line)
    View v_DType2Line;
    @BindView(R.id.travel_detail_no_pay_driver_travel_icon)
    ImageView iv_DIcon1;
    @BindView(R.id.travel_detail_no_pay_driver_travel_icon_bigger)
    ImageView iv_DIcon2;
    @BindView(R.id.travel_detail_no_pay_driver_travel_name)
    TextView tv_DName;
    @BindView(R.id.travel_detail_no_pay_driver_travel_sex)
    ImageView iv_DSex;
    @BindView(R.id.travel_detail_no_pay_driver_travel_info1)
    TextView tv_DInfo1;
    @BindView(R.id.travel_detail_no_pay_driver_travel_info2)
    TextView tv_Dinfo2;
    @BindView(R.id.travel_detail_no_pay_driver_travel_my_startAddress)
    TextView tv_DMyStartAddress;
    @BindView(R.id.travel_detail_no_pay_driver_travel_up_distance)
    TextView tv_DUpDistance;
    @BindView(R.id.travel_detail_no_pay_driver_travel_up)
    TextView tv_DUp;
    @BindView(R.id.travel_detail_no_pay_driver_travel_down)
    TextView tv_DDown;
    @BindView(R.id.travel_detail_no_pay_driver_travel_end_distance)
    TextView tv_DMyEndDistance;
    @BindView(R.id.travel_detail_no_pay_driver_travel_my_endAddress)
    TextView tv_DMyEndAddress;
    @BindView(R.id.travel_detail_no_pay_driver_travel_ticket_price)
    TextView tv_DTicketPrice;
    @BindView(R.id.travel_detail_no_pay_driver_travel_extra_distance)
    TextView tv_DExtraDistance;
    @BindView(R.id.travel_detail_no_pay_driver_travel_no_address)
    View v_DNoAddress;
    @BindView(R.id.travel_detail_no_pay_driver_travel_recommend_start_time)
    TextView v_DRecommendStartTime;
    @BindView(R.id.travel_detail_no_pay_driver_travel_recommend_end_time)
    TextView v_DRecommendEndTime;

    private ItemTravelViewHolder dTravelViewHolder;

//    private TextView tv_DTravelStartTime,tv_DTravelStartAddress,tv_DTravelEndAddress,tv_DTravelPrice;
//    private ImageView[] iv_DTravelHeads;

    @OnClick(R.id.travel_detail_no_pay_p_navigation)
    void onPNavigation() {
        if (travel != null) {
            new NavigationDialog()
                    .setLeftEnd(travel.getStart())
                    .setLeftEndtLocationName(travel.getStartAddress())
                    .setRightEnd(travel.getEnd())
                    .setRightEndLocationName(travel.getEndAddress())
                    .show(getSupportFragmentManager(), "");
        }
    }

    boolean isShowDriverTravel = false;
    boolean isShaowPassengerTravel = true;

    @OnClick({R.id.travel_detail_no_pay_passenger_travel_head})
    void onPassengerTravelHead() {//车主行程的显示隐藏
        LogUtil.i("TravelDetail_NoPayActivity method  onPassengerTravelHead");
        if (isShaowPassengerTravel) {
            v_passengerTravel.setVisibility(View.GONE);
            iv_PCloseTravel.setRotation(180);
        } else {
            v_passengerTravel.setVisibility(View.VISIBLE);
            iv_PCloseTravel.setRotation(0);
        }
        isShaowPassengerTravel = !isShaowPassengerTravel;
    }

    @OnClick({R.id.travel_detail_no_pay_driver_travel_view_1, R.id.travel_detail_no_pay_driver_travel_head_view})
    void showDriverTravelInfo() {//车主行程的显示隐藏
        LogUtil.i("TravelDetail_NoPayActivity method  showDriverTravelInfo");
        if (isShowDriverTravel) {
            v_DTravel1.setVisibility(View.VISIBLE);
            v_DTravel2.setVisibility(View.GONE);
        } else {
            v_DTravel1.setVisibility(View.GONE);
            v_DTravel2.setVisibility(View.VISIBLE);
        }
        isShowDriverTravel = !isShowDriverTravel;
    }

    @OnClick({R.id.travel_detail_no_pay_icon, R.id.travel_detail_no_pay_driver_travel_icon, R.id.travel_detail_no_pay_driver_travel_icon_bigger})
    void onIconClick() {//头像的点击事件
        LogUtil.i("TravelDetail_NoPayActivity method  onIconClick");
        if (travel != null) {
            Intent intent = new Intent(this, UserInfoActivity.class);
            intent.putExtra("phone", travel.getPhone());
            startActivity(intent);
        }
    }

    @OnClick({R.id.travel_detail_no_pay_call, R.id.travel_detail_no_pay_driver_travel_call_1, R.id.travel_detail_no_pay_driver_travel_call_2})
    void call() {
        LogUtil.i("TravelDetail_NoPayActivity method  call");
        if (travel != null) {
            DialogUtil.call(this, new String[]{String.valueOf(travel.getPhone())});
        }
    }

    private int type=0;
    @OnClick(R.id.travel_detail_no_pay_driver_travel_type_1)
    void type1() {//站点乘客
        LogUtil.i("TravelDetail_NoPayActivity method  type1");
        type=0;
        reserveTravelType = RESERVED_TRAVEL_STATION;
        dv_DDragView.setBottomHideHeight(UiUtils.getSize(303));
        v_DDistance.setVisibility(View.VISIBLE);
        tv_DType1Des.setVisibility(View.VISIBLE);
        v_DtypeLine.setVisibility(View.VISIBLE);
        tv_DType1.setTextColor(0xFF232426);
        v_DType1Line.setVisibility(View.VISIBLE);
        tv_DType2.setTextColor(0xFF939499);
        v_DType2Line.setVisibility(View.GONE);
        tv_DExtraDistance.setVisibility(View.GONE);
        if (travel != null) {
            tv_DTicketPrice.setText(String.format(Locale.CHINA, "%s元/座", CommonUtil.formatPrice(travel.getTravelPrice(), 1)));
        }
        initMarker();
        if (myStartMarker != null) {
            myStartMarker.setVisible(true);
        }
        if (myEndMarker != null) {
            myEndMarker.setVisible(true);
        }
        if (myStartText != null) {
            myStartText.setVisible(true);
        }
        if (myEndText != null) {
            myEndText.setVisible(true);
        }
    }

    @OnClick(R.id.travel_detail_no_pay_driver_travel_type_2)
    void type2() {//请求接送
        LogUtil.i("TravelDetail_NoPayActivity method  type2");
        type=1;
        reserveTravelType = RESERVED_TRAVEL_PICKUP;
        dv_DDragView.setBottomHideHeight(UiUtils.getSize(143));
        v_DDistance.setVisibility(View.GONE);
        tv_DType1Des.setVisibility(View.GONE);
        v_DtypeLine.setVisibility(View.GONE);
        tv_DType1.setTextColor(0xFF939499);
        v_DType1Line.setVisibility(View.GONE);
        tv_DType2.setTextColor(0xFF232426);
        v_DType2Line.setVisibility(View.VISIBLE);
        tv_DExtraDistance.setVisibility(View.VISIBLE);
        if (travel != null) {
            tv_DTicketPrice.setText(String.format(Locale.CHINA, "%s元/座+%s元接送费", CommonUtil.formatPrice(travel.getTravelPrice(), 1), CommonUtil.formatPrice(travel.getExtraMoney(), 1)));
        }
        initMarker();
        if (myStartMarker != null) {
            myStartMarker.setVisible(false);
        }
        if (myEndMarker != null) {
            myEndMarker.setVisible(false);
        }
        if (myStartText != null) {
            myStartText.setVisible(false);
        }
        if (myEndText != null) {
            myEndText.setVisible(false);
        }
    }

    double i = 0.001;

    @OnClick(R.id.travel_detail_no_pay_driver_travel_location)
    void dLocation() {//
        LogUtil.i("TravelDetail_NoPayActivity method  dLocation");
        if (travel == null) {
            return;
        }
        int space = UiUtils.getSize(40);
        int bottomSpace;
        double[][] locations=getShowMapSpace();

        if (dv_DDragView.getMoveType() == DragView.MoveType.TOP) {
            bottomSpace = (ll_DTravelInfo.getHeight() - dv_DDragView.getTopBottomHideHeight());
        } else {
            bottomSpace = (ll_DTravelInfo.getHeight() - dv_DDragView.getBottomHideHeight());
        }
        if(bottomSpace<UiUtils.getPixelV()*3/4&&locations!=null){
//            mMapUtil.showAllLine(travel.getStart(), travel.getEnd(), space, space, space, bottomSpace+space/2, null);
            mMapUtil.showAllLine(locations[0], locations[1], space, space, space, bottomSpace+space/2, null);
        }
    }

    @OnClick(R.id.travel_detail_no_pay_driver_travel_submit)
    void dSubmit() {
        LogUtil.i("TravelDetail_NoPayActivity method  dSubmit");
        if (travel == null) {
            return;
        }
        new SubmitOrderDialog()
                .setTravel(travel)
                .setReserveTravelType(reserveTravelType)
                .setTravelId(travelId)
                .setPayListener(payListener)
                .show(getSupportFragmentManager(), "");
    }

    @OnClick({R.id.travel_detail_no_pay_driver_travel_no_address})
    void dInputAddress() {//
        LogUtil.i("TravelDetail_NoPayActivity method  dInputAddress");
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("isOnlyInput", true);
        intent.putExtra("start", myStart);
        intent.putExtra("end", myEnd);
        startActivityForResult(intent, REQUEST_PASSENGER_INPUT_ADDRESS);
    }

    @OnClick(R.id.travel_detail_no_pay_driver_travel_up_distance)
    void dNavStart() {//
        LogUtil.i("TravelDetail_NoPayActivity method  dNavStart");
        if (travel != null && myStart != null) {
            double[] start = travel.getMatchStart();
            double[] end = travel.getRecommendStartLocation();
            navigation(start, travel.getMatchStartAddress(), end, travel.getRecommendEndAddress(), false);
        }
    }

    @OnClick(R.id.travel_detail_no_pay_driver_travel_end_distance)
    void dNavEnd() {//
        LogUtil.i("TravelDetail_NoPayActivity method  dNavEnd");
        if (travel != null && myEnd != null) {
            double[] start = travel.getRecommendEndLocation();
            double[] end = travel.getMatchEnd();
            navigation(start, travel.getRecommendEndAddress(), end, travel.getMatchEndAddress(), false);
        }
    }

    @OnClick(R.id.travel_detail_no_pay_driver_travel_up)
    void dChangeStart() {
        LogUtil.i("TravelDetail_NoPayActivity method  dChangeStart");
        if (travel != null) {
            changeLocation(REQUEST_CODE_CHANGE_START_LOCATION);
        }
    }

    @OnClick(R.id.travel_detail_no_pay_driver_travel_down)
    void dChangeEnd() {//
        LogUtil.i("TravelDetail_NoPayActivity method  dChangeEnd");
        if (travel != null) {
            changeLocation(REQUEST_CODE_CHANGE_END_LOCATION);
        }
    }

    @OnClick({R.id.travel_detail_no_pay_driver_travel_my_endAddress_view, R.id.travel_detail_no_pay_end_p})
    void changeMyEnd() {
        LogUtil.i("TravelDetail_NoPayActivity method  changeMyEnd");
        Intent intent = new Intent(this, SetAddressActivity.class);
        intent.putExtra("inputHint", "请输入终点");
        intent.putExtra("home_company_view", false);
        startActivityForResult(intent, REQUEST_CODE_MY_END_CHANGE);
    }

    @OnClick({R.id.travel_detail_no_pay_driver_travel_my_startAddress_view, R.id.travel_detail_no_pay_start_p})
    void changeMyStart() {
        LogUtil.i("TravelDetail_NoPayActivity method  changeMyStart");
        Intent intent = new Intent(this, SetAddressActivity.class);
        intent.putExtra("inputHint", "请输入起点");
        intent.putExtra("home_company_view", false);
        startActivityForResult(intent, REQUEST_CODE_MY_START_CHANGE);
    }

    @OnClick(R.id.travel_detail_no_pay_location)
    void showAllLine() {
        LogUtil.i("TravelDetail_NoPayActivity method  showAllLine");
        if (travel == null) {
            return;
        }

        int space = UiUtils.getSize(40);
        int bottomSpace;
        double[][] locations=getShowMapSpace();

        if (mDragView.getMoveType() == DragView.MoveType.TOP) {
            bottomSpace = (ll_passengerTravel.getHeight() - mDragView.getTopBottomHideHeight());
        } else {
            bottomSpace = (ll_passengerTravel.getHeight() - mDragView.getBottomHideHeight());
        }
        if(bottomSpace<UiUtils.getPixelV()*3/4&&locations!=null){
            mMapUtil.showAllLine(locations[0], locations[1], space*2, space, space, bottomSpace, null);
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(final DriveRouteResult driveRouteResult, int rCode) {
        LogUtil.i("TravelDetail_NoPayActivity method  onDriveRouteSearched");
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    final DrivePath drivePath = driveRouteResult.getPaths()
                            .get(0);
                    travelDistance = (int) drivePath.getDistance();
                    travelDuration=drivePath.getDuration();
                    showDriverTravelDistance();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (travelLine != null) {
                                travelLine.remove();
                            }
//                            travelLine = QuRoutOverlay.drawPath(aMap, QuRoutOverlay.getPolylineOptions(QuRoutOverlay.getLatLngs(drivePath), 0xFF499EF8));
                            travelLine = QuRoutOverlay.drawPath(aMap, QuRoutOverlay.getPolylineOptions(QuRoutOverlay.getLatLngs(drivePath)));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchEndLine();
                                    searchStartLine();
                                }
                            });
                            if(travelType==ConstantValue.TravelType.DRIVER){
                                dLocation();
                            }else {
                                showAllLine();
                            }
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

    @Override
    public void onMyLocationChange(android.location.Location location) {
        //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (isLocationStartAddress) {
                        LogUtil.i("TravelDetail_NoPayActivity method  onMyLocationChange onRegeocodeSearched");
                        myStart = mMapUtil.getTip(regeocodeResult);
                        if (myStart != null) {
                            isLocationStartAddress = false;
                            tv_PMyStart.setText(myStart.getName());
                            if (travel != null && travel.getStart() != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchStartLine();
                                    }
                                });
                            }
                        }
                    }

                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
        }
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(location.getLatitude(), location.getLongitude()), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @OnClick(R.id.travel_detail_no_pay_icon)
    void iconClick() {
        LogUtil.i("TravelDetail_NoPayActivity method  iconClick");
        if (travelDetail == null) return;
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("phone", travelDetail.getOwnerPhone());
        startActivity(intent);
    }

    @OnClick(R.id.travel_detail_no_pay_mapView_back)
    void back() {
        LogUtil.i("TravelDetail_NoPayActivity method  back");
        finish();
    }

    @OnClick(R.id.travel_detail_no_pay_dingzuo_view)
    void order() {
        LogUtil.i("TravelDetail_NoPayActivity method  order");
        if (travel == null) {
            return;
        }
        try {
            if (String.valueOf(travel.getPhone()).equals(User.getInstance().getPhoneNumber())) {
                ToastUtil.showToast("不能抢自己的行程");
            } else {
                new QAlertDialog()
                        .setImg(QAlertDialog.IMG_ALERT)
                        .setBTN(QAlertDialog.BTN_TWOBUTTON)
                        .setTitleText("抢单并接送乘客")
                        .setSureClickListener(new QAlertDialog.OnDialogClick() {
                            @Override
                            public void onClick(QAlertDialog dialog) {
                                driverSeckill();
                                dialog.dismiss();
                            }
                        })
                        .show(getSupportFragmentManager(), "");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i("TravelDetail_NoPayActivity method  onStart");
        LogUtil.i("noPay_activity=================== onStart" + DateUtil.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.i("TravelDetail_NoPayActivity method  onRestart");
        LogUtil.i("noPay_activity=================== onRestart" + DateUtil.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        LogUtil.i("TravelDetail_NoPayActivity method  onDestroy");
        mMapView.onDestroy();
        if (aMap != null) aMap = null;
        LogUtil.i("noPay_activity=================== onDestroy" + DateUtil.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("TravelDetail_NoPayActivity method  onResume");
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        LogUtil.i("noPay_activity=================== onResume" + DateUtil.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i("TravelDetail_NoPayActivity method  onPause");
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        LogUtil.i("noPay_activity=================== onPause" + DateUtil.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i("TravelDetail_NoPayActivity method  onStop");
        LogUtil.i("noPay_activity=================== onStop" + DateUtil.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.i("TravelDetail_NoPayActivity method  onSaveInstanceState");
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    void driverSeckill() {
        LogUtil.i("TravelDetail_NoPayActivity method  driverSeckill");
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("passengerTravelId", travelId);
        Request.post(URLString.driverSeckill, params, new EntityCallback(SeckillEntity.class) {
            @Override
            public void onSuccess(Object t) {
                SeckillEntity seckillEntity = (SeckillEntity) t;
                if (seckillEntity.getStatus() == -353) {
                    finishActivity(SearchActivity.class.getSimpleName());
                    Intent intent = new Intent(TravelDetail_NoPayActivity.this, TravelDetail_DriverActivity.class);
                    intent.putExtra("travelID", seckillEntity.getTravelDetail().getTravelId());
                    startActivity(intent);
                    finish();
                }
                if (seckillEntity.getStatus() == -361) {
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
                } else if (seckillEntity.getStatus() == -5) {
                    Intent intent = new Intent(TravelDetail_NoPayActivity.this, AuthenticationActivity.class);
                    startActivity(intent);
                }
                onError(seckillEntity.getDetail());
            }

            @Override
            public void onError(String errorInfo) {
                if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_detail__no_pay;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        LogUtil.i("TravelDetail_NoPayActivity method  initView");
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        mMapUtil = new MapUtil(this, aMap);
        mMapUtil.iniMap();
        mMapUtil.changeMapStyle();

        if (routeSearch == null) {
            routeSearch = new RouteSearch(this);
            routeSearch.setRouteSearchListener(this);
        }

        travelId = getIntent().getLongExtra("travelId", 0);
        travelType = getIntent().getIntExtra("travelType", 0);
        myStart = getIntent().getParcelableExtra("start");
        myEnd = getIntent().getParcelableExtra("end");

        if (travelType == 0) {//车主行程
            startImg = R.mipmap.icon_map_up;
            endImg = R.mipmap.icon_map_down;
            tv_dingzuo.setText("订座");
            v_passengerTravelView.setVisibility(View.GONE);
            dv_DDragView.setVisibility(View.VISIBLE);
            dv_DDragView.setScrollListener(new DragView.OnScrollListener() {
                @Override
                public void finish(boolean statusChanged) {
//                    if(statusChanged){
                        dLocation();
//                    }
                }
            });
            LayoutTransition lt = new LayoutTransition();
            lt.setDuration(200);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "alpha", 1f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0).
                    setDuration(lt.getDuration(LayoutTransition.DISAPPEARING));
            lt.setAnimator(LayoutTransition.DISAPPEARING, animator2);

            ll_DTravelInfo.setLayoutTransition(lt);
            ll_DType.setLayoutTransition(lt);
        } else {
            LayoutTransition lt = new LayoutTransition();
            lt.setDuration(200);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "alpha", 1f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0).
                    setDuration(lt.getDuration(LayoutTransition.DISAPPEARING));
            lt.setAnimator(LayoutTransition.DISAPPEARING, animator2);
            ll_passengerTravel.setLayoutTransition(lt);
            startImg = R.mipmap.icon_map_star_driver;
            endImg = R.mipmap.icon_map_end_driver;
            tv_dingzuo.setText("抢单");
            v_passengerTravelView.setVisibility(View.VISIBLE);
            dv_DDragView.setVisibility(View.GONE);
            mDragView.setScrollListener(new DragView.OnScrollListener() {
                @Override
                public void finish(boolean statusChanged) {
                    showAllLine();
                }
            });
        }
        isLocationStartAddress = false;
        mMapUtil.location(this, true, 0, false);
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("TravelDetail_NoPayActivity method  onActivityResult");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHANGE_START_LOCATION:
                    Tip tip = data.getParcelableExtra("location");
                    if (tip != null) {
                        travel.setRecommendStartLocation(new double[]{tip.getPoint().getLongitude(), tip.getPoint().getLatitude()});
                        travel.setRecommendStartAddress(tip.getName());
                        tv_DUp.setText(tip.getName());
                        mMapUtil.moveMorker(rStartMarker, travel.getRecommendStartLocation());
                        searchStartLine();
                    }
                    break;
                case REQUEST_CODE_CHANGE_END_LOCATION:
                    Tip tip1 = data.getParcelableExtra("location");
                    if (tip1 != null) {
                        travel.setRecommendEndLocation(new double[]{tip1.getPoint().getLongitude(), tip1.getPoint().getLatitude()});
                        travel.setRecommendEndAddress(tip1.getName());
                        tv_DDown.setText(tip1.getName());
                        mMapUtil.moveMorker(rEndMarker, travel.getRecommendEndLocation());
                        searchEndLine();
                    }
                    break;
                case REQUEST_CODE_MY_START_CHANGE:
                    myStart = data.getParcelableExtra("tip");
                    if (myStart != null) {
                        tv_PMyStart.setText(myStart.getName());
                        getData();
                    }
                    break;
                case REQUEST_CODE_MY_END_CHANGE:
                    myEnd = data.getParcelableExtra("tip");
                    if (myEnd != null) {
                        tv_PMyEnd.setText(myEnd.getName());
                        getData();
                    }
                    break;
                case REQUEST_PASSENGER_INPUT_ADDRESS:
                    myEnd = data.getParcelableExtra("end");
                    myStart = data.getParcelableExtra("start");
                    getData();
                    break;
            }
        }
    }

    @Override
    public boolean setStatusBar() {
        LogUtil.i("TravelDetail_NoPayActivity method  setStatusBar");
        UiUtils.setStatusBarTransparent(this);
        UiUtils.setStatusBarLightMode(this, Color.TRANSPARENT);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.i("TravelDetail_NoPayActivity method  onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
        }
        return super.onKeyDown(keyCode, event);
    }

    void getData() {
        LogUtil.i("TravelDetail_NoPayActivity method  getData");
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        params.put("type", travelType);
        if (myStart != null && myStart.getPoint() != null) {
            params.put("start", new double[]{myStart.getPoint().getLongitude(), myStart.getPoint().getLatitude()});
        }
        if (myEnd != null && myEnd.getPoint() != null) {
            params.put("end", new double[]{myEnd.getPoint().getLongitude(), myEnd.getPoint().getLatitude()});
        }
        Request.post(URLString.getTravelDetail, params, new EntityCallback(UnOrderDriverTravelDetailEntity.class) {
            @Override
            public void onSuccess(Object t) {
                UnOrderDriverTravelDetailEntity entity = (UnOrderDriverTravelDetailEntity) t;
                travel = entity.getTravelDetail();
                showTravelLine();
                initUI();
            }
        });
    }

    private void initUI() {
        LogUtil.i("TravelDetail_NoPayActivity method  initUI");
        if (travelType == ConstantValue.TravelType.PASSENGER) {
            initPassengerTravelUI();
        } else {
            initDriverTravelUI();
        }
    }

    private void initPassengerTravelUI() {
        LogUtil.i("TravelDetail_NoPayActivity method  initPassengerTravelUI");
        if (travel == null) return;
        Picasso.with(this)
                .load(travel.getPicture())
                .error(R.mipmap.img_me)
                .into(iv_icon);
        tv_name.setText(travel.getNickName());
        String info = travel.getCar() + travel.getLastTimeOnlineTxt();
        tv_info.setText(info);
        switch (travel.getSex()) {
            case ConstantValue.SexType.SECRECY:
                iv_sex.setVisibility(View.GONE);
                break;
            case ConstantValue.SexType.MAN:
                iv_sex.setVisibility(View.VISIBLE);
                iv_sex.setImageResource(R.mipmap.img_men);
                break;
            case ConstantValue.SexType.WOMAN:
                iv_sex.setVisibility(View.VISIBLE);
                iv_sex.setImageResource(R.mipmap.img_women);
                break;
            default:
                iv_sex.setVisibility(View.GONE);
                break;
        }
        if(false){//是否被秒杀
            v_seckill.setVisibility(View.VISIBLE);
            v_noSeckill.setVisibility(View.GONE);
        }else {
            v_seckill.setVisibility(View.GONE);
            v_noSeckill.setVisibility(View.VISIBLE);
        }

        tv_PStartTime.setText(travel.getStartTimeTxt());
        tv_PStartAddress.setText(travel.getStartAddress());
        tv_PEndAddress.setText(travel.getEndAddress());
        tv_PPeopleView.setVisibility(View.VISIBLE);
        tv_PPeopleText.setVisibility(View.VISIBLE);
        tv_PPeopleText.setText(String.valueOf(travel.getSeats()));
        tv_PPrice.setText(CommonUtil.formatPrice(travel.getTravelPrice(), 1));
        tv_PPriceType.setText("元");

        tv_PUp.setText(travel.getStartAddress());
        tv_PDwon.setText(travel.getEndAddress());
        initMyStartAndEndAddressFromNet();
        if (myStart != null) {
            tv_PMyStart.setText(myStart.getName());
        }
        if (myEnd != null) {
            tv_PMyEnd.setText(myEnd.getName());
        }
    }

    private void initDriverTravelUI() {
        LogUtil.i("TravelDetail_NoPayActivity method  initDriverTravelUI");
        if (travel == null) {
            return;
        }
        if (dTravelViewHolder == null) {
            dTravelViewHolder = new ItemTravelViewHolder(findViewById(R.id.travel_detail_no_pay_driver_travel_travel));
        }
        dTravelViewHolder.bindData(getTravel());
        Picasso.with(this)
                .load(travel.getPicture())
                .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                .placeholder(R.mipmap.img_me)
                .into(iv_DIcon1);
        Picasso.with(this)
                .load(travel.getPicture())
                .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                .placeholder(R.mipmap.img_me)
                .into(iv_DIcon2);
        tv_DName.setText(travel.getNickName());
        CommonUtil.setSexImg(travel.getSex(), iv_DSex);
        String time = travel.getStartTimeTxt();
        String surplusSeat = String.format(Locale.CHINA, "余%d座", travel.getSurplusSeats());
        String priceStr = String.format(Locale.CHINA, "%s元/座", CommonUtil.formatPrice(travel.getTravelPrice(), 1));
        tv_DInfo1.setText(CommonUtil.appendStringInPoint(time, surplusSeat, priceStr));
        tv_Dinfo2.setText(CommonUtil.appendStringInPoint(travel.getCar(), travel.getLastTimeOnlineTxt()));
        tv_DUp.setText(travel.getRecommendStartAddress());
        tv_DDown.setText(travel.getRecommendEndAddress());
        v_DRecommendStartTime.setText(DateUtil.getTimeStr(travel.getRecommendStartTime(), "HH:mm"));
        v_DRecommendEndTime.setText(DateUtil.getTimeStr(travel.getRecommendEndTime(), "HH:mm"));
        if (reserveTravelType == RESERVED_TRAVEL_STATION) {
            tv_DTicketPrice.setText(String.format(Locale.CHINA, "%s元/座", CommonUtil.formatPrice(travel.getTravelPrice(), 1)));
        } else {
            tv_DTicketPrice.setText(String.format(Locale.CHINA, "%s元/座+%ss元接送费", CommonUtil.formatPrice(travel.getTravelPrice(), 1), CommonUtil.formatPrice(travel.getExtraMoney(), 1)));
        }
        tv_DExtraDistance.setText(String.format(Locale.CHINA, "车主绕行%s公里", String.valueOf(travel.getExtraDistance())));

        initMyStartAndEndAddressFromNet();

        if (myStart != null) {
            tv_DMyStartAddress.setText(myStart.getName());
        }
        if (myEnd != null) {
            tv_DMyEndAddress.setText(myEnd.getName());
        }
        if (!travel.isRecommendStatus()) {
            initInputMyLocationUI();
        }
    }

    private void initMyStartAndEndAddressFromNet() {
        LogUtil.i("TravelDetail_NoPayActivity method  initMyStartAndEndAddressFromNet");
        if (travel != null) {
            v_DNoAddress.setVisibility(View.GONE);
            ll_DType.setVisibility(View.VISIBLE);

            if (travel.getMatchStart() != null) {
                myStart = new Tip();
                myStart.setName(travel.getMatchStartAddress());
                myStart.setPostion(new LatLonPoint(travel.getMatchStart()[1], travel.getMatchStart()[0]));
            }
            if (travel.getMatchEnd() != null) {
                myEnd = new Tip();
                myEnd.setName(travel.getMatchEndAddress());
                myEnd.setPostion(new LatLonPoint(travel.getMatchEnd()[1], travel.getMatchEnd()[0]));
            }

            if (myStart == null) {
                isLocationStartAddress = true;
            }
            if (myStart != null && myStart.getPoint() != null) {
//                searchStartLine();
                if (travel.getMatchStart() == null) {
                    travel.setMatchStart(new double[]{myStart.getPoint().getLongitude(), myStart.getPoint().getLatitude()});
                    travel.setMatchStartAddress(myStart.getName());
                }
            }
            if (myEnd != null && myEnd.getPoint() != null) {
//                searchEndLine();
                if (travel.getMatchEnd() == null) {
                    travel.setMatchEnd(new double[]{myEnd.getPoint().getLongitude(), myEnd.getPoint().getLatitude()});
                    travel.setMatchEndAddress(myEnd.getName());
                }
            }

            if(type==0){
                type1();//todo : 重新选择 站点乘车
            }else {
                type2();
            }
        }
    }

    private void initInputMyLocationUI() {
        LogUtil.i("TravelDetail_NoPayActivity method  initInputMyLocationUI");
        v_DNoAddress.setVisibility(View.VISIBLE);
        ll_DType.setVisibility(View.GONE);
        dv_DDragView.setBottomHideHeight(0);
    }

    private TravelEntity getTravel() {
        LogUtil.i("TravelDetail_NoPayActivity method  getTravel");
        if (travel == null) {
            return null;
        }
        TravelEntity iTravel = new TravelEntity();
        iTravel.setStartTime(travel.getStartTime());
        iTravel.setStartAddress(travel.getStartAddress());
        iTravel.setEndAddress(travel.getEndAddress());
        iTravel.setType(ConstantValue.TravelType.DRIVER);
        iTravel.setStatus(travel.getTravelStatus());
        iTravel.setSeats(travel.getSeats());
        List<String> pictures = new ArrayList<>();
        if (travel.getPassengers() != null) {
            for (int i = 0; i < travel.getPassengers().size(); i++) {
                for (int j = 0; j < travel.getPassengers().get(i).getBookSeats(); j++) {
                    pictures.add(travel.getPassengers().get(i).getPicture());
                }
            }

        }
        iTravel.setHeadPictures(pictures);
        iTravel.setSurplusSeats(travel.getSurplusSeats());
        iTravel.setTravelPrice(travel.getTravelPrice());
        return iTravel;
    }

    private void showTravelLine() {
        LogUtil.i("TravelDetail_NoPayActivity method  showTravelLine");
        if (aMap == null) {
            return;
        }
        if (travel == null) return;
        if (travelLine != null) {
            travelLine.remove();
        }
        LatLonPoint dStart = new LatLonPoint(travel.getStart()[1], travel.getStart()[0]);
        LatLonPoint dEnd = new LatLonPoint(travel.getEnd()[1], travel.getEnd()[0]);

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(dStart, dEnd);
        // 第一个参数表示路径规划的起点和终点
        // 第二个参数表示驾车模式
        // 第三个参数表示途经点
        // 第四个参数表示避让区域，第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, travel.getStrategy(), getLatLonPints(travel.getWaypoints()), null, "");
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    private Marker travelStartMarker;
    private Marker travelEndMarker;
    private Text travelStartText;
    private Text travelEndText;
    private void initMarker() {
        LogUtil.i("TravelDetail_NoPayActivity method  initMarker");
        if (travel != null) {
            if(travel.getStart()!=null){//行程的起点
                travelStartMarker=mMapUtil.addOrMoveMarker(travelStartMarker,travel.getStart(),R.mipmap.icon_map_star);
                travelStartText=mMapUtil.addOrChangeText(travelStartText,travel.getStart(),travel.getStartAddress());
                travelStartMarker.setAnchor(0.5f,0.5f);
            }

            if(travel.getEnd()!=null){//行程的终点
                travelEndMarker=mMapUtil.addOrMoveMarker(travelEndMarker,travel.getEnd(),R.mipmap.icon_map_end);
                travelEndText=mMapUtil.addOrChangeText(travelEndText,travel.getEnd(),travel.getEndAddress());
                travelEndMarker.setAnchor(0.5f,0.5f);
            }

            if (myStart != null ) {//我的起点
                double[] loc;
                if( myStartLine != null && myStartLine.getPoints() != null && myStartLine.getPoints().size() > 0){
                    loc = new double[]{myStartLine.getPoints().get(0).longitude, myStartLine.getPoints().get(0).latitude};
                }else {
                    loc = new double[]{myStart.getPoint().getLongitude(), myStart.getPoint().getLatitude()};
                }
                myStartMarker = mMapUtil.addOrMoveMarker(myStartMarker, loc, R.mipmap.icon_map_star);
                myStartText = mMapUtil.addOrChangeText(myStartText, loc, myStart.getName());
                if(myStartMarker!=null){
                    myStartMarker.setAnchor(0.5f,0.5f);
                }
            }

            if (myEnd != null ) {//我的终点
                double[] loc;
                if( myEndLine != null && myEndLine.getPoints() != null && myEndLine.getPoints().size() > 0){
                    loc = new double[]{myEndLine.getPoints().get(myEndLine.getPoints().size() - 1).longitude, myEndLine.getPoints().get(myEndLine.getPoints().size() - 1).latitude};
                }else {
                    loc = new double[]{myEnd.getPoint().getLongitude(), myEnd.getPoint().getLatitude()};
                }
                myEndMarker = mMapUtil.addOrMoveMarker(myEndMarker, loc, R.mipmap.icon_map_end);
                myEndText = mMapUtil.addOrChangeText(myEndText, loc, myEnd.getName());
                if(myEndMarker!=null){
                    myEndMarker.setAnchor(0.5f,0.5f);
                }
            }

            if ((travelType == ConstantValue.TravelType.DRIVER && reserveTravelType == RESERVED_TRAVEL_PICKUP)) {//接送时的上下车点
                double[] start = new double[]{myStart.getPoint().getLongitude(), myStart.getPoint().getLatitude()};
                double[] end = new double[]{myEnd.getPoint().getLongitude(), myEnd.getPoint().getLatitude()};
                rStartMarker = mMapUtil.addOrMoveMarker(rStartMarker, start, startImg);
                startText = mMapUtil.addOrChangeText(startText, start, myStart.getName());
                rEndMarker = mMapUtil.addOrMoveMarker(rEndMarker, end, endImg);
                endText = mMapUtil.addOrChangeText(endText, end, myEnd.getName());
            } else if (travelType == ConstantValue.TravelType.PASSENGER) {//站点乘车的上下车点
                rStartMarker = mMapUtil.addOrMoveMarker(rStartMarker, travel.getStart(), startImg);
                startText = mMapUtil.addOrChangeText(startText, travel.getStart(), travel.getStartAddress());
                rEndMarker = mMapUtil.addOrMoveMarker(rEndMarker, travel.getEnd(), endImg);
                endText = mMapUtil.addOrChangeText(endText, travel.getEnd(), travel.getEndAddress());
            } else {//抢单的起终点
                rStartMarker = mMapUtil.addOrMoveMarker(rStartMarker, travel.getRecommendStartLocation(), startImg);
                startText = mMapUtil.addOrChangeText(startText, travel.getRecommendStartLocation(), travel.getRecommendStartAddress());
                rEndMarker = mMapUtil.addOrMoveMarker(rEndMarker, travel.getRecommendEndLocation(), endImg);
                endText = mMapUtil.addOrChangeText(endText, travel.getRecommendEndLocation(), travel.getRecommendEndAddress());
            }
        }
    }

    //获取距离
    private void getDistance(final boolean isStartLine, double startLon, double startLat, double endLon, double endLat, final TextView tv_info) {
        LogUtil.i("TravelDetail_NoPayActivity method  getDistance");
        tv_info.setText("加载中...");
        LogUtil.i("getDistance=" + isStartLine + "  " + startLon + "   " + startLat + "    " + endLon + "   " + endLat);
        if (isStartLine) {
            if (myStartLine != null) {
                myStartLine.remove();
            }
        } else {
            if (myEndLine != null) {
                myEndLine.remove();
            }
        }
        RouteSearch search = new RouteSearch(this);
        search.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS
                        && driveRouteResult != null
                        && driveRouteResult.getPaths() != null
                        && driveRouteResult.getPaths().size() > 0) {
                    final DrivePath driverPath = driveRouteResult.getPaths().get(0);
                    if (driverPath != null) {
                        final float dis = driverPath.getDistance();
                        String disStr;
                        if (dis > 2000) {
                            disStr = (int) (dis / 1000) + "公里";
                        } else {
                            disStr = (int) dis + "米";
                        }
                        String info = "驾驶" + disStr + "(" + dateFormate(driverPath.getDuration()) + ")";
                        tv_info.setText(info);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Polyline polyline = QuRoutOverlay.drawPath(aMap, QuRoutOverlay.getPolylineOptions(QuRoutOverlay.getLatLngs(driverPath), 0xFFB6D8FC));
                                if (isStartLine) {
                                    myStartLine = polyline;
                                    startDistance = (int) dis;
                                    startDuration= driverPath.getDuration();
                                } else {
                                    myEndLine = polyline;
                                    endDistance = (int) dis;
                                    endDuration=driverPath.getDuration();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initMarker();
                                        showDriverTravelDistance();
                                    }
                                });
                            }
                        })
                                .start();

                    } else {
                        tv_info.setText("加载失败");
                    }
                } else {
                    tv_info.setText("加载失败");
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS
                        && walkRouteResult != null
                        && walkRouteResult.getPaths() != null
                        && walkRouteResult.getPaths().size() > 0) {
                    final WalkPath walkPath = walkRouteResult.getPaths().get(0);
                    if (walkPath != null) {
                        final float dis = walkPath.getDistance();
                        String disStr;
                        if (dis > 2000) {
                            disStr = (int) (dis / 1000) + "公里";
                        } else {
                            disStr = (int) dis + "米";
                        }
                        String info = "步行" + disStr + "(" + dateFormate(walkPath.getDuration()) + ")";
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Polyline polyline = QuRoutOverlay.drawPath(aMap, QuRoutOverlay.getPolylineOptions(QuRoutOverlay.getLatLngs(walkPath), 0xFFB6D8FC));
                                if (isStartLine) {
                                    myStartLine = polyline;
                                    startDistance = (int) dis;
                                } else {
                                    myEndLine = polyline;
                                    endDistance = (int) dis;
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initMarker();
                                        showDriverTravelDistance();
                                    }
                                });
                            }
                        })
                                .start();
                        tv_info.setText(info);
                    } else {
                        tv_info.setText("加载失败");
                    }
                } else if (i == CODE_AMAP_OVER_DIRECTION_RANGE) {
                    tv_info.setText("步行线路过长");
                } else {
                    tv_info.setText("加载失败");
                }
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });

        LatLonPoint dStart = new LatLonPoint(startLat, startLon);
        LatLonPoint dEnd = new LatLonPoint(endLat, endLon);

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(dStart, dEnd);

        if (travelType == ConstantValue.TravelType.DRIVER) {
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
            search.calculateWalkRouteAsyn(query);
        } else {
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
            search.calculateDriveRouteAsyn(query);
        }
    }

    private void searchStartLine() {
        LogUtil.i("TravelDetail_NoPayActivity method  searchStartLine");
        if (myStart == null || travel == null) {
            if (myStartLine != null) {
                myStartLine.remove();
            }
        } else {
            if (travelType == ConstantValue.TravelType.PASSENGER) {
                if (travel.getStart() != null && travel.getStart().length == 2) {
                    if (travelLine != null && travelLine.getPoints() != null && travelLine.getPoints().size() > 0) {
                        LatLng latLng = travelLine.getPoints().get(0);
                        getDistance(true, myStart.getPoint().getLongitude(), myStart.getPoint().getLatitude(), latLng.longitude, latLng.latitude, tv_PStartDistance);
                    } else {
                        getDistance(true, myStart.getPoint().getLongitude(), myStart.getPoint().getLatitude(), travel.getStart()[0], travel.getStart()[1], tv_PStartDistance);
                    }
                }
            } else {
                if (travel.getRecommendStartLocation() != null && travel.getRecommendStartLocation().length == 2) {
                    getDistance(true, myStart.getPoint().getLongitude(), myStart.getPoint().getLatitude(), travel.getRecommendStartLocation()[0], travel.getRecommendStartLocation()[1], tv_DUpDistance);
                }
            }
        }
    }

    private void searchEndLine() {
        LogUtil.i("TravelDetail_NoPayActivity method  searchEndLine");
        if (myEnd == null || travel == null) {
            if (myEndLine != null) {
                myEndLine.remove();
            }
        } else {
            if (travelType == ConstantValue.TravelType.PASSENGER) {
                if (travel.getEnd() != null && travel.getEnd().length == 2) {
                    if (travelLine != null && travelLine.getPoints() != null && travelLine.getPoints().size() > 0) {
                        LatLng latLng = travelLine.getPoints().get(travelLine.getPoints().size() - 1);
                        getDistance(false, latLng.longitude, latLng.latitude, myEnd.getPoint().getLongitude(), myEnd.getPoint().getLatitude(), tv_PEndDistance);
                    } else {
                        getDistance(false, travel.getEnd()[0], travel.getEnd()[1], myEnd.getPoint().getLongitude(), myEnd.getPoint().getLatitude(), tv_PEndDistance);
                    }
                }
            } else {
                if (travel.getRecommendStartLocation() != null && travel.getRecommendEndLocation().length == 2) {
                    getDistance(false, travel.getRecommendEndLocation()[0], travel.getRecommendEndLocation()[1], myEnd.getPoint().getLongitude(), myEnd.getPoint().getLatitude(), tv_DMyEndDistance);
                }
            }
        }
    }

    private void navigation(final double[] start, final String startAddress, final double[] end, final String endAddress, final boolean isDriving) {
        LogUtil.i("TravelDetail_NoPayActivity method  navigation");
        if (start == null || start.length != 2 && (end == null || end.length != 2)) {
            ToastUtil.showToast("没有位置信息");
            return;
        }
        String[] items = {"高德地图", "百度地图"};
        final ItemsDialog dialog = new ItemsDialog(TravelDetail_NoPayActivity.this, items, null);
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
                        if (end != null) {
                            Navigation.pathGaode(start, startAddress, end, endAddress, TravelDetail_NoPayActivity.this, isDriving);
                        } else {
                            ToastUtil.showToast("请稍后");
                        }
                        break;
                    case 1:
                        if (end != null) {
                            Navigation.pathBaidu(start, startAddress, end, endAddress, TravelDetail_NoPayActivity.this, isDriving);
                        } else {
                            ToastUtil.showToast("请稍后");
                        }
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    private void changeLocation(int code) {
        LogUtil.i("TravelDetail_NoPayActivity method  changeLocation");
        if (travel == null) return;
        if(travel.getTravelList()==null||travel.getTravelList().size()==0){
            ToastUtil.showToast("没有获取到站点信息");
            return;
        }
        Intent intent = new Intent(this, ChangeStartOrEndLocationActivity.class);
        intent.putExtra("start", travel.getStart());
        intent.putExtra("startAddress", travel.getStartAddress());
        intent.putExtra("end", travel.getEnd());
        intent.putExtra("endAddress", travel.getEndAddress());
        intent.putExtra("strategy", travel.getStrategy());
        intent.putParcelableArrayListExtra("travelStations",travel.getTravelList());
        intent.putExtra("startIndex",0);
        intent.putExtra("wayPoints",travel.getWaypoints());
        intent.putExtra("endIndex",travel.getTravelList().size());

        switch (code) {
            case REQUEST_CODE_CHANGE_START_LOCATION:
                intent.putExtra("type", ChangeStartOrEndLocationActivity.TYPE_START);
                intent.putExtra("location", travel.getRecommendStartLocation());
                intent.putExtra("locationName", travel.getRecommendStartAddress());
                startActivityForResult(intent, REQUEST_CODE_CHANGE_START_LOCATION);
                break;
            case REQUEST_CODE_CHANGE_END_LOCATION:
                intent.putExtra("type", ChangeStartOrEndLocationActivity.TYPE_END);
                intent.putExtra("location", travel.getRecommendEndLocation());
                intent.putExtra("locationName", travel.getRecommendEndAddress());
                startActivityForResult(intent, REQUEST_CODE_CHANGE_END_LOCATION);
                break;
        }
    }

    private String dateFormate(long time) {
        LogUtil.i("TravelDetail_NoPayActivity method  dateFormate");
        String timeStr;
        long hour;
        long minute;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                timeStr = minute + "分钟";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return hour + "小时";
                minute = minute % 60;
                timeStr = hour + "小时" + minute + "分钟";
            }
        }
        return timeStr;
    }

    //抢单时显示司机的行程距离
    private void showDriverTravelDistance() {
        LogUtil.i("TravelDetail_NoPayActivity method  showDriverTravelDistance");
        if (startDistance != -1 && travelDistance != -1 && endDistance != -1) {
            int dis = startDistance + travelDistance + endDistance;
            String disStr;
            if (dis > 2000) {
                disStr = (int) (dis / 1000) + "公里";
            } else {
                disStr = (int) dis + "米";
            }
            v_PAllDistance.setVisibility(View.VISIBLE);
            tv_PAllDistance.setText(String.format(Locale.CHINA, "全程%s，用时%s", disStr,dateFormate(startDuration+travelDuration+endDuration)));
        }
    }

    PayDialog.PayDialogListener payListener = new PayDialog.PayDialogListener() {
        @Override
        public void paySuccess(String ordersId) {
            finishActivity(SearchActivity.class.getSimpleName());
            EventBus.getDefault().post(EventStatus.SHOW_MAIN_RED_POINT);
            try {
                Intent intent = new Intent(TravelDetail_NoPayActivity.this, TravelDetail_PassengerActivity.class);
                intent.putExtra("ordersID", ordersId);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }

        @Override
        public void cancelledOrder() {

        }

        @Override
        public void payOvertime(PayDialog dialog) {

        }
    };

    private double[][] getShowMapSpace(){
        double[] myS=null;
        double[] myE=null;
        double[] tS=null;
        double[] tE=null;
        if(myStart!=null){
            myS=new double[]{myStart.getPoint().getLongitude(),myStart.getPoint().getLatitude()};
        }
        if(myEnd!=null){
            myE=new double[]{myEnd.getPoint().getLongitude(),myEnd.getPoint().getLatitude()};
        }
        if(travel!=null){
            tS=travel.getStart();
            tE=travel.getEnd();
        }

        return getShowMapSpace(myS,myE,tS,tE);
    }

    private double[][] getShowMapSpace(double[]... locations){
        if(travel==null){
            return null;
        }
        double[][] space=new double[2][2];
        double maxLat=-100001;
        double maxLon=-100001;
        double minLat=100001;
        double minLon=100001;
        for(int i=0;i<locations.length;i++){
            if(locations[i]!=null&&locations[i].length==2){
                if(locations[i][0]>maxLon){
                    maxLon=locations[i][0];
                }
                if(locations[i][1]>maxLat){
                    maxLat=locations[i][1];
                }

                if(locations[i][0]<minLon){
                    minLon=locations[i][0];
                }
                if(locations[i][1]<minLat){
                    minLat=locations[i][1];
                }
            }
        }
        if(maxLat!=-100001&&maxLon!=-100001&&minLat!=100001&&minLon!=100001){
            space[0][0]=minLon;
            space[0][1]=maxLat;
            space[1][0]=maxLon;
            space[1][1]=minLat;
            return space;
        }

        return null;
    }

}
