package com.qcx.mini.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.TravelDetail_PassengerEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class TravelDetail_Completed_PassengerActivity extends BaseActivity {
    TravelDetail_PassengerEntity travelDetail;

    @BindView(R.id.travel_detail_completed_passenger_time)
    TextView tv_time;
    @BindView(R.id.travel_detail_completed_passenger_startAddress)
    TextView tv_startAddress;
    @BindView(R.id.travel_detail_completed_passenger_endAddress)
    TextView tv_endAddress;
    @BindView(R.id.travel_detail_completed_passenger_price)
    TextView tv_price;
    @BindView(R.id.travel_detail_completed_passenger_driverIcon)
    ImageView iv_driverIcon;
    @BindView(R.id.travel_detail_completed_passenger_driverName)
    TextView tv_driverName;
    @BindView(R.id.travel_detail_completed_passenger_driverLastOnline)
    TextView tv_driverLastOnLine;
    @BindView(R.id.travel_detail_completed_passenger_driverCar)
    TextView tv_driverCar;
    @BindView(R.id.travel_detail_completed_passenger_passengerNum)
    TextView tv_passengerNum;

    @OnClick(R.id.travel_detail_completed_passenger_help)
    void help(){
        DialogUtil.call(this,null);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_detail__completed__passenger;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("",true,false);
        getData(getIntent().getStringExtra("ordersTravelId"));
    }

    private void getData(String ordersID){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("ordersId",ordersID);
        Request.post(URLString.ordersCompletedDetail, params,new EntityCallback(TravelDetail_PassengerEntity.class) {
            @Override
            public void onSuccess(Object t) {
                try{
                    travelDetail= (TravelDetail_PassengerEntity) t;
                    bindData(travelDetail);
                }catch (Exception e){
                    e.printStackTrace();
                    onError("");
                }
            }

            @Override
            public void onError(String errorInfo) {

            }
        });
    }

    private void bindData(TravelDetail_PassengerEntity data){
        tv_startAddress.setText(data.getTravel().getRecommendStartAddress());
        tv_endAddress.setText(data.getTravel().getRecommendEndAddress());
        tv_price.setText(CommonUtil.formatPrice(data.getTravel().getPrice(),2));
        tv_driverCar.setText(data.getTravel().getCar());
        tv_driverLastOnLine.setText(DateUtil.formatTime2String(data.getTravel().getLastTimeOnline(),false));
        tv_driverName.setText(data.getTravel().getNickname());
        tv_passengerNum.setText(String.valueOf(data.getTravel().getBookSeats()));
        Picasso.with(this)
                .load(data.getTravel().getPicture())
                .error(R.mipmap.img_me)
                .into(iv_driverIcon);
        String timeStr=DateUtil.getTimeStr(data.getTravel().getFinalTime(),"MM月dd日HH:mm");
        switch (data.getTravel().getOrdersStatus()){
            case 0:
            case 8:
                timeStr=timeStr+"行程已结束";
                break;
        }
        tv_time.setText(timeStr);
    }

}
