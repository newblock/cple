package com.qcx.mini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.activity.TravelDetail_NoPayActivity;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.GroupInfoEntity;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/4/8.
 */

public class ItemGroupTravelsAdapter extends BaseRecyclerViewAdapter<GroupInfoEntity.GroupTravelEntity, ItemGroupTravelsAdapter.ItemGroupTravelsViewHolder> {

    private BaseRecycleViewHolder.Params params;

    public ItemGroupTravelsAdapter(Context context) {
        super(context);
    }

    public ItemGroupTravelsAdapter(Context context, List<GroupInfoEntity.GroupTravelEntity> datas) {
        super(context, datas);
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    @Override
    public ItemGroupTravelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemGroupTravelsViewHolder(inflater.inflate(R.layout.item_group_travels, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemGroupTravelsViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemGroupTravelsViewHolder extends BaseRecycleViewHolder {
        GroupInfoEntity.GroupTravelEntity groupEntity;
        private ImageView iv_icon;
        private TextView tv_time;
        private TextView tv_startAddress;
        private TextView tv_endAddress;
        private TextView tv_info;
        private TextView tv_oper;

        public ItemGroupTravelsViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.item_group_travels_icon);
            tv_time = itemView.findViewById(R.id.item_group_travels_start_time);
            tv_startAddress = itemView.findViewById(R.id.item_group_travels_start_address);
            tv_endAddress = itemView.findViewById(R.id.item_group_travels_end_address);
            tv_info = itemView.findViewById(R.id.item_group_travels_info);
            tv_oper = itemView.findViewById(R.id.item_group_travels_oper);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(groupEntity!=null){
                        Intent intent=new Intent(context, TravelDetail_NoPayActivity.class);
                        intent.putExtra("travelId",groupEntity.getTravelId());
                        intent.putExtra("travelType",groupEntity.getTravelType());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            groupEntity = (GroupInfoEntity.GroupTravelEntity) data;
            Picasso.with(context)
                    .load(groupEntity.getPicture())
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .error(R.mipmap.img_me)
                    .placeholder(R.mipmap.img_me)
                    .into(iv_icon);
            tv_time.setText(DateUtil.getTimeStr(groupEntity.getStartTime(), "HH:mm"));
            tv_startAddress.setText(groupEntity.getStartAddress());
            tv_endAddress.setText(groupEntity.getEndAddress());
            String info = "";
            if (groupEntity.getTravelType() == ConstantValue.TravelType.DRIVER) {
                if (groupEntity.getSurplusSeats() == 0) {//售光
                    info = String.format(Locale.CHINA, "每座%s元 已售光", CommonUtil.formatPrice(groupEntity.getPrice(),2));
                    tv_info.setTextColor(0xFFABB1BA);
                    tv_oper.setVisibility(View.GONE);
                } else {
                    info = String.format(Locale.CHINA, "%d座余%d 车费%s元",groupEntity.getSeats(), groupEntity.getSurplusSeats(), CommonUtil.formatPrice(groupEntity.getPrice(),2));
                    tv_info.setTextColor(0xFF484848);
                    tv_oper.setVisibility(View.VISIBLE);
                    tv_oper.setText("订座");
                }
            } else {
                if (groupEntity.getStatus() != 0) {
                    info = String.format(Locale.CHINA, "车费%s元 被抢单",CommonUtil.formatPrice(groupEntity.getPrice(),2));
                    tv_info.setTextColor(0xFFABB1BA);
                    tv_oper.setVisibility(View.GONE);
                } else {
                    info = String.format(Locale.CHINA, "人数%d 车费%s元", groupEntity.getSurplusSeats(), CommonUtil.formatPrice(groupEntity.getPrice(),2));
                    tv_info.setTextColor(0xFF484848);
                    tv_oper.setVisibility(View.VISIBLE);
                    tv_oper.setText("抢单");
                }
            }
            tv_info.setText(info);
        }
    }
}
