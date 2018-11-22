package com.qcx.mini.widget.numSoftKeyboart;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/7/31.
 * 输入交易密码的键盘的holder
 */

public class ItemNumSoftKeyboartViewHolder extends BaseRecycleViewHolder {
    private String text;
    private TextView tv_text;
    private ImageView iv_delete;
    private OnNumInputListener listener;

    public ItemNumSoftKeyboartViewHolder(final View itemView) {
        super(itemView);
        tv_text = itemView.findViewById(R.id.item_soft_keyboard_num_text);
        iv_delete = itemView.findViewById(R.id.item_soft_keyboard_num_delete);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(text)||listener==null){
                    return;
                }
                LogUtil.i(text);
                if("-1".equals(text)){
                    listener.onDeleteNum();
                }else {
                    try {
                        int num=Integer.parseInt(text);
                        listener.onInputNum(num);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        try {
            listener = (OnNumInputListener) holederListener;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindData(Object data, @NonNull Params params) {
        try {
            text = (String) data;
            tv_text.setText(text);
            if (TextUtils.isEmpty(text)) {
                tv_text.setVisibility(View.GONE);
            }
            if("-1".equals(text)){
                tv_text.setVisibility(View.GONE);
                iv_delete.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
