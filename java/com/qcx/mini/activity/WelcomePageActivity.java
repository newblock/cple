package com.qcx.mini.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.adapter.WelcomePageAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class WelcomePageActivity extends BaseActivity {
    private WelcomePageAdapter adapter;

    @BindView(R.id.welcome_page_viewPager)
    ViewPager mViewPager;

    @BindView(R.id.welcome_page_in)
    TextView tv_in;

    @Override
    public int getLayoutID() {
        return R.layout.activity_welcome_page;
    }


    @OnClick(R.id.welcome_page_in)
    void in(){
        SharedPreferencesUtil.getAppSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_WELCOME_PAGER_IS_SHOW,true);
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        adapter=new WelcomePageAdapter(this,new int[]{R.mipmap.welcome_01,R.mipmap.welcome_02,R.mipmap.welcome_03});
        mViewPager.setAdapter(adapter);
        if(SharedPreferencesUtil.getAppSharedPreferences().getBoolean(ConstantString.SharedPreferencesKey.SP_WELCOME_PAGER_IS_SHOW,false)){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        tv_in.animate().alpha(0f).setDuration(0);
        tv_in.setVisibility(View.GONE);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==2){
                    tv_in.setVisibility(View.VISIBLE);
                    tv_in.animate().alpha(1.0f).setDuration(1000);
                }else {
                    tv_in.setVisibility(View.GONE);
                    tv_in.animate().alpha(0).setDuration(400);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
