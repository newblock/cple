package com.qcx.mini.adapter.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemTravelClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 *   item id:item_travel_2
 */

public class ItemTravelViewHolder extends BaseRecycleViewHolder implements View.OnClickListener{
    private ItemTravelClickListener listener;
    TravelEntity travel;

    private TextView tv_travelStartTime;
    private ImageView iv_travelStatus;
    private TextView tv_travelStartAddress;
    private TextView tv_travelEndAddress;
    private View v_passengerTravelView;
    private TextView tv_passengerNum;
    private ImageView[] passengerIcons;
    public TextView tv_travelPriceType;
    private TextView tv_travelPrice;
    private View v_travel;
    private TextView tv_timeRightText;
    private TextView tv_startRightText;
    private TextView tv_endRightText;

    public ItemTravelViewHolder(View itemView) {
        super(itemView);
        v_travel = itemView.findViewById(R.id.item_travel_view);
        tv_travelStartTime = itemView.findViewById(R.id.item_travel_start_time);
        iv_travelStatus = itemView.findViewById(R.id.item_travel_status);
        tv_travelStartAddress = itemView.findViewById(R.id.item_travel_startAddress);
        tv_travelEndAddress = itemView.findViewById(R.id.item_travel_endAddress);
        v_passengerTravelView = itemView.findViewById(R.id.item_travel_peoples_view);
        tv_passengerNum = itemView.findViewById(R.id.item_travel_peoples_text);
        passengerIcons = new ImageView[]{
                itemView.findViewById(R.id.item_travel_headImg1)
                , itemView.findViewById(R.id.item_travel_headImg2)
                , itemView.findViewById(R.id.item_travel_headImg3)
                , itemView.findViewById(R.id.item_travel_headImg4)
        };
        tv_travelPriceType = itemView.findViewById(R.id.item_travel_price_type);
        tv_travelPrice = itemView.findViewById(R.id.item_travel_price);
        tv_timeRightText=itemView.findViewById(R.id.item_travel_time_right_text);
        tv_startRightText=itemView.findViewById(R.id.item_travel_start_right_text);
        tv_endRightText=itemView.findViewById(R.id.item_travel_end__right_text);
    }

    protected void setClicklistener(){
        v_travel.setOnClickListener(this);
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        if(holederListener instanceof ItemTravelClickListener){
            this.listener=(ItemTravelClickListener) holederListener;
        }
    }

    @Override
    public void bindData(Object data,Params params) {
        if(data instanceof TravelEntity){
            setTravelInfo((TravelEntity) data);
        }else {
            LogUtil.e("bind a null Object or no TravelEntity");
        }
    }

    protected void setTravelInfo(TravelEntity travel){
        this.travel=travel;
        if(travel==null){
            return;
        }

        if (travel.getType() == 0) {//车主行程
            v_passengerTravelView.setVisibility(View.GONE);
            tv_passengerNum.setVisibility(View.GONE);
            tv_travelPriceType.setText("元/座");
            setHeadImg(travel.getSeats(), travel.getHeadPictures());
            switch (travel.getStatus()) {
                case 1://未发车
                case 2:
                    if (0 == travel.getSurplusSeats()) {
                        setStatus(R.mipmap.img_soldout);
                    } else {
                        setStatus(0);
                    }
                    break;
                case 3://已发车
                    setStatus(R.mipmap.img_depart);
                    break;
                case 4://完成
                    setStatus(R.mipmap.img_complete);
                    break;
                default:
                    setStatus(0);
                    break;
            }
        } else {
            setHeadImg(0, null);
            v_passengerTravelView.setVisibility(View.VISIBLE);
            tv_passengerNum.setVisibility(View.VISIBLE);
            tv_travelPriceType.setText("元");
            tv_passengerNum.setText(String.valueOf(travel.getSeats()));
            switch (travel.getStatus()) {
                case 3://等待抢单
                    setStatus(0);
                    break;
                case 4://被抢单
                    setStatus(R.mipmap.img_soldout_chengke);
                    break;
                default:
                    setStatus(0);
                    break;
            }
        }
        tv_travelStartTime.setText(DateUtil.getTimeStr(travel.getStartTime(), "MM月dd日HH:mm"));
        tv_travelStartAddress.setText(travel.getStartAddress());
        tv_travelEndAddress.setText(travel.getEndAddress());
        tv_travelPrice.setText(CommonUtil.formatPrice(travel.getTravelPrice(),1));
        if(!TextUtils.isEmpty(travel.getByWay())){
            tv_timeRightText.setVisibility(View.VISIBLE);
            tv_timeRightText.setText(travel.getByWay());
        }else {
            tv_timeRightText.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(travel.getStartRecommend())){
            tv_startRightText.setVisibility(View.VISIBLE);
            tv_startRightText.setText(travel.getStartRecommend());
        }else {
            tv_startRightText.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(travel.getEndRecommend())){
            tv_endRightText.setVisibility(View.VISIBLE);
            tv_endRightText.setText(travel.getEndRecommend());
        }else {
            tv_endRightText.setVisibility(View.GONE);
        }
    }

    protected void setStatus(int imgID) {
        if (imgID != 0) {
            iv_travelStatus.setVisibility(View.VISIBLE);
            iv_travelStatus.setImageResource(imgID);
        } else {
            iv_travelStatus.setVisibility(View.GONE);
        }
    }

    protected void setHeadImg(int seats, List<String> picURLs) {
        for (int i = 0; i < passengerIcons.length; i++) {
            if (i < seats) {
                passengerIcons[i].setVisibility(View.VISIBLE);
                if (picURLs != null && i < picURLs.size()) {
                    setHeadImg(passengerIcons[i], picURLs.get(i));
                } else {
                    passengerIcons[i].setImageResource(R.mipmap.img_haveseat);
                }
            } else {
                passengerIcons[i].setVisibility(View.GONE);
            }
        }
    }

    protected void setHeadImg(ImageView iv, String picURL) {
        try {
            Picasso.with(holderContext)
                    .load(picURL)
                    .error(R.mipmap.img_me)
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .tag(tag)
                    .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_travel_reserve:
                if(listener!=null){
                    listener.onTravelClick(travel);
                }
                break;
        }
    }
}
