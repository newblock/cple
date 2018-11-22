package com.qcx.mini.adapter.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemDriverAndTravelClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;
import java.util.Locale;

/**
 * Created by Administrator on 2018/4/9.
 */

public class ItemDriverAndTravelViewHolder extends ItemTravelViewHolder{
    public static Object tag = new Object();
    private ItemDriverAndTravelClickListener listener;

    private DriverAndTravelEntity entity;
    private ImageView iv_icon;
    public TextView tv_name;

    private ImageView iv_attention;
    private TextView tv_travelReserve;
    private View v_travelGood;
    private ImageView iv_travelGood;
    private TextView tv_travelGood;
    private View v_travelMessage;
    private TextView tv_travelMessage;
    private View v_travelShare;
    private TextView tv_redpackageNum;
    private View v_bottomView;

    public ItemDriverAndTravelViewHolder(View itemView) {
        super(itemView);
        iv_icon = itemView.findViewById(R.id.item_driver_travel_icon);
        tv_name = itemView.findViewById(R.id.item_driver_travel_name);
        iv_attention = itemView.findViewById(R.id.item_driver_travel_attention);
        tv_travelReserve = itemView.findViewById(R.id.item_travel_reserve);
        tv_travelPriceType = itemView.findViewById(R.id.item_travel_price_type);
        v_travelGood = itemView.findViewById(R.id.item_travel_good_view);
        iv_travelGood = itemView.findViewById(R.id.item_travel_good_img);
        tv_travelGood = itemView.findViewById(R.id.item_travel_good_text);
        v_travelMessage = itemView.findViewById(R.id.item_travel_message_view);
        tv_travelMessage = itemView.findViewById(R.id.item_travel_message_text);
        v_travelShare = itemView.findViewById(R.id.item_travel_share_view);
        tv_redpackageNum = itemView.findViewById(R.id.item_travel_red_package);
        v_bottomView = itemView.findViewById(R.id.item_travel_bottomView);
        setClicklistener();
    }

    public void hideBottomView() {
        v_bottomView.setVisibility(View.GONE);
    }

    @Override
    public void setHolederListener(ItemClickListener listener) {
        this.listener = (ItemDriverAndTravelClickListener) listener;
    }

    protected void setClicklistener(){
        super.setClicklistener();

        iv_icon.setOnClickListener(this);
        iv_attention.setOnClickListener(this);
        v_travelGood.setOnClickListener(this);
        v_travelMessage.setOnClickListener(this);
        v_travelShare.setOnClickListener(this);
        tv_travelReserve.setOnClickListener(this);
    }

    @Override
    public void bindData(Object data,Params params) {
        try {
            this.entity = (DriverAndTravelEntity) data;
            bindData();
            LogUtil.i("binddata++++++++++++++++++++");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("binddata-------------");
        }
    }

    void bindData() {
        if (entity == null) return;
        Picasso.with(holderContext)
                .load(entity.getPicture())
                .error(R.mipmap.img_me)
                .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                .tag(tag)
                .into(iv_icon);

        StringBuilder builder=new StringBuilder();
        String name=entity.getNickName();
        String carNum="";
        if(entity.getTravelVo().getType()==ConstantValue.TravelType.DRIVER){
            carNum=entity.getCarNumber();
        }
        if(!TextUtils.isEmpty(name)){
            builder.append(name);
            builder.append("·");
        }
        switch (entity.getSex()){
            case ConstantValue.SexType.MAN:
                builder.append("男");
                builder.append("·");
                break;
            case ConstantValue.SexType.WOMAN:
                builder.append("女");
                builder.append("·");
                break;
        }
        if(!TextUtils.isEmpty(carNum)){
            builder.append(carNum);
            builder.append("·");
        }
        if(!TextUtils.isEmpty(builder)&&builder.length()>0){
            builder.delete(builder.length()-1,builder.length());
        }
        tv_name.setText(builder);


        if (entity.isAttention()) {
            iv_attention.setImageResource(R.mipmap.btn_followed_mini);
        } else {
            iv_attention.setImageResource(R.mipmap.btn_follow_mini);
        }
        if (entity.isHideAttentionView()) {
            iv_attention.setVisibility(View.GONE);
        } else {
            iv_attention.setVisibility(View.VISIBLE);
        }
        TravelEntity travelEntity = entity.getTravelVo();
        if (travelEntity != null) {
            travelEntity.setByWay(entity.getByWay());
            travelEntity.setStartRecommend(entity.getStartRecommend());
            travelEntity.setEndRecommend(entity.getEndRecommend());
            setTravelInfo(travelEntity);
            if (travelEntity.getType() == 0) {//车主行程
                String text="坐TA的车";
                tv_travelReserve.setText(text);
            } else {
                tv_travelReserve.setText("抢单");
            }

            if (travelEntity.isLiked()) {
                iv_travelGood.setImageResource(R.mipmap.btn_goodyes_mini);
            } else {
                iv_travelGood.setImageResource(R.mipmap.btn_good_mini);
            }

            tv_travelGood.setText(String.valueOf(travelEntity.getLikesNum()));
            tv_travelMessage.setText(String.valueOf(travelEntity.getCommentsNum()));
            if (travelEntity.getRedPacketPrice() > 0) {
                tv_redpackageNum.setText(String.format(Locale.CHINA, "赚%s元", CommonUtil.formatPrice(travelEntity.getRedPacketPrice(),1)));
            } else {
                tv_redpackageNum.setText("分享");
            }
        }
        if (entity.isHideDingzuo()) {
            tv_travelReserve.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if(entity==null){
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.item_driver_travel_icon:
                if (listener != null) {
                    listener.onIconClick(entity.getPhone());
                }
                break;
            case R.id.item_driver_travel_attention:
                if (listener != null) {
                    listener.onAttentionClick(entity, iv_attention);
                }
                break;
            case R.id.item_travel_reserve:
            case R.id.item_travel_view:
                if (listener != null) {
                    listener.onTravelClick(entity.getTravelVo());
                }
                break;
            case R.id.item_travel_good_view:
                if (listener != null) {
                    listener.onLikesClick(entity, iv_travelGood, tv_travelGood);
                }
                break;
            case R.id.item_travel_message_view:
                if (listener != null) {
                    listener.onMessageClick(entity, tv_travelMessage);
                }
                break;
            case R.id.item_travel_share_view:
                if (listener != null) {
                    listener.onShareClick(entity);
                }
                break;
        }
    }
}