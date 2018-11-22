package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.AlmightyRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemTravelViewHolder;
import com.qcx.mini.adapter.viewHolder.ItemCompletedTravelPassengerViewHolder;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.ReleaseTypeDialog;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.TravelDetail_DriverEntity;
import com.qcx.mini.entity.TravelDetail_PassengerEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.entity.factory.TravelEntityFactory;
import com.qcx.mini.listener.ItemUserInfoClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.NetUtil;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.StatusBarUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.itemDecoration.QuItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class TravelHistoryDetailActivity extends BaseActivity {
    private int travelType;
    private AlmightyRecyclerViewAdapter adapter;
    private ItemTravelViewHolder travelViewHolder;

    private TravelDetail_DriverEntity driverTravel;
    private TravelDetail_PassengerEntity passengerTravel;

    @BindView(R.id.travel_detail_history_passenger_list)
    RecyclerView rv_passenger;
    @BindView(R.id.travel_detail_history_driver_info)
    View v_driverInfo;

    @BindView(R.id.travel_history_detail_travel_info)
    View v_travel;
    @BindView(R.id.travel_history_detail_people_type)
    TextView tv_peopleType;
    @BindView(R.id.travel_detail_history_describe)
    TextView tv_describe;
    @BindView(R.id.travel_detail_history_income)
    TextView tv_income;
    @BindView(R.id.travel_detail_history_price_type)
    TextView tv_priceType;

    @BindView(R.id.item_driver_info_icon)
    ImageView iv_driverIcon;
    @BindView(R.id.item_driver_info_sex)
    ImageView iv_driverSex;
    @BindView(R.id.item_driver_info_attention)
    ImageView iv_driverAttention;
    @BindView(R.id.item_driver_info_name)
    TextView tv_driverName;
    @BindView(R.id.item_driver_info_carNum)
    TextView tv_driverCarNum;
    @BindView(R.id.item_driver_info_info)
    TextView tv_driverInfo;


    @OnClick(R.id.travel_detail_history_release)
    void release(){
        new ReleaseTypeDialog()
                .show(getSupportFragmentManager(),"");
    }

    @OnClick(R.id.travel_detail_history_help)
    void help(){
        DialogUtil.call(this,null);
    }

    @OnClick(R.id.item_driver_info_icon)
    void driverIcon(){
        if(passengerTravel==null||passengerTravel.getTravel()==null){
            return;
        }
        userInfoClickListener.onIconClick(passengerTravel.getTravel().getDriverPhone());
    }
    @OnClick(R.id.item_driver_info_attention)
    void driverAttention(){

        if(passengerTravel==null||passengerTravel.getTravel()==null){
            return;
        }

        userInfoClickListener.onAttentionClick(passengerTravel.getTravel().isAttention(),passengerTravel.getTravel().getDriverPhone(),iv_driverAttention);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_history_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        long travelId = getIntent().getLongExtra("travelId", 0);
        travelType=getIntent().getIntExtra("travelType",0);
        String ordersId = getIntent().getStringExtra("ordersId");
        initTitleBar("",true,false);
        StatusBarUtil.setColor(this, Color.WHITE,0);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        if(travelType== ConstantValue.TravelType.DRIVER){
            rv_passenger.setVisibility(View.VISIBLE);
            v_driverInfo.setVisibility(View.GONE);
            tv_peopleType.setText("乘客信息");
            tv_priceType.setText("总收入");

            rv_passenger.setLayoutManager(manager);
            QuItemDecoration itemDecoration=new QuItemDecoration(0,0, UiUtils.getSize(16),UiUtils.getSize(16),UiUtils.getSize(1),0);
            itemDecoration.setColor(getResources().getColor(R.color.gray_line));
            rv_passenger.addItemDecoration(itemDecoration);
            adapter=new AlmightyRecyclerViewAdapter(this);
            rv_passenger.setAdapter(adapter);
            getDriverTravelDetail(travelId);
        }else {
            rv_passenger.setVisibility(View.GONE);
            v_driverInfo.setVisibility(View.VISIBLE);
            tv_peopleType.setText("车主信息");
            tv_priceType.setText("实付款");
            getPassengerTravelDetail(ordersId);
        }
        travelViewHolder=new ItemTravelViewHolder(v_travel);
    }

    private void getPassengerTravelDetail(String ordersID){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("ordersId",ordersID);
        Request.post(URLString.ordersCompletedDetail, params,new EntityCallback(TravelDetail_PassengerEntity.class) {
            @Override
            public void onSuccess(Object t) {
                try{
                    passengerTravel= (TravelDetail_PassengerEntity) t;
                    TravelEntity entity=TravelEntityFactory.creatTravel(passengerTravel);
                    entity.setStatus(-1);
                    travelViewHolder.bindData(entity);
                    TravelDetail_PassengerEntity.Travel travel=passengerTravel.getTravel();
                    switch (travel.getOrdersStatus()){
                        case  ConstantValue.OrdersStatus.FINAL:
                        case  ConstantValue.OrdersStatus.PASSENGER_DOWN:
                            initTitleBar("已完成",true,false);
                            break;
                        default:
                            initTitleBar("已关闭",true,false);
                            break;
                    }
                    Picasso.with(TravelHistoryDetailActivity.this)
                            .load(travel.getPicture())
                            .resize(ConstantValue.ICON_RESIZE,ConstantValue.ICON_RESIZE)
                            .error(R.mipmap.img_me)
                            .into(iv_driverIcon);
                    CommonUtil.setSexImg(travel.getSex(),iv_driverSex);
                    tv_driverName.setText(travel.getNickname());
                    tv_driverCarNum.setText(travel.getCarNumber());
                    tv_driverInfo.setText(travel.getCar());
                    if(travel.isAttention()){
                        iv_driverAttention.setImageResource(R.mipmap.btn_followed_mini);
                    }else {
                        iv_driverAttention.setImageResource(R.mipmap.btn_follow_mini);
                    }
                    tv_income.setText(CommonUtil.formatPrice(travel.getPrice(),1));
                    switch (travel.getOrdersStatus()){
                        case  ConstantValue.OrdersStatus.FINAL:
                        case  ConstantValue.OrdersStatus.PASSENGER_DOWN:
                            break;

                    }
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

    private void getDriverTravelDetail(long travelId){
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        Request.post(URLString.driverTravelDetail, params, new EntityCallback(TravelDetail_DriverEntity.class) {
            @Override
            public void onSuccess(Object t) {
                driverTravel= (TravelDetail_DriverEntity) t;
                if(driverTravel!=null){
                    List<TravelDetail_DriverEntity.Passenger> passengers=driverTravel.getPassengers();
                    adapter.clear();
                    adapter.addData(ItemCompletedTravelPassengerViewHolder.class,passengers,R.layout.item_completed_travel_passenger_info,userInfoClickListener);
                    TravelEntity travel=TravelEntityFactory.creatTravel(driverTravel);
                    travel.setStatus(-1);//隐藏掉状态图片
                    travelViewHolder.bindData(travel);
                    if(driverTravel.getTravel().getTravelStatus()==4){
                        tv_describe.setVisibility(View.VISIBLE);
                        if(driverTravel.getPassengers()==null||driverTravel.getPassengers().size()==0){
                            tv_describe.setText("无人订座行程自动关闭");
                            initTitleBar("已关闭",true,false);
                        }else {
                            tv_describe.setText("行程已完成");
                            initTitleBar("已完成",true,false);
                        }
                    }else {
                        tv_describe.setVisibility(View.GONE);
                    }
                    tv_income.setText(CommonUtil.formatPrice(driverTravel.getTravel().getDriverIncome(),1));
                }
            }
        });
    }

    ItemUserInfoClickListener userInfoClickListener=new ItemUserInfoClickListener() {
        @Override
        public void onIconClick(long phone) {

            Intent intent=new Intent(TravelHistoryDetailActivity.this, UserInfoActivity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }

        @Override
        public void onAttentionClick(boolean isAttention, final long phone, final ImageView attentionView) {
            if(isAttention){
                NetUtil.cancelAttention(phone, new EntityCallback(IntEntity.class) {
                    @Override
                    public void onSuccess(Object t) {
                        IntEntity intEntity= (IntEntity) t;
                        if(intEntity.getStatus()==200){
                            if(travelType==ConstantValue.TravelType.DRIVER){
                                for(TravelDetail_DriverEntity.Passenger passenger:driverTravel.getPassengers()){
                                    if(passenger.getPassengerPhone()==phone){
                                        passenger.setAttention(false);
                                    }
                                }
                            }else {
                                passengerTravel.getTravel().setAttention(false);
                            }
                            attentionView.setImageResource(R.mipmap.btn_follow_mini);
                        }
                    }
                });
            }else {
                NetUtil.attention(phone, new EntityCallback(IntEntity.class) {
                    @Override
                    public void onSuccess(Object t) {
                        IntEntity intEntity= (IntEntity) t;
                        if(intEntity.getStatus()==200){
                            if(travelType==ConstantValue.TravelType.DRIVER){
                                for(TravelDetail_DriverEntity.Passenger passenger:driverTravel.getPassengers()){
                                    if(passenger.getPassengerPhone()==phone){
                                        passenger.setAttention(true);
                                    }
                                }
                            }else {
                                passengerTravel.getTravel().setAttention(true);
                            }
                            attentionView.setImageResource(R.mipmap.btn_followed_mini);
                        }
                    }
                });
            }
        }
    };


}
