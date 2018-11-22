package com.qcx.mini.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.dexfun.layout.utils.AutoUtils;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseListViewAdapter;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */

public class SearchAddressListAdapter extends BaseRecyclerViewAdapter<Tip, SearchAddressListAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public SearchAddressListAdapter(Context context) {
        super(context);
    }

    public SearchAddressListAdapter(Context context, List<Tip> datas) {
        super(context, datas);
    }

    private void initializeViews(Tip object, ViewHolder holder) {
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public SearchAddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_set_address, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchAddressListAdapter.ViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        if (position < datas.size()) {
            holder.v_clear.setVisibility(View.GONE);
            holder.v_data.setVisibility(View.VISIBLE);
            holder.bindData(getItem(position),params);
        } else {
            holder.v_clear.setVisibility(View.VISIBLE);
            holder.v_data.setVisibility(View.GONE);
        }
    }

    protected class ViewHolder extends BaseRecycleViewHolder implements View.OnClickListener {
        private Tip tip;
        private TextView itemCheckAddressTitle;
        private TextView itemCheckAddressDesc;
        private ImageView itemCheckAddressImg;
        private View v_data, v_clear;

        public ViewHolder(View view) {
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
        public void bindData(Object data, @NonNull Params params) {
            try {
                tip = (Tip) data;
                itemCheckAddressDesc.setVisibility(View.VISIBLE);
                itemCheckAddressDesc.setText(tip.getDistrict());
                itemCheckAddressImg.setImageResource(R.mipmap.icon_place);
                itemCheckAddressTitle.setText(tip.getName());
            } catch (Exception e) {
                e.printStackTrace();
                tip=null;
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_check_address_data:
                    if(listener!=null&&tip!=null){
                        listener.onAddressClick(tip);
                    }
                    break;
                case R.id.item_check_address_clear:
                    if(listener!=null&&tip!=null){
                        listener.onClearClick();
                    }
                    break;
            }
        }
    }

    public interface OnItemClickListener{
        void onAddressClick(Tip tip);
        void onClearClick();
    }
}
