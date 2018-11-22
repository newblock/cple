package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.CouponEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */

public class CouponListAdapter extends BaseRecyclerViewAdapter<CouponEntity,CouponListAdapter.CouponListViewHolder> {

    public CouponListAdapter(Context context) {
        super(context);
    }

    public CouponListAdapter(Context context, List<CouponEntity> datas) {
        super(context, datas);
    }

    @Override
    public CouponListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponListViewHolder(inflater.inflate(R.layout.item_coupon_list,parent,false));
    }

    @Override
    public void onBindViewHolder(CouponListViewHolder holder, int position) {

    }

    class CouponListViewHolder extends RecyclerView.ViewHolder{

        public CouponListViewHolder(View itemView) {
            super(itemView);
        }
    }
}
