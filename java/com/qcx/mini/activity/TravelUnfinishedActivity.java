package com.qcx.mini.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.adapter.QuFragmentPagerAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.fragment.TravelUnfinishedFragment;
import com.qcx.mini.utils.StatusBarUtil;
import com.qcx.mini.widget.PagerTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TravelUnfinishedActivity extends BaseActivity {
    private List<String> titles;
    private List<TravelUnfinishedFragment> fragments;
    @BindView(R.id.travel_unfinished_pager_title)
    PagerTitle mPagerTitle;
    @BindView(R.id.travel_unfinished_viewPager)
    ViewPager mViewPager;

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_unfinished;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, Color.WHITE,0);
        initTitleBar("待完成行程",true,false);
        initPager();
    }

    private void initPager(){
        titles=new ArrayList<>();
        fragments=new ArrayList<>();
        TravelUnfinishedFragment passengerFragment=new TravelUnfinishedFragment();
        passengerFragment.setTravelType(ConstantValue.TravelType.PASSENGER);
        TravelUnfinishedFragment driverFragment=new TravelUnfinishedFragment();
        driverFragment.setTravelType(ConstantValue.TravelType.DRIVER);

        titles.add("我是乘客");
        titles.add("我是车主");
        fragments.add(passengerFragment);
        fragments.add(driverFragment);

        mViewPager.setAdapter(new QuFragmentPagerAdapter<>(getSupportFragmentManager(),fragments));
        mPagerTitle.setTitles(titles);
        mPagerTitle.setPager(mViewPager);
    }


}
