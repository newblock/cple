package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.HeadEntity;
import com.qcx.mini.entity.TravelDetail_DriverEntity;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class TravelDetailHeadsAdapter extends BaseRecyclerViewAdapter<HeadEntity, TravelDetailHeadsAdapter.TravelDetailHeadsViewHolder> {
    private OnItemClickListener listener;
//    private int selectedPosition;
    private long selectedPhone;//-1表示空座
    private final static int ADD_SEAT=-1001;

    public TravelDetailHeadsAdapter(Context context) {
        super(context);
    }

    public void setData(TravelDetail_DriverEntity data){
        datas.clear();

        if(data.getSeckillTravelNopay()!=null){
                    for(int i=0;i<data.getSeckillTravelNopay().getSeats();i++){
                        TravelDetail_DriverEntity.SeckillTravelNopay nopay=data.getSeckillTravelNopay();
                        HeadEntity entity=new HeadEntity();
                        entity.setLastOnline(nopay.getLastTimeOnline());
                        entity.setName(nopay.getNickName());
                        entity.setPicture(nopay.getPicture());
                        entity.setStatusText("未支付");
                        entity.setPrice(nopay.getPrice());
                        entity.setSex(nopay.getSex());
                        entity.setPassengerNum(nopay.getSeats());
                        entity.setStart(nopay.getStart());
                        entity.setStartAddress(nopay.getStartAddress());
                        entity.setEndAddress(nopay.getEndAddress());
                        entity.setEnd(nopay.getEnd());
                        entity.setPhone(nopay.getPassengerPhone());
                        entity.setAttention(nopay.isAttention());
                        entity.setStartTime(nopay.getStartTime());
                        datas.add(entity);
                    }
                }

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
                            entity.setStart(passenger.getStart());
                            entity.setEnd(passenger.getEnd());
                            entity.setStartAddress(passenger.getStartAddress());
                            entity.setEndAddress(passenger.getEndAddress());
                            entity.setOrdersId(passenger.getOrdersTravelId());
                            entity.setLocation(passenger.getLocation());
                            entity.setPrice(passenger.getTicketPrice());
                            entity.setSex(passenger.getSex());
                            entity.setPassengerNum(passenger.getBookSeats());
                            entity.setAttention(passenger.isAttention());
                            entity.setStartTime(passenger.getStartTime());
                            entity.setPickUp(passenger.isPickUp());
                            datas.add(entity);
                        }
            }
        }

        if (data.getTravel()!=null){
            int size=datas.size();
            for(int i=0;i<data.getTravel().getSeats()-size;i++){
                HeadEntity entity=new HeadEntity();
                entity.setPhone(-i-1);
                datas.add(entity);
            }
        }
        if(data.getSeckillTravelNopay()==null&&datas.size()<4){
            HeadEntity entity=new HeadEntity();
            entity.setPhone(ADD_SEAT);
            datas.add(entity);
        }
        setSelectedPhone(selectedPhone);
        notifyDataSetChanged();
    }
    public long getSelectedPhone() {
        return selectedPhone;
    }

    /**
     * @param selectedPhone 设置选中的电话号码  -1空座,不存在选中第一个
     */
    public void setSelectedPhone(long selectedPhone) {
//        this.selectedPhone = selectedPhone;
        LogUtil.i("set phone = "+this.selectedPhone+"   "+selectedPhone);
        for(int i=0;i<datas.size();i++){
            if(datas.get(i)!=null&&datas.get(i).getPhone()==selectedPhone){
                if(this.selectedPhone!=selectedPhone){
                    this.selectedPhone = selectedPhone;
                    notifyDataSetChanged();
                    if(listener!=null){
                        LogUtil.i("set phone listener= "+this.selectedPhone);
                        listener.onSelectedPhoneChanged(this.selectedPhone);
                    }
                }
                return;
            }
        }
        if(datas.size()>0&&datas.get(0)!=null){
            if(this.selectedPhone!=datas.get(0).getPhone()){
                this.selectedPhone=datas.get(0).getPhone();
                if(listener!=null){
                    LogUtil.i("set phone listener2= "+this.selectedPhone);
                    listener.onSelectedPhoneChanged(this.selectedPhone);
                }
            }
        }
        notifyDataSetChanged();
    }

    public HeadEntity getData(long phone){
        for(int i=0;i<datas.size();i++){
            if(datas.get(i)!=null&&datas.get(i).getPhone()==phone){
                return  datas.get(i);
            }
        }
        return null;
    }

    /**
     * @param phone=-1 表示获取第一个空座的position
     * @return position
     */
    public int getPosition(long phone){
        for(int i=0;i<datas.size();i++){
            if(datas.get(i)!=null&&datas.get(i).getPhone()==phone){
                return i;
            }
        }
        return -1;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public TravelDetailHeadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TravelDetailHeadsViewHolder(inflater.inflate(R.layout.item_travel_detail_heads, parent, false));
    }

    @Override
    public void onBindViewHolder(TravelDetailHeadsViewHolder holder, int position) {
        HeadEntity headEntity=getItem(position);
        if(headEntity!=null){
            HeadEntity leftHeadEntity=null;
            HeadEntity rightHeadEntity=null;
            if(position>0){
                leftHeadEntity=getItem(position-1);
            }

            if(position<getItemCount()-1){
                rightHeadEntity=getItem(position+1);
            }
            headEntity.setSamePassenger(HeadEntity.SAME_NONE);
            if(leftHeadEntity!=null&&leftHeadEntity.getPhone()==headEntity.getPhone()){
                headEntity.setSamePassenger(HeadEntity.SAME_LEFT);
            }
            if(rightHeadEntity!=null&&rightHeadEntity.getPhone()==headEntity.getPhone()){
                headEntity.setSamePassenger(HeadEntity.SAME_RIGHT);
            }
            if(leftHeadEntity!=null&&leftHeadEntity.getPhone()==headEntity.getPhone()&&rightHeadEntity!=null
                    &&rightHeadEntity.getPhone()==headEntity.getPhone()){
                headEntity.setSamePassenger(HeadEntity.SAME_LEFT_RIGHT);
            }

        }

        boolean isSelected=false;
        if(getItem(position)!=null&&getItem(position).getPhone()==selectedPhone){
            isSelected=true;
        }

        holder.bindTypeData(getItem(position),isSelected,position);
    }

    class TravelDetailHeadsViewHolder extends RecyclerView.ViewHolder {
        private HeadEntity headEntity;
        private int position;
        private CircleImageView iv_icon;
        private TextView tv_status;
        private TextView tv_num;
        private View v_lineRhght;
        private View v_lineLeft;


        public TravelDetailHeadsViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.item_travel_detail_icon);
            tv_status = itemView.findViewById(R.id.item_travel_detail_status);
            tv_num = itemView.findViewById(R.id.item_travel_detail_num);
            v_lineRhght = itemView.findViewById(R.id.item_travel_detail_heads_right_line);
            v_lineLeft = itemView.findViewById(R.id.item_travel_detail_heads_left_line);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClick(position,headEntity);
                    }
                }
            });
        }

        private void bindTypeData(HeadEntity headEntity,boolean isSelected,int position) {
            this.position=position;
            this.headEntity=headEntity;
            String num="";
            if(headEntity!=null&&headEntity.getPhone()>0){
                switch (position){
                    case 0:
                        num="乘客一";
                        break;
                    case 1:
                        num="乘客二";
                        break;
                    case 2:
                        num="乘客三";
                        break;
                    case 3:
                        num="乘客四";
                        break;
                }
                if(isSelected){
                    tv_num.setTextColor(0xFF232426);
                    tv_status.setTextColor(0xFF232426);
                    iv_icon.setBorderColor(0xff4186E7);
                }else {
                    tv_num.setTextColor(0xFF939499);
                    iv_icon.setBorderColor(0xFFFFFFFF);
                    tv_status.setTextColor(0xFF939499);
                }
                tv_num.setText(num);

                Picasso.with(context)
                        .load(headEntity.getPicture())
                        .error(R.mipmap.img_me)
                        .into(iv_icon);
                tv_status.setVisibility(View.VISIBLE);
                String statusText=headEntity.getStatusText();
                if(headEntity.isPickUp()){
                    statusText=statusText+" 需接送";
                }
                tv_status.setText(statusText);
                switch (headEntity.getSamePassenger()){
                    case HeadEntity.SAME_NONE:
                        v_lineRhght.setVisibility(View.INVISIBLE);
                        v_lineLeft.setVisibility(View.INVISIBLE);
                        break;
                    case HeadEntity.SAME_RIGHT:
                        v_lineRhght.setVisibility(View.VISIBLE);
                        v_lineLeft.setVisibility(View.INVISIBLE);
                        break;
                    case HeadEntity.SAME_LEFT:
                        v_lineRhght.setVisibility(View.INVISIBLE);
                        v_lineLeft.setVisibility(View.VISIBLE);
                        break;
                    case HeadEntity.SAME_LEFT_RIGHT:
                        v_lineRhght.setVisibility(View.VISIBLE);
                        v_lineLeft.setVisibility(View.VISIBLE);
                        break;
                }
            }else if(headEntity!=null&&headEntity.getPhone()==ADD_SEAT){

                iv_icon.setBorderColor(0xFFFFFFFF);
                Picasso.with(context)
                        .load(R.mipmap.img_add_invite)
                        .into(iv_icon);
                tv_status.setVisibility(View.GONE);
                tv_num.setText("添加座位");
                tv_num.setTextColor(0xFF939499);
                v_lineRhght.setVisibility(View.INVISIBLE);
                v_lineLeft.setVisibility(View.INVISIBLE);
            }else {

                if(isSelected){
                    iv_icon.setBorderColor(0xff4186E7);
                }else {
                    iv_icon.setBorderColor(0xFFFFFFFF);
                }
                Picasso.with(context)
                        .load(R.mipmap.img_invite)
                        .into(iv_icon);
                tv_status.setVisibility(View.GONE);
                tv_num.setText("空座");
                tv_num.setTextColor(0xFF939499);
                v_lineRhght.setVisibility(View.INVISIBLE);
                v_lineLeft.setVisibility(View.INVISIBLE);
            }
        }

    }

    public interface OnItemClickListener{
        void onClick(int position,HeadEntity passenger);
        void onSelectedPhoneChanged(long phone);
    }
}
