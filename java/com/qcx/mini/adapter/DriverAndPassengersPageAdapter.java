package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.entity.TravelDetail_PassengerEntity;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class DriverAndPassengersPageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private View v_driverInfo;
    private View v_passengers;
    private TravelDetail_PassengerEntity.Travel travel;
    private List<TravelDetail_PassengerEntity.Passenger> passengers;
    private TravelDetail_PassengerEntity travelDetail;
    private PassengerTravelDetailHeadsAdapter mAdapter;

    private CircleImageView iv_icon;
    private TextView tv_name;
    private TextView tv_startTime;
    private TextView tv_lastOnLine;
    private TextView tv_carInfo;
    private ImageView iv_sex;

    private RecyclerView rv_passengers;

    private boolean isOnlyOne;

    public DriverAndPassengersPageAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        v_driverInfo=inflater.inflate(R.layout.page_passenger_travel_detail_driver_info,null);
        v_passengers=inflater.inflate(R.layout.page_passenger_travel_passengers,null);
        iv_sex=v_driverInfo.findViewById(R.id.page_passenger_travel_detail_driver_info_sex);
        iv_icon=v_driverInfo.findViewById(R.id.page_passenger_travel_detail_driver_info_icon);
        tv_name=v_driverInfo.findViewById(R.id.page_passenger_travel_detail_driver_info_name);
        tv_lastOnLine=v_driverInfo.findViewById(R.id.page_passenger_travel_detail_driver_info_lastOnLine);
        tv_carInfo=v_driverInfo.findViewById(R.id.page_passenger_travel_detail_driver_info_carInfo);
        tv_startTime=v_driverInfo.findViewById(R.id.page_passenger_travel_detail_driver_info_startTime);
        rv_passengers=v_passengers.findViewById(R.id.page_passenger_travel_passengers_recyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_passengers.setLayoutManager(manager);
        mAdapter=new PassengerTravelDetailHeadsAdapter(context);
        rv_passengers.setAdapter(mAdapter);
    }

    public void setOnHeadsClickListener(OnHeadIconClickListener listener){
        mAdapter.setListener(listener);
    }

    public void setOnlyOne(boolean onlyOne) {
        isOnlyOne = onlyOne;
        notifyDataSetChanged();
    }

    public void setTravelDetail(TravelDetail_PassengerEntity travelDetail) {
        this.travelDetail = travelDetail;
        setDriverInfo(travelDetail.getTravel());
        setPassengersInfo(travelDetail.getSameWayPassengers());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return isOnlyOne ? 1 : 2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(position==0?v_driverInfo:v_passengers);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position==0){
            container.addView(v_driverInfo);
            return v_driverInfo;
        }else {
            container.addView(v_passengers);
            return v_passengers;
        }
    }

    private void setDriverInfo(TravelDetail_PassengerEntity.Travel travel){
        if(travel!=null){
            tv_name.setText(travel.getNickname());
            tv_lastOnLine.setText(DateUtil.formatTime2String(travel.getLastTimeOnline(),false));
            tv_startTime.setText(String.format("%s%s发车",DateUtil.formatDay("MM月dd日",travel.getStartTime()),DateUtil.getTimeStr(travel.getStartTime(),"HH:mm")));
            tv_carInfo.setText(String.format("%s%s",travel.getCar(),travel.getCarNumber()));
            Picasso.with(context)
                    .load(travel.getPicture())
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
        }else {
            tv_name.setText("暂无车主接单");
            tv_name.setTextColor(0xFFDADDE2);
            tv_lastOnLine.setText("暂无");
            tv_carInfo.setText("暂无车主接单");
            tv_lastOnLine.setTextColor(0x00000000);
            tv_carInfo.setTextColor(0x00000000);
            tv_lastOnLine.setBackground(context.getResources().getDrawable(R.drawable.bg_circular_gray1));
            tv_carInfo.setBackground(context.getResources().getDrawable(R.drawable.bg_circular_gray1));
            tv_startTime.setVisibility(View.GONE);
            iv_icon.setImageResource(R.mipmap.img_wait);
            iv_sex.setVisibility(View.GONE);
        }
    }

    private void setPassengersInfo(List<TravelDetail_PassengerEntity.Passenger> passengers){
        mAdapter.setDatas(passengers);
    }

    public interface OnHeadIconClickListener{
        void onClick(long phone);
    }
}
