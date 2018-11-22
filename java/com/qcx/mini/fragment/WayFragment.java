package com.qcx.mini.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.XxxxxxxxxxxxxActivity;
import com.qcx.mini.activity.CreateGroupActivity;
import com.qcx.mini.activity.MyJoinGroupActivity;
import com.qcx.mini.activity.SearchActivity;
import com.qcx.mini.activity.WayInfoActivity;
import com.qcx.mini.adapter.ItemMyWayAdapter;
import com.qcx.mini.adapter.QuFragmentPagerAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.ItemsDialog;
import com.qcx.mini.entity.HaveCreateGroupAuthEntity;
import com.qcx.mini.entity.WayListEntity;
import com.qcx.mini.listener.LocationListener;
import com.qcx.mini.listener.OnGroupClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.Location;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.FlowLayoutManger;
import com.qcx.mini.widget.GestureLinearLayout;
import com.qcx.mini.widget.QuRefreshHeader;
import com.qcx.mini.widget.ScrollControlViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.qcx.mini.net.NetUtil.joinGroup;

/**
 * Created by Administrator on 2018/3/22.
 */

public class WayFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {
    private String mToken = User.getInstance().getToken();
    private Location location = Location.getInstance();
    private boolean isGetWayData=false;

    private RecommendFragment recommendFragment;
    private HotWayFragment hotWayFragment;

    private ItemMyWayAdapter wayAdapter;

    @BindView(R.id.fragment_way_flock)
    RecyclerView rv_flock;
    @BindView(R.id.fragment_way_viewPager)
    ScrollControlViewPager viewPager;
    @BindView(R.id.fragment_way_refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.fragment_way_appBar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.fragment_way_title)
    TextView mTitle;
    @BindView(R.id.fragment_way_view)
    GestureLinearLayout mView;
    @BindView(R.id.fragment_way_title0)
    TextView tv_title0;
    @BindView(R.id.fragment_way_title0_view)
    View v_title0;
    @BindView(R.id.fragment_way_title1)
    TextView tv_title1;
    @BindView(R.id.fragment_way_groupType)
    TextView tv_groupType;
    @BindView(R.id.fragment_way_show_all_group)
    TextView tv_showAllGroup;
    @BindView(R.id.fragment_way_create_group)
    TextView tv_createGroup;
    @BindView(R.id.fragment_way_coordinatorLayout)
    CoordinatorLayout cl_coordinator;

    @OnClick(R.id.fragment_way_show_all_group)
    void showAllGroup(){
        startActivity(new Intent(getContext(),MyJoinGroupActivity.class));
    }


    public static void expandAppBarLayout(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        if (lp.getBehavior() != null) {
            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) lp.getBehavior();

            behavior.setTopAndBottomOffset(0);
//            behavior.onNestedPreScroll(coordinatorLayout, appBarLayout, null, 0, 1, new int[2]);
            behavior.onNestedPreScroll(coordinatorLayout, appBarLayout, null, 0, 1, new int[2],1);
//            behavior.onNestedScroll(coordinatorLayout,appBarLayout,null,0,0,100,100,1);
        }
    }
    @OnClick(R.id.fragment_way_create_group)
    void createGroup(){
        if(createStatus==1){
            startActivity(new Intent(getContext(),CreateGroupActivity.class));
        }else {
            startActivity(new Intent(getContext(),XxxxxxxxxxxxxActivity.class));
        }
//        expandAppBarLayout(cl_coordinator,mAppBarLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        mToken="";
        if ((mToken != null && !mToken.equals(User.getInstance().getToken()))
                ||isGetWayData) {
            mToken = User.getInstance().getToken();
            getWayData();
            initCreateGroup();
        }
    }
    @OnClick(R.id.fragment_way_search)
    void search(){
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    private int travelType=ConstantValue.TravelType.DRIVER;
    @OnClick(R.id.fragment_way_title0_view)
    void clickTitle0(){
        if(viewPager.getCurrentItem()==1){
            setPageTitelBack(0);
            viewPager.setCurrentItem(0);
            return;
        }
        String[] items;
        if(travelType==ConstantValue.TravelType.DRIVER){
            items = new String[]{"乘客行程"};
        }else {
            items = new String[]{"车主行程"};
        }
        final ItemsDialog dialog = new ItemsDialog(getContext(), items, null);
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
                        if(travelType==ConstantValue.TravelType.DRIVER){
                            travelType=ConstantValue.TravelType.PASSENGER;
                            recommendFragment.setType(ConstantValue.TravelType.PASSENGER);
                            tv_title0.setText("乘客行程");
                        }else {
                            tv_title0.setText("车主行程");
                            travelType=ConstantValue.TravelType.DRIVER;
                            recommendFragment.setType(ConstantValue.TravelType.DRIVER);
                        }
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.fragment_way_title1)
    void clickTitle1(){
        setPageTitelBack(1);
        viewPager.setCurrentItem(1);
        ViewPager pager;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        mSmartRefreshLayout.setRefreshHeader(new QuRefreshHeader(getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        FlowLayoutManger manger = new FlowLayoutManger();
        rv_flock.setLayoutManager(manger);
        wayAdapter=new ItemMyWayAdapter(getContext());
        wayAdapter.setListener(new OnGroupClickListener() {
            @Override
            public void onItemClick(long groupId,int groupType) {
                Intent intent=new Intent(getContext(),WayInfoActivity.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("groupType",groupType);
                startActivity(intent);
            }

            @Override
            public void onAddClick(long groupId,View view1) {
                joinGroup(groupId,view1);
            }
        });
        rv_flock.setAdapter(wayAdapter);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getWayData();
                if(recommendFragment!=null&&recommendFragment.isVisible()){
                    recommendFragment.getTravelsData(1);
                }
                if(hotWayFragment!=null&&hotWayFragment.isVisible()){
                    hotWayFragment.getData(1);
                }

            }
        });
        addPage();
//        getWayData();
//        initCreateGroup();
        mView.setListener(new GestureLinearLayout.OnGestureListener() {
            @Override
            public void event(MotionEvent ev) {
                if(ev.getAction()==MotionEvent.ACTION_UP){
                    isDown=false;
                    viewPager.setScanScroll(true);
                }
                if(ev.getAction()==MotionEvent.ACTION_DOWN){
                    isDown=true;
                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_way;
    }

    private void addPage() {
        final List<Fragment> fragments = new ArrayList<>();
        recommendFragment = new RecommendFragment();
        hotWayFragment = new HotWayFragment();
        fragments.add(recommendFragment);
        fragments.add(hotWayFragment);
        setPageTitelBack(0);
        viewPager.setAdapter(new QuFragmentPagerAdapter<>(getChildFragmentManager(), fragments, null));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPageTitelBack(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private boolean isDown=true;
    private int lastOffset;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(isDown&&verticalOffset!=0&&Math.abs(verticalOffset-lastOffset)>80){
            viewPager.setScanScroll(false);
        }
        if(isDown){
            lastOffset=verticalOffset;
            isDown=false;
        }
    }

    private void setPageTitelBack(int pagePostion) {
        if (pagePostion == 0) {
            v_title0.setBackground(getContext().getResources().getDrawable(R.drawable.bg_circular_white));
            tv_title0.setTextColor(0xFF484848);
            tv_title1.setBackgroundColor(0);
            tv_title1.setTextColor(0xFFB9BDC3);
        } else if (pagePostion == 1) {
            v_title0.setBackgroundColor(0);
            tv_title0.setTextColor(0xFFB9BDC3);
            tv_title1.setBackground(getContext().getResources().getDrawable(R.drawable.bg_circular_white));
            tv_title1.setTextColor(0xFF484848);
        }
    }

    private LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            getWayData(aMapLocation.getCity());
//                    location.onDestroy();
        }

        @Override
        public void onError(int errorCode) {
            ToastUtil.showToast("获取位置信息失败");
            getWayData("");
//                    location.onDestroy();
        }

        @Override
        public void onNoLocationPermission() {
//                    location.onDestroy();
            getWayData("");
        }
    };

    public void getWayData(){
        String city=SharedPreferencesUtil.getAppSharedPreferences().getString(ConstantString.SharedPreferencesKey.SP_LOCATION_CITY);
        if(TextUtils.isEmpty(city)){
            if(getActivity()!=null){
                ((BaseActivity)getActivity()).mRxPermissions
                        .request("android.permission.ACCESS_COARSE_LOCATION")
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    location.startLocation(locationListener, null);
                                } else {
                                    if (locationListener != null) {
                                        locationListener.onNoLocationPermission();
                                    }
                                }
                            }
                        });
            }
        }else {
            getWayData(city);
        }
    }

    private void getWayData(String city){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("currentCity",city);
        params.put("pageNo", 1);
        Request.post(URLString.myJoinedGroups, params, new EntityCallback(WayListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoading();
                WayListEntity entity= (WayListEntity) t;
                wayAdapter.setDatas(entity.getMyJoinedGroups().getGroupsList());
                if(tv_showAllGroup==null){
                    return;
                }
                if(wayAdapter.getItemCount()>=6){
                    tv_showAllGroup.setVisibility(View.VISIBLE);
                }else {
                    tv_showAllGroup.setVisibility(View.GONE);
                }
                if(entity.getMyJoinedGroups().isRecommend()){
                    tv_groupType.setText("推荐的拼车群");
                }else {
                    tv_groupType.setText("我的拼车群");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
            }
        });
    }

    private void finishLoading(){
        if(mSmartRefreshLayout!=null){
            mSmartRefreshLayout.finishRefresh();
        }
    }

    private int createStatus;
    private void initCreateGroup(){
        Map<String,Object> params=new HashMap<>();
        params.put("token",User.getInstance().getToken());
        Request.post(URLString.isHaveCreateGroupAuth, params, new EntityCallback(HaveCreateGroupAuthEntity.class) {
            @Override
            public void onSuccess(Object t) {
                HaveCreateGroupAuthEntity entity= (HaveCreateGroupAuthEntity) t;
                if(entity.isHaveCreateGroupAuth()){
                    tv_createGroup.setVisibility(View.VISIBLE);
                    createStatus=1;
                }else {
                    tv_createGroup.setVisibility(View.VISIBLE);
                    createStatus=2;
                }
            }
        });
    }
}
