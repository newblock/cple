package com.qcx.mini.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.TravelDetail_DriverEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemUserInfoClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2018/7/2.
 */

public class ItemCompletedTravelPassengerViewHolder extends BaseRecycleViewHolder {
    TravelDetail_DriverEntity.Passenger passenger;
    ItemUserInfoClickListener listener;

    private ImageView iv_icon,iv_attention;
    private TextView tv_name,tv_describe;

    public ItemCompletedTravelPassengerViewHolder(View itemView) {
        super(itemView);
        tv_name=itemView.findViewById(R.id.item_user_info_name);
        tv_describe=itemView.findViewById(R.id.item_completed_travel_passenger_info_describe);
        iv_icon=itemView.findViewById(R.id.item_user_info_icon);
        iv_attention=itemView.findViewById(R.id.item_user_info_attention);
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passenger!=null&&listener!=null){
                    listener.onIconClick(passenger.getPassengerPhone());
                }
            }
        });
        iv_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passenger!=null&&listener!=null){
                    listener.onAttentionClick(passenger.isAttention(),passenger.getPassengerPhone(),iv_attention);
                }
            }
        });
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        try {
            this.listener= (ItemUserInfoClickListener) holederListener;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindData(Object data,Params params) {
        if(data==null){
            return;
        }
        try{
            passenger= (TravelDetail_DriverEntity.Passenger) data;
            Picasso.with(holderContext)
                    .load(passenger.getPicture())
                    .resize(ConstantValue.ICON_RESIZE,ConstantValue.ICON_RESIZE)
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
            String info=passenger.getNickName();
            switch (passenger.getSex()){
                case ConstantValue.SexType.MAN:
                    info+="·男";
                    break;
                case ConstantValue.SexType.WOMAN:
                    info+="·女";
                    break;
            }
            tv_name.setText(info);
            if(passenger.isAttention()){
                iv_attention.setImageResource(R.mipmap.btn_followed_mini);
            }else {
                iv_attention.setImageResource(R.mipmap.btn_follow_mini);
            }
            StringBuilder describe=new StringBuilder();
            switch (passenger.getOrderStatus()){
                case ConstantValue.OrdersStatus.NO_PAY:
                case  ConstantValue.OrdersStatus.PAY_TIMEOUT:
                    describe.append("乘客未支付");
                    break;
                case  ConstantValue.OrdersStatus.FINAL:
                case  ConstantValue.OrdersStatus.PASSENGER_DOWN:
                    describe.append("行程已完成");
                    break;
                case  ConstantValue.OrdersStatus.REFUND:
                    describe.append("已退订");
                    break;
            }
            describe.append(" ");
            describe.append(passenger.getBookSeats());
            describe.append("座");
            describe.append(" 车费：");
            describe.append(passenger.getTicketPrice());
            describe.append("元");
            tv_describe.setText(describe.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
