package com.qcx.mini.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;

/**
 * Created by Administrator on 2018/8/31.
 *
 */

public class ItemForceInfoHeadViewHolder extends BaseRecycleViewHolder<String> {

    View v_describeSingle;
    View v_describeAll;

    public ItemForceInfoHeadViewHolder(View itemView) {
        super(itemView);
        v_describeSingle=itemView.findViewById(R.id.force_info_describe_single);
        v_describeAll=itemView.findViewById(R.id.force_info_describe_all);
        itemView.findViewById(R.id.force_info_describe_unfold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_describeAll.setVisibility(View.VISIBLE);
                v_describeSingle.setVisibility(View.GONE);
            }
        });
        itemView.findViewById(R.id.force_info_describe_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_describeAll.setVisibility(View.GONE);
                v_describeSingle.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void bindData(String data, @NonNull Params params) {

    }
}
