package com.qcx.mini.activity;

import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.adapter.QuFragmentPagerAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.fragment.PersonalLinesFragment;
import com.qcx.mini.widget.PagerTitle;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 个人常用路线
 */
public class PersonalLinesActivity extends BaseActivity {
    private List<PersonalLinesFragment> fragments;

    @BindView(R.id.personal_line_pager)
    ViewPager mViewPager;
    @BindView(R.id.personal_line_pager_title)
    PagerTitle mPagerTitle;

    @Override
    public int getLayoutID() {
        return R.layout.activity_personal_lines;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initTitleBar("我的常用路线",true,false);
        initPager();
        fragments.get(0).setType(ConstantValue.TravelType.PASSENGER);
        fragments.get(1).setType(ConstantValue.TravelType.DRIVER);
    }

    private void initPager(){
        fragments=new ArrayList<>();
        fragments.add(new PersonalLinesFragment());
        fragments.add(new PersonalLinesFragment());

        QuFragmentPagerAdapter<PersonalLinesFragment> adapter=new QuFragmentPagerAdapter<>(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);

        List<String> titles=new ArrayList<>();
        titles.add("我是乘客");
        titles.add("我是车主");
        mPagerTitle.setTitles(titles);
        mPagerTitle.setPager(mViewPager);
    }

}
