package com.qcx.mini.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.adapter.QuFragmentPagerAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.fragment.SetLinePageFragment;
import com.qcx.mini.listener.OnDataChangedListener;
import com.qcx.mini.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改、添加常用路线
 */
public class SetLineActivity extends BaseActivity {
    private QuFragmentPagerAdapter<Fragment> pagerAdapter;
    private SetLinePageFragment passengerFragment;
    private SetLinePageFragment driverFragment;
    private int currentPage=0;//
    private long lineId;

    @BindView(R.id.set_line_viewPager)
    ViewPager mPager;
    @BindView(R.id.set_line_page_title0)
    TextView tv_title0;
    @BindView(R.id.set_line_page_title1)
    TextView tv_title1;
    @BindView(R.id.set_line_submit)
    TextView tv_submit;

    @OnClick(R.id.set_line_page_title0)
    void clickTitle0(){
        setPageTitelBack(0);
        mPager.setCurrentItem(0);
    }

    @OnClick(R.id.set_line_page_title1)
    void clickTitle1(){
        setPageTitelBack(1);
        mPager.setCurrentItem(1);
    }
    @OnClick(R.id.set_line_submit)
    void submit(){
        if(currentPage==0){
            passengerFragment.submit();
        }else {
            driverFragment.submit();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_set_line;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("常用路线设置",true,false);
        lineId=getIntent().getLongExtra("lineId",0);
        int type=getIntent().getIntExtra("type",-1);
        boolean isToLinesActivity=getIntent().getBooleanExtra("isToLinesActivity",true);
        List<Fragment> fragments=new ArrayList<>();
        passengerFragment=new SetLinePageFragment();
        passengerFragment.setType(ConstantValue.TravelType.PASSENGER);
        passengerFragment.setListener(dataChangedListener);
        passengerFragment.setToLinesActivity(isToLinesActivity);

        driverFragment=new SetLinePageFragment();
        driverFragment.setType(ConstantValue.TravelType.DRIVER);
        driverFragment.setListener(dataChangedListener);
        driverFragment.setToLinesActivity(isToLinesActivity);
        if(lineId>0){
            passengerFragment.setLineId(lineId);
            driverFragment.setLineId(lineId);
        }

        fragments.add(passengerFragment);
        fragments.add(driverFragment);
        pagerAdapter=new QuFragmentPagerAdapter<>(getSupportFragmentManager(),fragments);
        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(pageChangeListener);

        if(type==ConstantValue.TravelType.DRIVER){
            mPager.setCurrentItem(1);
        }else if(type==ConstantValue.TravelType.PASSENGER){
            mPager.setCurrentItem(0);
        }

    }

    private ViewPager.OnPageChangeListener pageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setPageTitelBack(position);
            currentPage=position;
            dataChangedListener.onChanged();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private OnDataChangedListener dataChangedListener=new OnDataChangedListener() {
        @Override
        public void onChanged() {
            if(currentPage==0){
                if(passengerFragment!=null&&passengerFragment.canSubmit()){
                    tv_submit.setBackground(getResources().getDrawable(R.drawable.bg_circular_gradient_blue));
                }else {
                    tv_submit.setBackground(getResources().getDrawable(R.drawable.bg_circular_gray1));
                }
            }else {
                if(driverFragment!=null&&driverFragment.canSubmit()){
                    tv_submit.setBackground(getResources().getDrawable(R.drawable.bg_circular_gradient_blue));
                }else {
                    tv_submit.setBackground(getResources().getDrawable(R.drawable.bg_circular_gray1));
                }
            }
        }
    };

    private void setPageTitelBack(int pagePostion) {
        if (pagePostion == 0) {
            tv_title0.setBackground(getResources().getDrawable(R.drawable.bg_circular_white));
            tv_title0.setTextColor(0xFF484848);
            tv_title1.setBackgroundColor(0);
            tv_title1.setTextColor(0xFF8E95A1);
        } else if (pagePostion == 1) {
            tv_title0.setBackgroundColor(0);
            tv_title0.setTextColor(0xFF8E95A1);
            tv_title1.setBackground(getResources().getDrawable(R.drawable.bg_circular_white));
            tv_title1.setTextColor(0xFF484848);
        }
    }

}
