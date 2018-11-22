package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.ItemCityAdapter;
import com.qcx.mini.adapter.ItemDriverAndTravelAdapter;
import com.qcx.mini.adapter.ItemGroupTravelsGroupAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.CalendarDialog;
import com.qcx.mini.dialog.ChooseCityDialog;
import com.qcx.mini.dialog.DayOfMonthDialog;
import com.qcx.mini.dialog.ReleaseTypeDialog;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.GroupInfoEntity;
import com.qcx.mini.entity.GroupMembersEntity;
import com.qcx.mini.listener.ItemDriverAndTravelClickListenerImp;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.NetUtil;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.utils.mapUtils.MapUtil;
import com.qcx.mini.utils.mapUtils.NextDrivingRouteOverlay;
import com.qcx.mini.widget.QuRefreshHeader;
import com.qcx.mini.widget.itemDecoration.ItemGrayDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qcx.mini.adapter.viewHolder.ItemDriverAndTravelViewHolder.tag;

public class WayInfoActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, RouteSearch.OnRouteSearchListener {
    private long groupId;
    private int groupType;
    private ItemGroupTravelsGroupAdapter workAdapter;
    private ItemDriverAndTravelAdapter driverTravelAdapter;
    private int travelType;
    private int travelDay;
    private int goOrCome;
    private String startDate;
    private String endDate;
    private Map<String, GroupInfoEntity> cacheDriverData;
    private Map<String, GroupInfoEntity> cachePassengerData;
    private ItemGrayDecoration decoration;
    private RouteSearch routeSearch;
    private boolean isRouteSearch;//是否规划过线路
    private int pageNum;
    private List<String> citys;
    private GroupInfoEntity groupInfo;

    private AMap aMap;
    private MapUtil mMapUtil;

    @BindView(R.id.way_info_travel_list)
    RecyclerView rv_travel;
    @BindView(R.id.way_info_appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.way_info_titleBar2)
    View titleBar2;
    @BindView(R.id.way_info_titleBar1)
    View titleBar1;
    @BindView(R.id.way_info_back1)
    View titleBack1;
    @BindView(R.id.way_info_back2)
    View titleBack2;
    @BindView(R.id.way_info_more1)
    View titleMore1;
    @BindView(R.id.way_info_more2)
    View titleMore2;
    @BindView(R.id.way_info_refresh)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.way_info_share1)
    ImageView iv_share;
    @BindView(R.id.way_info_share2)
    ImageView iv_shar2;
    @BindView(R.id.way_info_title)
    TextView tv_title;
    @BindView(R.id.way_info_title2)
    TextView tv_title2;
    @BindView(R.id.way_info_driver_num)
    TextView tv_driverNum;
    @BindView(R.id.way_info_passenger_num)
    TextView tv_passengerNum;
    @BindView(R.id.way_info_describe)
    TextView tv_describe;
    @BindView(R.id.way_info_manager_id)
    TextView tv_manager;
    @BindView(R.id.way_info_members_count)
    TextView tv_membersCount;
    @BindView(R.id.way_info_head_view_1)
    View v_head1;
    @BindView(R.id.way_info_head_img_1)
    ImageView iv_head1;
    @BindView(R.id.way_info_head_view_2)
    View v_head2;
    @BindView(R.id.way_info_head_img_2)
    ImageView iv_head2;
    @BindView(R.id.way_info_head_view_3)
    View v_head3;
    @BindView(R.id.way_info_head_img_3)
    ImageView iv_head3;
    @BindView(R.id.way_info_day0)
    TextView tv_day0;
    @BindView(R.id.way_info_day1)
    TextView tv_day1;
    @BindView(R.id.way_info_day2)
    TextView tv_day2;
    @BindView(R.id.way_info_day3)
    TextView tv_day3;
    @BindView(R.id.way_info_day4)
    TextView tv_day4;
    @BindView(R.id.way_info_day5)
    TextView tv_day5;
    @BindView(R.id.way_info_day6)
    TextView tv_day6;
    @BindView(R.id.way_info_travel_type_0)
    View v_driverTravel;
    @BindView(R.id.way_info_travel_type_1)
    View v_passengerTravel;
    @BindView(R.id.way_info_join_view)
    View v_join;
    @BindView(R.id.way_info_wark_day_view)
    View v_warkDay;
    @BindView(R.id.way_info_scenic_city_view)
    View v_scenicCity;
    @BindView(R.id.way_info_city_view)
    View v_city;
    @BindView(R.id.way_info_mapView)
    MapView mMapView;
    @BindView(R.id.way_info_group_img)
    ImageView iv_groupImg;
    @BindView(R.id.way_info_no_travel_view)
    View v_noTravel;
    @BindView(R.id.way_info_city_text)
    TextView tv_city;
    @BindView(R.id.way_info_time_text)
    TextView tv_time;
    @BindView(R.id.way_info_price)
    TextView tv_price;
    @BindView(R.id.way_info_distance)
    TextView tv_distance;
    @BindView(R.id.way_info_travel_list_view)
    View v_list;

    private List<TextView> days;

    @OnClick({R.id.way_info_back1, R.id.way_info_back2})
    void back() {
        finish();
    }

    @OnClick({R.id.way_info_share1, R.id.way_info_share2})
    void share() {
        if (groupInfo == null) {
            return;
        }
        ShareUtil.shareGroup(getSupportFragmentManager(), groupId, groupType, groupInfo.getGroupTitle(), groupInfo.getGroupNotice());
    }

    @OnClick({R.id.way_info_more1, R.id.way_info_more2})
    void more() {
        Intent intent = new Intent(this, GroupSetActivity.class);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }

    @OnClick(R.id.way_info_group_member_view)
    void showMembers() {
        Intent intent = new Intent(this, GroupMembersActivity.class);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }

    @OnClick(R.id.way_info_join_view)
    void join() {
        NetUtil.joinGroup(groupId, v_join);
    }

    @OnClick(R.id.way_info_release_travel_view)
    void releaseTravel() {
        new ReleaseTypeDialog()
                .setShowSuccessDialog(false)
                .show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.way_info_travel_type_0)
    void clickDriverTravel() {
        v_driverTravel.setBackground(getResources().getDrawable(R.drawable.bg_circular_white));
        v_passengerTravel.setBackgroundColor(Color.TRANSPARENT);
        travelType = ConstantValue.TravelType.DRIVER;
        getData(true, 1);
    }

    @OnClick(R.id.way_info_travel_type_1)
    void clickPassengerTravel() {
        v_driverTravel.setBackgroundColor(Color.TRANSPARENT);
        v_passengerTravel.setBackground(getResources().getDrawable(R.drawable.bg_circular_white));
        travelType = ConstantValue.TravelType.PASSENGER;
        getData(true, 1);
    }

    @OnClick(R.id.way_info_day0)
    void clickDay0() {
        selectedDay(0);
    }

    @OnClick(R.id.way_info_day1)
    void clickDay1() {
        selectedDay(1);
    }

    @OnClick(R.id.way_info_day2)
    void clickDay2() {
        selectedDay(2);
    }

    @OnClick(R.id.way_info_day3)
    void clickDay3() {
        selectedDay(3);
    }

    @OnClick(R.id.way_info_day4)
    void clickDay4() {
        selectedDay(4);
    }

    @OnClick(R.id.way_info_day5)
    void clickDay5() {
        selectedDay(5);
    }

    @OnClick(R.id.way_info_day6)
    void clickDay6() {
        selectedDay(6);
    }

    @OnClick(R.id.way_info_time_view)
    void time() {
        new CalendarDialog()
                .setListener(new CalendarDialog.OnDialogClickListener() {
                    @Override
                    public void onTopLeftClick() {

                    }

                    @Override
                    public void onTopRightClick(long startTime, long endTime) {
                        if (startTime == 0) {
                            startDate = "";
                            endDate = "";
                            tv_time.setText("全部时间");
                        } else if (endTime == 0) {
                            tv_time.setText(DateUtil.getTimeStr(startTime, "MM月dd日"));
                            startDate = DateUtil.getTimeStr(startTime, "yyyy-MM-dd");
                        } else {
                            startDate = DateUtil.getTimeStr(startTime, "yyyy-MM-dd");
                            endDate = DateUtil.getTimeStr(endTime, "yyyy-MM-dd");
                            String showTime = DateUtil.getTimeStr(startTime, "MM月dd日") + "-" + DateUtil.getTimeStr(endTime, "MM月dd日");
                            tv_time.setText(showTime);
                        }
                        getData(false, 1);
                    }
                })
                .show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.way_info_city_view)
    void city() {
        if (citys == null) return;
        final ChooseCityDialog dialog = new ChooseCityDialog();
        dialog.setData(citys)
                .setListener(new ItemCityAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String city) {
                        if (!TextUtils.isEmpty(city)) {
                            tv_city.setText(city);
                            getData(false, 1);
                            dialog.dismiss();
                        }
                    }
                });
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_way_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        groupId = getIntent().getLongExtra("groupId", 0);
        groupType = getIntent().getIntExtra("groupType", 0);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_travel.setLayoutManager(manager);
        workAdapter = new ItemGroupTravelsGroupAdapter(this);
        driverTravelAdapter = new ItemDriverAndTravelAdapter(this);
        driverTravelAdapter.setListener(new ItemDriverAndTravelClickListenerImp(this));
        decoration = new ItemGrayDecoration(8, getResources().getColor(R.color.gray_line));
        appBarLayout.addOnOffsetChangedListener(this);
        refreshLayout.setRefreshHeader(new QuRefreshHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(false, 1);
            }
        });
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(false, pageNum);
            }
        });
        cacheDriverData = new HashMap<>();
        cachePassengerData = new HashMap<>();
        days = new ArrayList<>();
        days.add(tv_day0);
        days.add(tv_day1);
        days.add(tv_day2);
        days.add(tv_day3);
        days.add(tv_day4);
        days.add(tv_day5);
        days.add(tv_day6);
        initDayView();
        travelType = ConstantValue.TravelType.DRIVER;
        goOrCome = ConstantValue.WayType.GO_BACK;
        tv_city.setText(SharedPreferencesUtil.getAppSharedPreferences().getString(ConstantString.SharedPreferencesKey.SP_LOCATION_CITY));

//        groupType=ConstantValue.GroupType.SCENIC;
//        groupId=41013033902786L;
        endDate = "";

        switch (groupType) {
            case ConstantValue.GroupType.WORK:
                v_warkDay.setVisibility(View.VISIBLE);
                v_scenicCity.setVisibility(View.GONE);
                iv_groupImg.setVisibility(View.GONE);
                mMapView.setVisibility(View.VISIBLE);
                mMapView.onCreate(savedInstanceState);
                initMapView();
                refreshLayout.setEnableLoadmore(false);
                startDate = DateUtil.getCurrentDateString("yyyy-MM-dd");
                v_list.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                break;
            case ConstantValue.GroupType.ACROSS_CITY:
                v_warkDay.setVisibility(View.GONE);
                v_scenicCity.setVisibility(View.VISIBLE);
                v_city.setVisibility(View.GONE);
                iv_groupImg.setVisibility(View.GONE);
                mMapView.setVisibility(View.VISIBLE);
                mMapView.onCreate(savedInstanceState);
                initMapView();
                refreshLayout.setEnableLoadmore(true);
                refreshLayout.setEnableAutoLoadmore(false);
                v_list.setBackgroundColor(Color.WHITE);
                break;
            case ConstantValue.GroupType.SCENIC:
                v_warkDay.setVisibility(View.GONE);
                v_scenicCity.setVisibility(View.VISIBLE);
                v_city.setVisibility(View.VISIBLE);
                iv_groupImg.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.GONE);
                refreshLayout.setEnableLoadmore(true);
                refreshLayout.setEnableAutoLoadmore(false);
                startDate = DateUtil.getCurrentDateString("yyyy-MM-dd");
                v_list.setBackgroundColor(Color.WHITE);
                break;
        }

        rv_travel.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(getApplicationContext()).resumeTag(tag);
                } else {
                    Picasso.with(getApplicationContext()).pauseTag(tag);
                }
            }
        });
        getData(false, 1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData(false, 1);
    }

    @Override
    public boolean setStatusBar() {
        UiUtils.setStatusBarTransparent(this);
        UiUtils.setStatusBarLightMode(this, Color.TRANSPARENT);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
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
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float absVerticalOffset = Math.abs(verticalOffset);//AppBarLayout竖直方向偏移距离px
        float totalScrollRange = appBarLayout.getTotalScrollRange();//AppBarLayout总的距离px
        float alpha = absVerticalOffset / (totalScrollRange * 3 / 7);
        titleBar2.setAlpha(alpha);
        titleBack2.setAlpha(alpha);
        titleMore2.setAlpha(alpha);
        tv_title.setAlpha(alpha);
        iv_share.setAlpha(alpha);

        titleBack1.setAlpha(1 - alpha);
        titleMore1.setAlpha(1 - alpha);
        iv_shar2.setAlpha(1 - alpha);
    }

    private void getNormalGroupData(final int type, final String date) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("groupId", groupId);
        params.put("travelType", type);
        params.put("dateOfTxt", date);
        Request.post(URLString.groupDetail, params, new EntityCallback(GroupInfoEntity.class) {
            @Override
            public void onSuccess(Object t) {
                GroupInfoEntity groupInfoEntity = (GroupInfoEntity) t;
                showData(groupInfoEntity, type);
                if (type == ConstantValue.TravelType.DRIVER) {
                    cacheDriverData.put(date, groupInfoEntity);
                } else if (type == ConstantValue.TravelType.PASSENGER) {
                    cachePassengerData.put(date, groupInfoEntity);
                }
                showDataView();
            }
        });
    }

    private void getScenicGroupData(final int type, final String startDate, String endDate, final int page) {
        if (page == 1) {
            pageNum = 1;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("groupId", groupId);
        params.put("travelType", type);
        if (!TextUtils.isEmpty(endDate)) {
            params.put("endDateOfTxt", endDate);
        }
        if (!TextUtils.isEmpty(startDate)) {
            params.put("startDateOfTxt", startDate);
        }
        params.put("pageNo", pageNum);
        params.put("currentCity", tv_city.getText());
        Request.post(URLString.touristGroupDetail, params, new EntityCallback(GroupInfoEntity.class) {
            @Override
            public void onSuccess(Object t) {
                GroupInfoEntity groupInfoEntity = (GroupInfoEntity) t;
                citys = groupInfoEntity.getCitys();
                if (page == 1) {
                    showData(groupInfoEntity, type);
                    showDataView();
                } else {
                    driverTravelAdapter.addDatas(groupInfoEntity.getTravelOneVos());
                }
                if (groupInfoEntity.getTravelOneVos().size() > 0) {
                    pageNum++;
                }
                finishLoading();
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
                citys = null;
            }
        });
    }

    private void getCrossCityGroupData(final int type, final String startDate, String endDate, final int page) {
        if (page == 1) {
            pageNum = 1;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("groupId", groupId);
        params.put("travelType", type);
        params.put("goOrCome", goOrCome);
        if (!TextUtils.isEmpty(startDate)) {
            params.put("startDateOfTxt", startDate);
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("endDateOfTxt", endDate);
        }
        params.put("pageNo", pageNum);
        Request.post(URLString.crossCityGroupDetail, params, new EntityCallback(GroupInfoEntity.class) {
            @Override
            public void onSuccess(Object t) {
                GroupInfoEntity groupInfoEntity = (GroupInfoEntity) t;
                if (page == 1) {
                    showData(groupInfoEntity, type);
                    showDataView();
                } else {
                    driverTravelAdapter.addDatas(groupInfoEntity.getTravelOneVos());
                }
                if (groupInfoEntity.getTravelOneVos().size() > 0) {
                    pageNum++;
                }
                finishLoading();
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
            }
        });
    }

    private void getData(boolean isShowCache, int pageNum) {
        if (isShowCache) {
            if (travelType == ConstantValue.TravelType.DRIVER) {
                GroupInfoEntity driverTravels = cacheDriverData.get(startDate);
                if (driverTravels != null && driverTravels.getThatDayTravels() != null && driverTravels.getThatDayTravels().size() > 0) {
                    showData(cacheDriverData.get(startDate), travelType);
                    return;
                }
            } else if (travelType == ConstantValue.TravelType.PASSENGER) {
                GroupInfoEntity passengerTravels = cachePassengerData.get(startDate);
                if (passengerTravels != null && passengerTravels.getThatDayTravels() != null && passengerTravels.getThatDayTravels().size() > 0) {
                    showData(cachePassengerData.get(startDate), travelType);
                    return;
                }
            }
        }
        switch (groupType) {
            case ConstantValue.GroupType.WORK:
                getNormalGroupData(travelType, startDate);
                break;
            case ConstantValue.GroupType.ACROSS_CITY:
                getCrossCityGroupData(travelType, startDate, endDate, pageNum);
                break;
            case ConstantValue.GroupType.SCENIC:
                getScenicGroupData(travelType, startDate, endDate, pageNum);
                break;
        }
    }

    private void showDataView() {
        if (rv_travel == null || rv_travel.getAdapter() == null || rv_travel.getAdapter().getItemCount() == 0) {
//            rv_travel.setVisibility(View.GONE);
            v_noTravel.setVisibility(View.VISIBLE);
        } else {
//            rv_travel.setVisibility(View.VISIBLE);
            v_noTravel.setVisibility(View.GONE);
        }
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
    }

    private void showData(GroupInfoEntity groupInfo, int type) {
        this.groupInfo = groupInfo;
        switch (groupType) {
            case ConstantValue.GroupType.WORK:
                showWorkTravelList(groupInfo);
                showLine(groupInfo.getStart(), groupInfo.getEnd());
                mMapUtil.addText(groupInfo.getStart(),groupInfo.getStartAddress());
                mMapUtil.addText(groupInfo.getEnd(),groupInfo.getEndAddress());
                break;
            case ConstantValue.GroupType.ACROSS_CITY:
                showScenicTravelList(groupInfo);
                showLine(groupInfo.getStart(), groupInfo.getEnd());
                mMapUtil.addText( groupInfo.getStart(),groupInfo.getStartAddress());
                mMapUtil.addText(groupInfo.getEnd(),groupInfo.getEndAddress());
                break;
            case ConstantValue.GroupType.SCENIC:
                showScenicTravelList(groupInfo);
                break;
        }
        tv_title.setText(groupInfo.getGroupTitle());
        tv_title2.setText(groupInfo.getGroupTitle());
        tv_driverNum.setText("车主".concat(String.valueOf(groupInfo.getGroupMembers().getDriversCount())));
        tv_passengerNum.setText("乘客".concat(String.valueOf(groupInfo.getGroupMembers().getMembersCount() - groupInfo.getGroupMembers().getDriversCount())));
        tv_describe.setText(groupInfo.getGroupNotice());
        tv_manager.setText("运营客服微信：".concat(groupInfo.getGroupManager()));
        tv_membersCount.setText(String.format(Locale.CHINA, "共%d人", groupInfo.getGroupMembers().getMembersCount()));
        if (groupInfo.getAdvisePrice() != 0) {
            tv_price.setText("推荐价".concat(String.valueOf(groupInfo.getAdvisePrice())));
            tv_price.setVisibility(View.VISIBLE);
        } else {
            tv_price.setVisibility(View.GONE);
        }

        if (groupInfo.getDistance() != 0) {
            tv_distance.setText("路程".concat(String.valueOf(groupInfo.getDistance()).concat("公里")));
            tv_distance.setVisibility(View.VISIBLE);
        } else {
            tv_distance.setVisibility(View.GONE);
        }

        if (groupInfo.isJoined()) {
            v_join.setVisibility(View.GONE);
            titleMore1.setVisibility(View.VISIBLE);
            titleMore2.setVisibility(View.VISIBLE);
        } else {
            v_join.setVisibility(View.VISIBLE);
            titleMore1.setVisibility(View.GONE);
            titleMore2.setVisibility(View.GONE);
        }
        Picasso.with(this)
                .load(groupInfo.getGroupBanner())
                .resize(1000, 500)
                .into(iv_groupImg);
        showMemberHead(groupInfo.getGroupMembers().getMembers());
        showDataView();
    }

    private void showWorkTravelList(GroupInfoEntity groupInfo) {
        if (groupInfo != null) {
            if (!(rv_travel.getAdapter() instanceof ItemGroupTravelsGroupAdapter)) {
                rv_travel.setAdapter(workAdapter);
                rv_travel.removeItemDecoration(decoration);
            }
            Map<String, List<GroupInfoEntity.GroupTravelEntity>> data = groupInfo.getThatDayTravels();
            if (data != null) {
                List<List<GroupInfoEntity.GroupTravelEntity>> datas = new ArrayList<>();
                final String[] hours = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
                for (int i = 0; i < hours.length; i++) {
                    List<GroupInfoEntity.GroupTravelEntity> groupEntity = data.get(hours[i]);
                    if (groupEntity != null) {
                        for (GroupInfoEntity.GroupTravelEntity d : groupEntity) {
                            d.setTitle(hours[i] + ":00");
                            d.setTravelType(travelType);
                        }
                        datas.add(groupEntity);
                    }
                }
                workAdapter.setDatas(datas);
            }
        }
    }

    private void showScenicTravelList(GroupInfoEntity groupInfo) {
        if (groupInfo != null) {
            List<DriverAndTravelEntity> datas = groupInfo.getTravelOneVos();
            if (!(rv_travel.getAdapter() instanceof ItemDriverAndTravelAdapter)) {
                rv_travel.setAdapter(driverTravelAdapter);
                rv_travel.addItemDecoration(decoration);
            }
            driverTravelAdapter.setDatas(datas);
        }
    }

    private void showMemberHead(List<GroupMembersEntity.GroupMember> members) {
        if (members == null || members.size() == 0) {
            v_head1.setVisibility(View.GONE);
            v_head2.setVisibility(View.GONE);
            v_head3.setVisibility(View.GONE);
        } else if (members.size() == 1) {
            v_head1.setVisibility(View.VISIBLE);
            v_head2.setVisibility(View.GONE);
            v_head3.setVisibility(View.GONE);
            Picasso.with(this)
                    .load(members.get(0).getPicture())
                    .error(R.mipmap.img_me)
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .into(iv_head1);
        } else if (members.size() == 2) {
            v_head1.setVisibility(View.VISIBLE);
            v_head2.setVisibility(View.VISIBLE);
            v_head3.setVisibility(View.GONE);
            Picasso.with(this)
                    .load(members.get(0).getPicture())
                    .error(R.mipmap.img_me)
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .into(iv_head1);
            Picasso.with(this)
                    .load(members.get(1).getPicture())
                    .error(R.mipmap.img_me)
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .into(iv_head2);
        } else if (members.size() >= 3) {
            v_head1.setVisibility(View.VISIBLE);
            v_head2.setVisibility(View.VISIBLE);
            v_head3.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(members.get(0).getPicture())
                    .error(R.mipmap.img_me)
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .into(iv_head1);
            Picasso.with(this)
                    .load(members.get(1).getPicture())
                    .error(R.mipmap.img_me)
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .into(iv_head2);
            Picasso.with(this)
                    .load(members.get(2).getPicture())
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .error(R.mipmap.img_me)
                    .into(iv_head3);
        }
    }

    private void initDayView() {
        for (int i = 0; i < days.size(); i++) {
            long time = System.currentTimeMillis() + i * DateUtil.DAY;
            days.get(i).setText(DateUtil.formatDayInWeek(time));
            days.get(i).setTag(DateUtil.getTimeStr(time, "yyyy-MM-dd"));
        }
    }

    private void selectedDay(int day) {
        for (int i = 0; i < days.size(); i++) {
            if (i == day) {
                days.get(i).setBackground(getResources().getDrawable(R.drawable.bg_circular_blue));
                days.get(i).setTextColor(Color.WHITE);
                travelDay = day;
                startDate = (String) days.get(i).getTag();
                getData(true, 1);
            } else {
                days.get(i).setBackgroundColor(Color.TRANSPARENT);
                days.get(i).setTextColor(0xFF484848);
            }
        }
    }

    private void initMapView() {
        aMap = mMapView.getMap();
        mMapUtil = new MapUtil(this, aMap);
        mMapUtil.iniMap();
        if (routeSearch == null) {
            routeSearch = new RouteSearch(this);
            routeSearch.setRouteSearchListener(this);
        }
    }

    private void showLine(double[] start, double[] end) {
        if (aMap == null) return;
        LogUtil.i("ddddddddddddddd showline");
        LatLonPoint dStart = new LatLonPoint(start[1], start[0]);
        LatLonPoint dEnd = new LatLonPoint(end[1], end[0]);

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(dStart, dEnd);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询

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
                                    WayInfoActivity.this, aMap, drivePath,
                                    driveRouteResult.getStartPos(),
                                    driveRouteResult.getTargetPos(), null);
                            nextDrivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                            nextDrivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                            nextDrivingRouteOverlay.removeFromMap();
                            nextDrivingRouteOverlay.addToMap();
                            mMapUtil.showAllMarkers(80, nextDrivingRouteOverlay.getEndMarker(), nextDrivingRouteOverlay.getStartMarker());
                            isRouteSearch = true;
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

    private void finishLoading() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    }
}
