package com.qcx.mini.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.MapView;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.RadarView;

import butterknife.BindView;
import butterknife.OnClick;

public class TravelDetailActivity extends BaseActivity {

    MapView mMapView = null;
    @BindView(R.id.travel_detail_radar)
    RadarView mRadarView;
    @BindView(R.id.travel_detail_noPerson_view)
    View v_noPerson;

    @OnClick(R.id.travel_detail_noPerson_view)
    void onPerson(){
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView=findViewById(R.id.travel_detail_mapView);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public boolean setStatusBar() {
        UiUtils.setStatusBarTransparent(this);
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
}
