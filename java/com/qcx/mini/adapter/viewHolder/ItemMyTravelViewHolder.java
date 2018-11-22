package com.qcx.mini.adapter.viewHolder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.activity.UserInfoActivity;
import com.qcx.mini.adapter.MeTravelsListAdapter;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.TravelsListEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/8/9.
 */

public class ItemMyTravelViewHolder extends BaseRecycleViewHolder<DriverAndTravelEntity> implements View.OnClickListener {

    private OnMeTravelsClickListener listener;
    private DriverAndTravelEntity travel;

    private TextView tv_time, tv_dingzuo, tv_startAddress, tv_endAddress, tv_price;
    private ImageView[] heads;
    private TextView tv_peoples;
    private View v_peoples;
    private TextView tv_good, tv_message, tv_redPackage;
    private ImageView iv_good;
    private View v_good, v_message, v_share;
    private View v_status;
    private View v_oper;
    private View v_top;
    private TextView tv_status;
    private ImageView iv_icon;
    private TextView tv_info;
    private ImageView iv_attention;


    public ItemMyTravelViewHolder(View itemView) {
        super(itemView);

        tv_time = itemView.findViewById(R.id.item_travel_start_time);
        tv_dingzuo = itemView.findViewById(R.id.item_travel_reserve);
        tv_startAddress = itemView.findViewById(R.id.item_travel_startAddress);
        tv_endAddress = itemView.findViewById(R.id.item_travel_endAddress);
        tv_price = itemView.findViewById(R.id.item_travel_price);

        tv_peoples = itemView.findViewById(R.id.item_travel_peoples_text);
        v_peoples = itemView.findViewById(R.id.item_travel_peoples_view);
        tv_good = itemView.findViewById(R.id.item_travel_good_text);
        tv_message = itemView.findViewById(R.id.item_travel_message_text);
        tv_redPackage = itemView.findViewById(R.id.item_travel_red_package);

        iv_good = itemView.findViewById(R.id.item_travel_good_img);
        v_good = itemView.findViewById(R.id.item_travel_good_view);
        v_message = itemView.findViewById(R.id.item_travel_message_view);
        v_share = itemView.findViewById(R.id.item_travel_share_view);

        heads = new ImageView[4];
        heads[0] = itemView.findViewById(R.id.item_travel_headImg1);
        heads[1] = itemView.findViewById(R.id.item_travel_headImg2);
        heads[2] = itemView.findViewById(R.id.item_travel_headImg3);
        heads[3] = itemView.findViewById(R.id.item_travel_headImg4);

        v_status = itemView.findViewById(R.id.item_my_travel_status_view);
        tv_status = itemView.findViewById(R.id.item_my_travel_status_text);
        v_oper = itemView.findViewById(R.id.item_my_travel_operate_view);
        v_top = itemView.findViewById(R.id.item_my_travel_top_view);

        iv_icon = itemView.findViewById(R.id.item_travel_top_icon);
        tv_info = itemView.findViewById(R.id.item_travel_top_info);
        iv_attention = itemView.findViewById(R.id.item_travel_top_attention);

        setClickListener();
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener= (OnMeTravelsClickListener) holederListener;
    }

    void setClickListener() {
        if (v_good != null) {
            v_good.setOnClickListener(this);
        }
        if (v_message != null) {
            v_message.setOnClickListener(this);
        }
        if (v_share != null) {
            v_share.setOnClickListener(this);
        }
        if (tv_dingzuo != null) {
            tv_dingzuo.setOnClickListener(this);
        }
        if (iv_icon != null) {
            iv_icon.setOnClickListener(this);
        }
        if (iv_attention != null) {
            iv_attention.setOnClickListener(this);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onTravelClick(travel);
                }
            }
        });
    }

    @Override
    public void bindData(DriverAndTravelEntity data, @NonNull Params params) {
        if (data == null || data.getTravelVo() == null) {
            return;
        }
        travel = data;
        setTravelInfo();
        setData();
    }

    private void setTravelInfo() {
        tv_time.setText(DateUtil.getTimeStr(travel.getTravelVo().getStartTime(), "MM月dd日HH:mm"));
        tv_startAddress.setText(travel.getTravelVo().getStartAddress());
        tv_endAddress.setText(travel.getTravelVo().getEndAddress());
        tv_price.setText(CommonUtil.formatPrice(travel.getTravelVo().getTravelPrice(), 2));

        int type = travel.getTravelVo().getType();
        setTravelTypeUI(type);
    }

    private void setTravelTypeUI(int type) {
        if (type == 3 || type == 1) {
            setHeadImg(travel.getTravelVo().getSeats(), travel.getTravelVo().getHeadPictures());
            v_peoples.setVisibility(View.GONE);
            tv_peoples.setVisibility(View.GONE);
        } else {
            setHeadImg(0, null);
            v_peoples.setVisibility(View.VISIBLE);
            tv_peoples.setVisibility(View.VISIBLE);
            tv_peoples.setText(String.valueOf(travel.getTravelVo().getSeats() - travel.getTravelVo().getSurplusSeats()));
        }
        switch (type) {
            case 0://车主行程
                v_status.setVisibility(View.VISIBLE);
                v_oper.setVisibility(View.VISIBLE);
                break;
            case 1://乘客创建的行程
                v_status.setVisibility(View.VISIBLE);
                v_oper.setVisibility(View.VISIBLE);
                break;
            case 2://乘客订的车主的行程
                v_status.setVisibility(View.VISIBLE);
                v_oper.setVisibility(View.GONE);
                break;
            case 3://车主抢的乘客的行程
                v_status.setVisibility(View.VISIBLE);
                v_oper.setVisibility(View.GONE);
                break;
        }
    }


    private void setData() {
        tv_dingzuo.setVisibility(View.GONE);
        v_top.setVisibility(View.GONE);
        tv_status.setText(CommonUtil.getTravelStatusText(travel.getTravelVo().getStatus(),travel.getTravelVo().getStartTime()));
        if (travel.getTravelVo().isLiked()) {
            iv_good.setImageResource(R.mipmap.btn_goodyes_mini);
        } else {
            iv_good.setImageResource(R.mipmap.btn_good_mini);
        }

        tv_good.setText(String.valueOf(travel.getTravelVo().getLikesNum()));
        tv_message.setText(String.valueOf(travel.getTravelVo().getCommentsNum()));

        double redPackage = travel.getTravelVo().getRedPacketPrice();
        if (redPackage > 0) {
            tv_redPackage.setVisibility(View.VISIBLE);
            tv_redPackage.setText(String.format("赚%s元", CommonUtil.formatPrice(redPackage, 1)));
        } else {
            tv_redPackage.setVisibility(View.GONE);
            tv_redPackage.setText("0.0");
        }
        if (travel.getPhone() >0) {
            Picasso.with(holderContext)
                    .load(travel.getPicture())
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .error(R.mipmap.img_me)
                    .placeholder(R.mipmap.img_me)
                    .into(iv_icon);

            StringBuilder builder = new StringBuilder();
            String name = travel.getNickName();
            String car = null;
            String carNum = null;
            if (travel.getTravelVo().getType() == 1 || travel.getTravelVo().getType() == 2) {
                carNum = travel.getCarNumber();
                car = travel.getCar();
            }
            if (!TextUtils.isEmpty(name)) {
                builder.append(name);
                builder.append("·");
            }
            switch (travel.getSex()) {
                case ConstantValue.SexType.MAN:
                    builder.append("男");
                    builder.append("·");
                    break;
                case ConstantValue.SexType.WOMAN:
                    builder.append("女");
                    builder.append("·");
                    break;
            }
            if (!TextUtils.isEmpty(carNum)) {
                builder.append(carNum);
                builder.append("·");
            }
            if (!TextUtils.isEmpty(car)) {
                builder.append(car);
                builder.append("·");
            }
            if (!TextUtils.isEmpty(builder) && builder.length() > 0) {
                builder.delete(builder.length() - 1, builder.length());
            }
            tv_info.setText(builder);
            if (travel.isAttention()) {
                iv_attention.setImageResource(R.mipmap.btn_followed_mini);
            } else {
                iv_attention.setImageResource(R.mipmap.btn_follow_mini);
            }
        }
    }


    private void setHeadImg(int seats, List<String> picURLs) {
        for (int i = 0; i < heads.length; i++) {
            if (i < seats) {
                heads[i].setVisibility(View.VISIBLE);
                if (picURLs != null && i < picURLs.size()) {
                    setHeadImg(heads[i], picURLs.get(i));
                } else {
                    heads[i].setImageResource(R.mipmap.img_haveseat);
                }
            } else {
                heads[i].setVisibility(View.GONE);
            }
        }
    }

    private void setHeadImg(ImageView iv, String picURL) {
        try {
            Picasso.with(holderContext)
                    .load(picURL)
                    .error(R.mipmap.img_me)
                    .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_travel_good_text:
            case R.id.item_travel_good_view:
                if (listener != null) {
                    listener.onLikesClick(travel, iv_good, tv_good);
                }
                break;
            case R.id.item_travel_message_text:
            case R.id.item_travel_message_view:
                if (listener != null) {
                    listener.onMessageClick(travel, tv_message);
                }
                break;
            case R.id.item_travel_share_text:
            case R.id.item_travel_share_view:
                if (listener != null) {
                    listener.onShareClick(travel);
                }
                break;
            case R.id.item_travel_reserve:
                if (listener != null) {
                    listener.onTravelOperClick(travel);
                }
                break;
            case R.id.item_travel_top_icon:
                if (travel != null && travel.getPhone() > 0) {
                    Intent intent = new Intent(holderContext, UserInfoActivity.class);
                    intent.putExtra("phone",travel.getPhone());
                    holderContext.startActivity(intent);
                }
                break;
            case R.id.item_travel_top_attention:
                if (listener != null) {
                    listener.onAttentionClick(travel, iv_attention);
                }
                break;
        }
    }

    public interface OnMeTravelsClickListener extends ItemClickListener {
        void onLikesClick(DriverAndTravelEntity data, ImageView likeViw, TextView likeNum);

        void onMessageClick(DriverAndTravelEntity data, TextView messageNum);

        void onShareClick(DriverAndTravelEntity data);

        void onTravelOperClick(DriverAndTravelEntity data);

        void onTravelClick(DriverAndTravelEntity data);

        void onAttentionClick(DriverAndTravelEntity data, ImageView iv_attention);
    }
}
