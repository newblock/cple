package com.qcx.mini.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.WayListEntity;
import com.qcx.mini.listener.OnGroupClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Administrator on 2018/4/3.
 */

public class ItemMyWayAdapter extends BaseRecyclerViewAdapter<WayListEntity.GroupItem, ItemMyWayAdapter.ItemMyWayViewHolder> {
    private OnGroupClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public ItemMyWayAdapter(Context context) {
        super(context);
    }

    public ItemMyWayAdapter(Context context, List<WayListEntity.GroupItem> datas) {
        super(context, datas);
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas.size() > 6 ? 6 : datas.size();
    }

    public void setListener(OnGroupClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ItemMyWayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemMyWayViewHolder(inflater.inflate(R.layout.item_my_way, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemMyWayViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemMyWayViewHolder extends BaseRecycleViewHolder implements View.OnClickListener {
        private WayListEntity.GroupItem item;
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_num;
        private ImageView iv_add;

        public ItemMyWayViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.item_my_way_icon);
            tv_name = itemView.findViewById(R.id.item_my_way_name);
            tv_num = itemView.findViewById(R.id.item_my_way_num);
            iv_add = itemView.findViewById(R.id.item_my_way_add);

            iv_icon.setOnClickListener(this);
            tv_name.setOnClickListener(this);
            iv_add.setOnClickListener(this);
        }

        @Override
        public void bindData(Object data,Params params) {
            try {
                item = (WayListEntity.GroupItem) data;
                Picasso.with(context)
                        .load(item.getGroupBanner())
                        .placeholder(R.mipmap.img_me)
                        .error(R.mipmap.img_me)
                        .resize(ConstantValue.ICON_RESIZE_V2, ConstantValue.ICON_RESIZE_V2)
                        .into(iv_icon);
                tv_name.setText(item.getGroupTitle());
                tv_num.setVisibility(View.GONE);
                if (item.isJoined()) {
                    iv_add.setVisibility(View.GONE);
                } else {
                    iv_add.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_my_way_icon:
                case R.id.item_my_way_name:
                    if (listener != null) {
                        listener.onItemClick(item.getGroupId(), item.getGroupType());
                    }
                    break;
                case R.id.item_my_way_add:
                    if (listener != null) {
                        listener.onAddClick(item.getGroupId(), iv_add);
                    }
                    break;

            }
        }
    }
}
