package com.qcx.mini.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.adapter.QuFragmentPagerAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.ItemsDialog;
import com.qcx.mini.entity.PersonalLineEntity;
import com.qcx.mini.fragment.LineTravelFragment;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.StatusBarUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.PagerTitle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 常用线路行程匹配页
 */
public class LineTravelActivity extends BaseActivity {
    private int type;
    private long lineId ;
    private List<String> titles;
    private List<LineTravelFragment> fragments;
    private ItemsDialog topRightDialog;
    private QuFragmentPagerAdapter<LineTravelFragment> adapter;

    @BindView(R.id.line_travel_pager_title)
    PagerTitle mPagerTitle;
    @BindView(R.id.line_travel_pager)
    ViewPager mViewPager;
    @BindView(R.id.line_travel_startAddress)
    TextView tv_startAddress;
    @BindView(R.id.line_travel_endAddress)
    TextView tv_endAddress;

    @Override
    public int getLayoutID() {
        return R.layout.activity_line_travel;
    }

    @Override
    public void onTitleRightClick(View v) {
        if(topRightDialog==null){
            String[] items;
            if(type== ConstantValue.LinelType.DRIVER){
                items=new String[]{"修改时间","修改座位"};
            }else {
                items=new String[]{"修改时间","修改人数"};
            }
            topRightDialog=new ItemsDialog(this,items);
            topRightDialog.itemTextColor(Color.BLACK)
                    .cancelText(Color.BLACK)
                    .cancelText("取消")
                    .isTitleShow(false);
            topRightDialog.setOnItemClickListener(new ItemsDialog.OnItemClick() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            changeTime();
                            break;
                        case 1:
                            changeSeats();
                            break;
                    }
                    topRightDialog.dismiss();
                }
            });
        }
        topRightDialog.show();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("下班");
        StatusBarUtil.setColor(this,Color.WHITE,0);
        type=getIntent().getIntExtra("type",-1);
        lineId=getIntent().getLongExtra("lineId",-1);
        if(type==-1||lineId==-1){
            ToastUtil.showToast("无效的线路信息");
            finish();
        }
        initPager();
        mPagerTitle.setTitles(titles);
        adapter=new QuFragmentPagerAdapter<>(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(15);
        mPagerTitle.setPager(mViewPager);
    }

    private void initPager(){
        titles=new ArrayList<>();
        fragments=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        for(int i=0;i<7;i++){
            titles.add(DateUtil.formatDay("MM月dd日",calendar.getTimeInMillis()));
            LineTravelFragment fragment=new LineTravelFragment();
            fragment.setDay(i);
            fragment.setLineId(lineId);
            fragment.setLineType(type);
            fragments.add(fragment);
            calendar.add(Calendar.DATE,1);
        }
    }

//    private PersonalLineEntity line;
    public void setPersonalLine(PersonalLineEntity line){
//        this.line=line;
        if(line!=null){
            tv_startAddress.setText(line.getStartAddress());
            tv_endAddress.setText(line.getEndAddress());
        }

    }

    private void changeTime(){
        adapter.getItem(mViewPager.getCurrentItem()).changeStartTime();
    }

    private void changeSeats(){
        adapter.getItem(mViewPager.getCurrentItem()).changeSeats();
    }
}
