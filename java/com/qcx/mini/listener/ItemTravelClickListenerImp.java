package com.qcx.mini.listener;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.User;
import com.qcx.mini.activity.TravelDetail_DriverActivity;
import com.qcx.mini.activity.TravelDetail_PassengerActivity;
import com.qcx.mini.activity.TravelNoneActivity;
import com.qcx.mini.entity.TravelEntity;

/**
 * Created by Administrator on 2018/8/15.
 */

public class ItemTravelClickListenerImp implements ItemTravelClickListener {
    private Context context;

    public ItemTravelClickListenerImp(Context context){
        this.context=context;
    }
    @Override
    public void onTravelClick(TravelEntity data) {
        long phone= User.getInstance().getPhoneNumber();
        if(phone==data.getTravelPhone()){//自己的行程
            Intent intent;
            switch (data.getStatus()) {
                case 0://正在寻找乘客
                case 3://乘客发布的行程，等待接单
                    intent = new Intent(context, TravelNoneActivity.class);
                    intent.putExtra("travelId", data.getTravelId());
                    int travelType;
                    if(data.getType()==0||data.getType()==3){
                        travelType= ConstantValue.TravelType.DRIVER;
                    }else {
                        travelType= ConstantValue.TravelType.PASSENGER;
                    }
                    intent.putExtra("travelType", travelType);
                    context.startActivity(intent);
                    break;
                case 1://有乘客下单
                case 2://已发车，行程中
                case 9://车主抢单，乘客未支付
                case 11://车主抢单，乘客已支付
                case 12://车主抢单，已发车，行车中
                    intent = new Intent(context, TravelDetail_DriverActivity.class);
                    intent.putExtra("travelID", data.getTravelId());
                    context.startActivity(intent);
                    break;
                case 5://乘客订的车主的行程，未支付
                case 4://乘客发布的行程，车主已抢单，等待支付
                case 6://乘客订的车主的行程，已支付
                case 7://乘客订的车主的行程，已发车
                case 8://乘客订的车主的行程，行程中
                    intent = new Intent(context, TravelDetail_PassengerActivity.class);
                    intent.putExtra("travelID", data.getTravelId());
                    intent.putExtra("ordersID", data.getOrdersTravelId());
                    context.startActivity(intent);
                    break;
            }
        }

    }
}
