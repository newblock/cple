package com.qcx.mini.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.CompletedPassengerAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.HeadEntity;
import com.qcx.mini.entity.TravelDetail_DriverEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class TravelDetail_Completed_DriverActivity extends BaseActivity {
    TravelDetail_DriverEntity mTravelDetail;
    private List<HeadEntity> mPassengers;
    private CompletedPassengerAdapter mAdapter;
    private ImageView[] heads;

    @BindView(R.id.travel_detail_completed_driver_time)
    TextView tv_time;
    @BindView(R.id.travel_detail_completed_driver_startAddress)
    TextView tv_startAddress;
    @BindView(R.id.travel_detail_completed_driver_endAddress)
    TextView tv_endAddress;
    @BindView(R.id.travel_detail_completed_driver_income)
    TextView tv_income;
    @BindView(R.id.travel_detail_completed_driver_list)
    ListView lv_passengers;
    @BindView(R.id.travel_detail_completed_driver_head1)
    ImageView iv_head1;
    @BindView(R.id.travel_detail_completed_driver_head2)
    ImageView iv_head2;
    @BindView(R.id.travel_detail_completed_driver_head3)
    ImageView iv_head3;
    @BindView(R.id.travel_detail_completed_driver_head4)
    ImageView iv_head4;

    @OnClick(R.id.travel_detail_completed_driver_help)
    void help(){
        DialogUtil.call(this,null);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_detail__completed__driver;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("",true,false);
        mAdapter=new CompletedPassengerAdapter(this);
        lv_passengers.setAdapter(mAdapter);
        heads=new ImageView[]{iv_head1,iv_head2,iv_head3,iv_head4};
        long travelId=getIntent().getLongExtra("travelId",0);
        getData(travelId);
    }


    private void getData(long travelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        Request.post(URLString.driverTravelDetail, params, new EntityCallback(TravelDetail_DriverEntity.class) {
            @Override
            public void onSuccess(Object t) {
                try {
                    mTravelDetail = (TravelDetail_DriverEntity) t;
                    mPassengers=getPassengers(mTravelDetail);
                    setHeadImg(mTravelDetail.getTravel().getSeats(),mPassengers);
                    mAdapter.setDatas(mTravelDetail.getPassengers());
                    tv_startAddress.setText(mTravelDetail.getTravel().getStartAddress());
                    tv_endAddress.setText(mTravelDetail.getTravel().getEndAddress());
                    tv_income.setText(CommonUtil.formatPrice(mTravelDetail.getTravel().getDriverIncome(),2));
                    String timeStr=DateUtil.getTimeStr(mTravelDetail.getTravel().getFinalTime(),"MM月dd日HH:mm");
                    switch (mTravelDetail.getTravel().getTravelStatus()){
                        case -1:
                            timeStr=timeStr+"行程已取消";
                            break;
                        case 4:
                            timeStr=timeStr+"行程已结束";
                            break;
                    }
                    tv_time.setText(timeStr);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorInfo) {

            }
        });
    }



    private List<HeadEntity> getPassengers(TravelDetail_DriverEntity data){
        List<HeadEntity> datas=new ArrayList<>();
        datas.clear();
        if(data.getPassengers()!=null&&data.getPassengers().size()>0){
            for(int i=0;i<data.getPassengers().size();i++){
                for (int j=0;j<data.getPassengers().get(i).getBookSeats();j++){
                    TravelDetail_DriverEntity.Passenger passenger=data.getPassengers().get(i);
                    HeadEntity entity=new HeadEntity();
                    entity.setLastOnline(passenger.getLastTimeOnline());
                    entity.setName(passenger.getNickName());
                    entity.setPicture(passenger.getPicture());
                    entity.setPhone(passenger.getPassengerPhone());
                    entity.setStatusText(passenger.getOrderStatusDetail());
                    entity.setOrderStatus(passenger.getOrderStatus());
                    entity.setPrice(passenger.getTicketPrice());
//                    if(passenger.getOrderStatus()==-1){
//                        entity.setPrice(String.format("待付款：%s元", CommonUtil.formatPrice(passenger.getTicketPrice(),2)));
//                    }else {
//                        entity.setPrice(String.format("实付款：%s元", CommonUtil.formatPrice(passenger.getTicketPrice(),2)));
//                    }
                    datas.add(entity);
                }
            }
        }

        if (data.getTravel()!=null){
            for(int i=datas.size();i<data.getTravel().getSeats();i++){
                datas.add(null);
            }
        }
        return datas;
    }


    private void setHeadImg(int seats,List<HeadEntity> mPassengers){
        for(int i=0;i<heads.length;i++){
            if(i<seats) {
                heads[i].setVisibility(View.VISIBLE);
                if(mPassengers.get(i)!=null&&i<mPassengers.size()) {
                    setHeadImg(heads[i],mPassengers.get(i).getPicture());
                }else {
                    heads[i].setImageResource(R.mipmap.img_haveseat);
                }
            }
            else {
                heads[i].setVisibility(View.GONE);
            }
        }
    }

    private void setHeadImg(ImageView iv,String picURL){
        try {
            Picasso.with(this)
                    .load(picURL)
                    .error(R.mipmap.img_me)
                    .into(iv);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
