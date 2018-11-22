package com.qcx.mini.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.MotionEvent;
import android.view.View;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.QuFragmentPagerAdapter;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.entity.HomeDataEntity;
import com.qcx.mini.net.QuCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.GestureFrameLayout;
import com.qcx.mini.widget.PagerTitle;
import com.qcx.mini.widget.ScrollControlViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/6.
 *
 */

public class MainFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener{

    private List<BaseFragment> fragments;
    private HomeFragment passengerFragment;
    private HomeFragment driverFragment;


    @BindView(R.id.fragment_main_toolbar)
    View mToolbar;
    @BindView(R.id.fragment_main_scale_view)
    View mScaleView;
    @BindView(R.id.fragment_main_gesture_layout)
    GestureFrameLayout mView;
    @BindView(R.id.fragment_main_appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.fragment_main_not_login_view)
    View v_notLogin;
    @BindView(R.id.fragment_main_login_view)
    View v_logined;
    @BindView(R.id.fragment_main_pager_title)
    PagerTitle mPagerTitle;
    @BindView(R.id.fragment_main_viewPager)
    ScrollControlViewPager mViewPager;

    @OnClick(R.id.fragment_main_not_login_view)
    void onLogin(){
        ToastUtil.showToast("登录");
        v_notLogin.setVisibility(View.GONE);
        v_logined.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fragment_main_login_view)
    void login(){
        v_notLogin.setVisibility(View.VISIBLE);
        v_logined.setVisibility(View.GONE);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        initPager();
        mAppBarLayout.addOnOffsetChangedListener(this);
        mView.setListener(new GestureFrameLayout.OnGestureListener() {
            @Override
            public void event(MotionEvent ev) {
                if(ev.getAction()==MotionEvent.ACTION_UP){
                    isDown=false;
                    isUp=true;
                    mViewPager.setScanScroll(true);
                }
                if(ev.getAction()==MotionEvent.ACTION_DOWN){
                    isDown=true;
                    isUp=false;

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainPageData();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }

    private void initPager(){
        List<String> titles = new ArrayList<>();
        titles.add("乘客");
        titles.add("车主");
        mPagerTitle.setTitles(titles);

        fragments=new ArrayList<>();

        passengerFragment=new HomeFragment();
        passengerFragment.setType(ConstantValue.TravelType.PASSENGER);
        driverFragment=new HomeFragment();
        driverFragment.setType(ConstantValue.TravelType.DRIVER);

        fragments.add(passengerFragment);
        fragments.add(driverFragment);

        QuFragmentPagerAdapter<BaseFragment> adapter=new QuFragmentPagerAdapter<>(getChildFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);
        mPagerTitle.setPager(mViewPager);
    }



    private boolean isDown=true;
    private boolean isUp=true;
    private int lastOffset;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(verticalOffset!=0&&Math.abs(verticalOffset-lastOffset)>80&&!isUp){
            mViewPager.setScanScroll(false);
        }
        if(isDown){
            lastOffset=verticalOffset;
            isDown=false;
        }

        float absVerticalOffset = Math.abs(verticalOffset);//AppBarLayout竖直方向偏移距离px
        float totalScrollRange = appBarLayout.getTotalScrollRange();//AppBarLayout总的距离px
        float alpha = absVerticalOffset / (totalScrollRange *3);
        mScaleView.setScaleY(1-(alpha/4));
        mScaleView.setScaleX(1-alpha);
//        if(absVerticalOffset>totalScrollRange*19/20){
//            mToolbar.setBackgroundResource(R.drawable.bg_circular_gray_top);
//        }else {
//            mToolbar.setBackgroundResource(R.drawable.bg_transparent);
//        }

    }

    private void getMainPageData(){
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.listOfHome, params, new QuCallback<HomeDataEntity>() {
            @Override
            public void onSuccess(HomeDataEntity t) {
                if(t!=null){
                    passengerFragment.setTravels(t.getPassenger().getPassengerTravel());
                    passengerFragment.setLines(t.getPassenger().getPassengerLine());
                    driverFragment.setTravels(t.getDriver().getDriverTravel());
                    driverFragment.setLines(t.getDriver().getDriverLine());
                }
            }
        });
    }
}
