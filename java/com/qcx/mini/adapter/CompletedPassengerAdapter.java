package com.qcx.mini.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseListViewAdapter;
import com.qcx.mini.entity.TravelDetail_DriverEntity;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/1/24.
 */

public class CompletedPassengerAdapter extends BaseListViewAdapter<TravelDetail_DriverEntity.Passenger> {
    private Context context;

    public CompletedPassengerAdapter(Context context) {
        super(context);
        this.context=context;
    }

    public CompletedPassengerAdapter(Context context, List<TravelDetail_DriverEntity.Passenger> datas) {
        super(context, datas);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CompletedPassengerViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_completed_passenger,parent,false);
            holder=new CompletedPassengerViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (CompletedPassengerViewHolder) convertView.getTag();
        }
        holder.bindData(getItem(position));
        return convertView;
    }

    class  CompletedPassengerViewHolder{
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_passengerNum;
        private TextView tv_getOnTime;
        private TextView tv_price;

        public CompletedPassengerViewHolder(View view){
            iv_icon=view.findViewById(R.id.item_completed_passenger_icon);
            tv_name=view.findViewById(R.id.item_completed_passenger_name);
            tv_passengerNum=view.findViewById(R.id.item_completed_passenger_num);
            tv_getOnTime=view.findViewById(R.id.item_completed_passenger_time);
            tv_price=view.findViewById(R.id.item_completed_passenger_price);
        }

        private void bindData(TravelDetail_DriverEntity.Passenger passenger){
            Picasso.with(context)
                    .load(passenger.getPicture())
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(passenger.getNickName());
            tv_getOnTime.setText(DateUtil.formatTime2String(passenger.getGetOnTime(),false));
            tv_passengerNum.setText(String.format(Locale.CHINA,"乘车人数%d",passenger.getBookSeats()));
            tv_price.setText(String.format(Locale.CHINA,"实付款:%s", CommonUtil.formatPrice(passenger.getTicketPrice(),2)));
        }
    }

}
