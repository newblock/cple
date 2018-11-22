package com.qcx.mini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.activity.TravelHistoryActivity;
import com.qcx.mini.activity.UserInfoActivity;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.TravelsListEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/12/18.
 */

public class MeTravelsListAdapter extends BaseRecyclerViewAdapter<TravelsListEntity.TravelEntity, MeTravelsListAdapter.TravelsListViewHolder> {
    public final static int TYPE_UNFINISH = 1;
    public final static int TYPE_HISTORY = 2;
    private boolean haveBottomView = false;
    private int travelType;

    private Context context;
    private OnMeTravelsClicklistener listener;
    private int type = TYPE_UNFINISH;
    private List<TravelsListViewHolder> holders;
    TravelsListEntity.TravelEntity travelEntity;
    private boolean isOrther=false;

    public OnMeTravelsClicklistener getListener() {
        return listener;
    }

    public void setListener(OnMeTravelsClicklistener listener) {
        this.listener = listener;
    }

    public MeTravelsListAdapter(Context context) {
        super(context);
        this.context = context;
        holders = new ArrayList<>();
    }

    public void dateChanged() {
        for (int i = 0; i < holders.size(); i++) {
            if(!isOrther){
                holders.get(i).countDownTime();
            }
        }
    }

    public void setOrther(boolean orther) {
        isOrther = orther;
    }

    public void setTravelType(int travelType) {
        this.travelType = travelType;
    }

    @Override
    public int getItemCount() {
        if (haveBottomView) {
            return super.getItemCount() + 1;
        } else {
            return super.getItemCount();
        }
    }

    public void setBottomeView(TravelsListEntity.TravelEntity travelEntity) {
        this.haveBottomView = travelEntity != null;
        this.travelEntity = travelEntity;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size()) {
            return 2;
        } else {
            return 1;
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public TravelsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new TravelsListViewHolder(inflater.inflate(R.layout.item_my_travel, parent, false));
        } else {//底部VIew
            return new BottomViewHolder(inflater.inflate(R.layout.item_my_travel_bottom, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(TravelsListViewHolder holder, int position) {
        if (position < datas.size()) {
            holder.bindData(datas.get(position), type);
        } else {
            holder.bindData(travelEntity, type);
        }
        if (!holders.contains(holder)) {
            holders.add(holder);
        }
    }

    class TravelsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TravelsListEntity.TravelEntity entity;

        TextView tv_time, tv_dingzuo, tv_startAddress, tv_endAddress, tv_price;
        ImageView[] heads;
        TextView tv_peoples;
        View v_peoples;
        TextView tv_good, tv_message, tv_redPackage;
        ImageView iv_good;
        View v_good, v_message, v_share;
        View v_status;
        View v_oper;
        View v_top;
        TextView tv_status;
        ImageView iv_icon;
        TextView tv_info;
        ImageView iv_attention;

        public TravelsListViewHolder(View view) {
            super(view);
            tv_time = view.findViewById(R.id.item_travel_start_time);
            tv_dingzuo = view.findViewById(R.id.item_travel_reserve);
            tv_startAddress = view.findViewById(R.id.item_travel_startAddress);
            tv_endAddress = view.findViewById(R.id.item_travel_endAddress);
            tv_price = view.findViewById(R.id.item_travel_price);

            tv_peoples = view.findViewById(R.id.item_travel_peoples_text);
            v_peoples = view.findViewById(R.id.item_travel_peoples_view);
            tv_good = view.findViewById(R.id.item_travel_good_text);
            tv_message = view.findViewById(R.id.item_travel_message_text);
            tv_redPackage = view.findViewById(R.id.item_travel_red_package);

            iv_good = view.findViewById(R.id.item_travel_good_img);
            v_good = view.findViewById(R.id.item_travel_good_view);
            v_message = view.findViewById(R.id.item_travel_message_view);
            v_share = view.findViewById(R.id.item_travel_share_view);

            heads = new ImageView[4];
            heads[0] = view.findViewById(R.id.item_travel_headImg1);
            heads[1] = view.findViewById(R.id.item_travel_headImg2);
            heads[2] = view.findViewById(R.id.item_travel_headImg3);
            heads[3] = view.findViewById(R.id.item_travel_headImg4);

            v_status = view.findViewById(R.id.item_my_travel_status_view);
            tv_status = view.findViewById(R.id.item_my_travel_status_text);
            v_oper = view.findViewById(R.id.item_my_travel_operate_view);
            v_top = view.findViewById(R.id.item_my_travel_top_view);

            iv_icon = view.findViewById(R.id.item_travel_top_icon);
            tv_info = view.findViewById(R.id.item_travel_top_info);
            iv_attention = view.findViewById(R.id.item_travel_top_attention);

            setClickListener();

        }

        void setClickListener(){
            if(v_good!=null){
                v_good.setOnClickListener(this);
            }
            if(v_message!=null){
                v_message.setOnClickListener(this);
            }
            if(v_share!=null){
                v_share.setOnClickListener(this);
            }
            if(tv_dingzuo!=null){
                tv_dingzuo.setOnClickListener(this);
            }
            if(iv_icon!=null){
                iv_icon.setOnClickListener(this);
            }
            if(iv_attention!=null){
                iv_attention.setOnClickListener(this);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        listener.onTravelClick(entity);
                    }
                }
            });
        }

        void bindData(TravelsListEntity.TravelEntity e, int type) {
            this.entity = e;
            setData(type);
        }

        void setTravelInfo(int type){
            tv_time.setText(DateUtil.getTimeStr(entity.getStartTime(), "MM月dd日HH:mm"));
            tv_startAddress.setText(entity.getStartAddress());
            tv_endAddress.setText(entity.getEndAddress());
            tv_price.setText(CommonUtil.formatPrice(entity.getTravelPrice(),2));
            setStatus(entity.getType()==0);
        }

        private void setData(int type) {
            setTravelInfo(type);

            if(isOrther){
                tv_dingzuo.setVisibility(View.VISIBLE);
                v_status.setVisibility(View.GONE);
                v_top.setVisibility(View.GONE);
//                v_oper.setVisibility(View.VISIBLE);
                v_oper.setVisibility(View.GONE);
            }else {
                tv_dingzuo.setVisibility(View.GONE);

                switch (entity.getType()) {
                    case 0://车主行程
                        v_status.setVisibility(View.VISIBLE);
//                        v_oper.setVisibility(View.VISIBLE);
                        v_oper.setVisibility(View.GONE);
                        break;
                    case 1://乘客创建的行程
                        v_status.setVisibility(View.VISIBLE);
//                        v_oper.setVisibility(View.VISIBLE);
                        v_oper.setVisibility(View.GONE);
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


            v_top.setVisibility(View.GONE);
            countDownTime();
            if (entity.isLiked()) {
                iv_good.setImageResource(R.mipmap.btn_goodyes_mini);
            } else {
                iv_good.setImageResource(R.mipmap.btn_good_mini);
            }
            tv_good.setText(String.valueOf(entity.getLikesNum()));
            tv_message.setText(String.valueOf(entity.getCommentsNum()));

            double redPackage = entity.getRedPacketPrice();
            if (redPackage > 0) {
                tv_redPackage.setVisibility(View.VISIBLE);
                tv_redPackage.setText(String.format("赚%s元", CommonUtil.formatPrice(redPackage,1)));
            } else {
                tv_redPackage.setVisibility(View.GONE);
                tv_redPackage.setText("0.0");
            }
            if (type == TYPE_HISTORY) {
                v_top.setVisibility(View.GONE);
                v_status.setVisibility(View.GONE);
                v_oper.setVisibility(View.GONE);
            }

            if (entity.getDriverInfo() != null) {
                TravelsListEntity.UserInfo driver = entity.getDriverInfo();
                Picasso.with(context)
                        .load(driver.getPicture())
                        .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                        .error(R.mipmap.img_me)
                        .placeholder(R.mipmap.img_me)
                        .into(iv_icon);

                StringBuilder builder = new StringBuilder();
                String name = driver.getNickName();
                String car=null;
                String carNum=null;
                if (entity.getType() == 1 || entity.getType() == 2) {
                    carNum = driver.getCarNumber();
                    car = driver.getCar();
                }
                if (!TextUtils.isEmpty(name)) {
                    builder.append(name);
                    builder.append("·");
                }
                switch (driver.getSex()) {
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
                if(entity.isAttention()){
                    iv_attention.setImageResource(R.mipmap.btn_followed_mini);
                }else {
                    iv_attention.setImageResource(R.mipmap.btn_follow_mini);
                }
            }

        }

        void countDownTime() {
            String info = "";
            if (entity == null) {
                return;
            }
            long startTime = entity.getStartTime();
            long cTime = System.currentTimeMillis();

            long time;
            String timeStr;
            switch (entity.getStatus()) {
                case 0://正在寻找乘客
                    info = "等待乘客订座";
                    break;
                case 1://车主有乘客未发车
                    if (entity.getStartTime() < System.currentTimeMillis() + 15 * DateUtil.MINUTE) {
                        long c = startTime + 15 * DateUtil.MINUTE - cTime;
                        if (c > 0) {
                            int m = (int) (c / DateUtil.MINUTE);
                            int s = (int) (c % DateUtil.MINUTE) / (1000);
                            info = String.format(Locale.CHINA, "发车倒计时%d:%d", m, s);
                        } else {
                            info = "即将自动发车";
                        }
                    } else {
                        info = String.format("%s %s发车哦", DateUtil.formatDay("MM-dd", entity.getStartTime()), DateUtil.getTimeStr(entity.getStartTime(), "HH:mm"));

                    }
                    break;
                case 2://车主发布的行程已发车
                    time = 5 * DateUtil.HOUR - (cTime - startTime);
                    if (time > 0) {
                        if(time>DateUtil.DAY){
                            timeStr = DateUtil.getTime(time, true, true, true, false);
                        }else if (time > DateUtil.HOUR) {
                            timeStr = DateUtil.getTime(time, false, true, true, false);
                        } else {
                            timeStr = DateUtil.getTime(time, false, false, true, true);
                        }
                        info = "已发车，" + timeStr + "后行程将自动完成哦";
                    } else {
                        info = "即将自动完成";
                    }
                    break;
                case 9://车主抢单，乘客未支付
                    info = "抢单成功,乘客未支付";
                    v_top.setVisibility(View.VISIBLE);
                    break;
                case 11://车主抢单，乘客已支付
                    if (entity.getStartTime() < System.currentTimeMillis() + 15 * DateUtil.MINUTE) {
                        long c = startTime + 15 * DateUtil.MINUTE - cTime;
                        if (c > 0) {
                            int m = (int) (c / DateUtil.MINUTE);
                            int s = (int) (c % DateUtil.MINUTE) / (1000);
                            info = String.format(Locale.CHINA, "发车倒计时%d:%d", m, s);
                        } else {
                            info = "即将自动发车";
                        }
                    } else {
                        info = String.format("乘客已支付，%s %s发车哦", DateUtil.formatDay("MM-dd", entity.getStartTime()), DateUtil.getTimeStr(entity.getStartTime(), "HH:mm"));
                    }
                    v_top.setVisibility(View.VISIBLE);
                    break;
                case 12://车主抢单，已发车，行车中
                    time = 5 * DateUtil.HOUR - (cTime - startTime);
                    if (time > 0) {
                        if(time>DateUtil.DAY){
                            timeStr = DateUtil.getTime(time, true, true, true, false);
                        }else if (time > DateUtil.HOUR) {
                            timeStr = DateUtil.getTime(time, false, true, true, false);
                        } else {
                            timeStr = DateUtil.getTime(time, false, false, true, true);
                        }
                        info = "已发车，" + timeStr + "后行程将自动完成哦";
                    } else {
                        info = "即将自动完成";
                    }
                    v_top.setVisibility(View.VISIBLE);
                    break;

                case 3://乘客发布的行程，等待接单
                    info = "等待车主接单";
                    break;
                case 4://乘客发布的行程，车主已抢单，等待支付
                    info = "车主已接单，去支付吧";
                    v_top.setVisibility(View.VISIBLE);
                    break;

                case 5://乘客订的车主的行程，未支付
                    info = "座位已锁定，去支付";
                    v_top.setVisibility(View.VISIBLE);
                    break;
                case 6://乘客订的车主的行程，已支付
                    if (cTime > startTime) {
                        time = 30 * DateUtil.MINUTE - (cTime - startTime);
                        if (time > 0) {
                            info = DateUtil.getTime(time, false, false, true, true) + "后自动上车";
                        } else {
                            info = "即将自动上车";
                        }
                    } else {
                        info = String.format("已支付，%s %s记得准时上车", DateUtil.formatDay("MM-dd", entity.getStartTime()), DateUtil.getTimeStr(entity.getStartTime(), "HH:mm"));
                    }
                    v_top.setVisibility(View.VISIBLE);
                    break;
                case 7://乘客订的车主的行程，已发车
                    if (cTime > startTime) {
                        time = 30 * DateUtil.MINUTE - (cTime - startTime);
                        if (time > 0) {
                            info = DateUtil.getTime(time, false, false, true, true) + "后自动上车";
                        } else {
                            info = "即将自动上车";
                        }
                    } else {

                        info = "车主发车啦，22:30上车哦";
                    }
                    v_top.setVisibility(View.VISIBLE);
                    break;
                case 8://乘客订的车主的行程，行程中
                    info = "行程中,请系好安全带";
                    v_top.setVisibility(View.VISIBLE);
                    break;
            }
            tv_status.setText(info);

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
                Picasso.with(context)
                        .load(picURL)
                        .error(R.mipmap.img_me)
                        .into(iv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setStatus(boolean isDriver) {
            if (isDriver) {
                setHeadImg(entity.getSeats(), entity.getHeadPictures());
                v_peoples.setVisibility(View.GONE);
                tv_peoples.setVisibility(View.GONE);
            } else {
                setHeadImg(0, null);
                v_peoples.setVisibility(View.VISIBLE);
                tv_peoples.setVisibility(View.VISIBLE);
                tv_peoples.setText(String.valueOf(entity.getSeats() - entity.getSurplusSeats()));
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_travel_good_text:
                case R.id.item_travel_good_view:
                    if (listener != null) {
                        listener.onLikesClick(entity, iv_good, tv_good);
                    }
                    break;
                case R.id.item_travel_message_text:
                case R.id.item_travel_message_view:
                    if (listener != null) {
                        listener.onMessageClick(entity, tv_message);
                    }
                    break;
                case R.id.item_travel_share_text:
                case R.id.item_travel_share_view:
                    if (listener != null) {
                        listener.onShareClick(entity);
                    }
                    break;
                case R.id.item_travel_reserve:
                    if (listener != null) {
                        listener.onTravelOperClick(entity);
                    }
                    break;
                case R.id.item_travel_top_icon:
                    if(entity!=null&&entity.getDriverInfo()!=null&&entity.getDriverInfo().getPhone()>0){
                        Intent intent=new Intent(context, UserInfoActivity.class);
                        intent.putExtra("phone",entity.getDriverInfo().getPhone());
                        context.startActivity(intent);
                    }
                    break;
                case R.id.item_travel_top_attention:
                    if(listener!=null){
                        listener.onAttentionClick(entity,iv_attention);
                    }
                    break;
            }
        }
    }


    class BottomViewHolder extends TravelsListViewHolder {
        private View v_h;
        private View v_travel;
        private View v_noData;

        public BottomViewHolder(View view) {
            super(view);
            v_h = view.findViewById(R.id.xxxxxxxxxxxxxx_history);
            v_travel=view.findViewById(R.id.item_my_travel_bottom_travel);
            v_noData=view.findViewById(R.id.item_my_travel_bottom_no_data);
            v_h.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int type;
                    if(entity.getType()==0||entity.getType()==3){
                        type=ConstantValue.TravelType.DRIVER;
                    }else {
                        type=ConstantValue.TravelType.PASSENGER;
                    }
                    Intent intent=new Intent(context, TravelHistoryActivity.class);
                    intent.putExtra("travelType",travelType);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        void setClickListener() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        @Override
        void bindData(TravelsListEntity.TravelEntity e, int type) {
            entity=e;
            if(entity==null||entity.getTravelId()==0){
                v_travel.setVisibility(View.GONE);
                v_noData.setVisibility(View.VISIBLE);
            }else {
                v_travel.setVisibility(View.VISIBLE);
                v_noData.setVisibility(View.GONE);
                setTravelInfo(type);
            }
        }

        @Override
        void countDownTime() {
        }
    }

    public interface OnMeTravelsClicklistener extends ItemClickListener{
        void onLikesClick(TravelsListEntity.TravelEntity data, ImageView likeViw, TextView likeNum);

        void onMessageClick(TravelsListEntity.TravelEntity data, TextView messageNum);

        void onShareClick(TravelsListEntity.TravelEntity data);

        void onTravelOperClick(TravelsListEntity.TravelEntity data);

        void onTravelClick(TravelsListEntity.TravelEntity data);

        void onAttentionClick(TravelsListEntity.TravelEntity data, ImageView iv_attention);
    }
}
