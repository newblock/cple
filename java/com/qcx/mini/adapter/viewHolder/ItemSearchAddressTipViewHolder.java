package com.qcx.mini.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemSearchAddressTipClickListener;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ItemSearchAddressTipViewHolder extends BaseRecycleViewHolder implements View.OnClickListener {
    private ItemSearchAddressTipClickListener listener;
    private Tip tip;
    private TextView itemCheckAddressTitle;
    private TextView itemCheckAddressDesc;
    private ImageView itemCheckAddressImg;
    private View v_data, v_clear;

    public ItemSearchAddressTipViewHolder(View view) {
        super(view);
        itemCheckAddressTitle = view.findViewById(R.id.item_check_address_title);
        itemCheckAddressDesc = view.findViewById(R.id.item_check_address_desc);
        itemCheckAddressImg = view.findViewById(R.id.item_check_address_img);
        v_data = view.findViewById(R.id.item_check_address_data);
        v_clear = view.findViewById(R.id.item_check_address_clear);

        v_data.setOnClickListener(this);
        v_clear.setOnClickListener(this);
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener= (ItemSearchAddressTipClickListener) holederListener;
    }

    @Override
    public void bindData(Object data, @NonNull Params params) {
        try {
            tip = (Tip) data;
            itemCheckAddressDesc.setVisibility(View.VISIBLE);
            itemCheckAddressDesc.setText(tip.getDistrict());
            itemCheckAddressImg.setImageResource(R.mipmap.icon_place);
            itemCheckAddressTitle.setText(tip.getName());
        } catch (Exception e) {
            e.printStackTrace();
            tip = null;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_check_address_data:
                if (listener != null && tip != null) {
                    listener.onAddressClick(tip);
                }
                break;
            case R.id.item_check_address_clear:
                if (listener != null && tip != null) {
                    listener.onClearClick();
                }
                break;
        }
    }
}
