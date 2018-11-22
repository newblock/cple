package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.MeMessagesEntity;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class MeMessageListAdapter extends BaseRecyclerViewAdapter<MeMessagesEntity.MeMessageEntity,MeMessageListAdapter.MeMessageViewHoler> {
    private Context context;

    public MeMessageListAdapter(Context context) {
        super(context);
        this.context=context;
    }

    public MeMessageListAdapter(Context context, List<MeMessagesEntity.MeMessageEntity> datas) {
        super(context, datas);
        this.context=context;
    }

    @Override
    public MeMessageViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeMessageViewHoler(inflater.inflate(R.layout.item_me_message_list,null));
    }

    @Override
    public void onBindViewHolder(MeMessageViewHoler holder, int position) {
        holder.bindData(getItem(position));
    }

    class MeMessageViewHoler extends RecyclerView.ViewHolder{
        private MeMessagesEntity.MeMessageEntity entity;

        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_type;
        private TextView tv_message;
        private View v_travel;
        private TextView tv_travelTime;
        private TextView tv_travelAddress;
        private TextView tv_travelSeat;
        private TextView tv_travelPrice;
        private TextView tv_time;

        public MeMessageViewHoler(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.item_me_message_icon);
            tv_name=itemView.findViewById(R.id.item_me_message_name);
            tv_type=itemView.findViewById(R.id.item_me_message_type);
            tv_message=itemView.findViewById(R.id.item_me_message_message);
            v_travel=itemView.findViewById(R.id.item_me_message_travel);
            tv_travelTime=itemView.findViewById(R.id.item_me_message_travel_time);
            tv_travelAddress=itemView.findViewById(R.id.item_me_message_travel_address);
            tv_travelSeat=itemView.findViewById(R.id.item_me_message_travel_seatNum);
            tv_travelPrice=itemView.findViewById(R.id.item_me_message_travel_price);
            tv_time=itemView.findViewById(R.id.item_me_message_message_time);
        }

        void bindData(MeMessagesEntity.MeMessageEntity entity){
            this.entity=entity;
            setData();
        }

        private void setData(){
            Picasso.with(context)
                    .load(entity.getPicture())
                    .resize(ConstantValue.ICON_RESIZE,ConstantValue.ICON_RESIZE)
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(entity.getNickName());

            if(entity.getType()==0){
                tv_type.setText("评论了");
            }else if(entity.getType()==1){
                tv_type.setText("点赞了");
            }else if(entity.getType()==2){
                tv_type.setText("分享了");
            }else {
                tv_type.setText("");
            }
            if(TextUtils.isEmpty(entity.getComment())){
                tv_message.setVisibility(View.GONE);
            }else {
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(entity.getComment());
            }

            tv_time.setText(entity.getCreatTime());
            if(entity.getDriverTravel()==null){
                v_travel.setVisibility(View.GONE);
            }else {
                v_travel.setVisibility(View.VISIBLE);
                MeMessagesEntity.TravelEntity travel=entity.getDriverTravel();
                if(travel.getDriverPhone()!=0){
                    tv_travelTime.setText("车找人 ".concat(DateUtil.getTimeStr(travel.getStartTime(),"MM月dd日HH:mm")));
                    tv_travelSeat.setText("座位数:".concat(String.valueOf(travel.getSeats())));
                    tv_travelPrice.setText("票价:".concat(CommonUtil.formatPrice(travel.getTravelPrice(),2)));
                }else {
                    tv_travelTime.setText("人找车 ".concat(DateUtil.getTimeStr(travel.getStartTime(),"MM月dd日HH:mm")));
                    tv_travelSeat.setText("乘车人数:".concat(String.valueOf(travel.getSeats())));
                    tv_travelPrice.setText("车费:".concat(CommonUtil.formatPrice(travel.getPrice(),2)));
                }
                tv_travelAddress.setText(String.format("%s 到 %s",travel.getStartAddress(),travel.getEndAddress()));
            }


        }


    }
}
