package com.qcx.mini.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.qcx.mini.R;
import com.qcx.mini.adapter.CouponListAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.CouponEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CouponActivity extends BaseActivity {
    @BindView(R.id.coupon_list)
    RecyclerView coupon_list;
    @BindView(R.id.no_coupon_img)
    ImageView no_coupon_img;

    @Override
    public int getLayoutID() {
        return R.layout.activity_coupon;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("优惠券");
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupon_list.setLayoutManager(layoutManager);
        coupon_list.setAdapter(new CouponListAdapter(this,getTestDatas()));
    }


    List<CouponEntity> getTestDatas(){
        List<CouponEntity> datas=new ArrayList<>();
        for(int i=0;i<100;i++){
            CouponEntity entity=new CouponEntity();
            datas.add(entity);
        }
        return datas;
    }
}
