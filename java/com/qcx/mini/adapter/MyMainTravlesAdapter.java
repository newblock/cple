package com.qcx.mini.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.MainUnfinishTravelsEntity;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/12/25.
 */

public class MyMainTravlesAdapter extends BaseRecyclerViewAdapter<MainUnfinishTravelsEntity.UnFinishTravelEntity, MyMainTravlesAdapter.MyMainTravlesViewHolder> {
    private Activity context;
    private List<MyMainTravlesViewHolder> holders;
    private OnTravelClickListener listener;

    public void setListener(OnTravelClickListener listener) {
        this.listener = listener;
    }

    public MyMainTravlesAdapter(Activity context) {
        super(context);
        this.context = context;
        holders = new ArrayList<>();
    }

    public MyMainTravlesAdapter(Activity context, List<MainUnfinishTravelsEntity.UnFinishTravelEntity> datas) {
        super(context, datas);
        this.context = context;
        holders = new ArrayList<>();
    }

    public void dateChanged() {
        for (int i = 0; i < holders.size(); i++) {
            holders.get(i).setCountDownTime();
        }
    }

    @Override
    public MyMainTravlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyMainTravlesViewHolder(inflater.inflate(R.layout.item_main_my_travels, parent, false));
    }

    @Override
    public void onBindViewHolder(MyMainTravlesViewHolder holder, int position) {
        holder.bindData(getItem(position));
        if (!holders.contains(holder)) {
            holders.add(holder);
        }

        LogUtil.i("position=" + position + " travelType=" + getItem(position).getType() + " status=" + getItem(position).getStatus());
    }

    class MyMainTravlesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel;
        View ball;
        TextView tv_type, tv_typeName, tv_time, tv_endAddress, tv_centerText;
        TextView tv_typeStatus;
        int countDownTimeType;//0 不显示倒计时,1显示乘客上车倒计时，2，显示车主发车倒计时

        public MyMainTravlesViewHolder(View itemView) {
            super(itemView);
            ball = itemView.findViewById(R.id.item_main_travels_ball);
            tv_type = itemView.findViewById(R.id.item_main_travels_type);
            tv_typeName = itemView.findViewById(R.id.item_main_travels_type_name);
            tv_typeStatus = itemView.findViewById(R.id.item_main_travels_type_status);
            tv_time = itemView.findViewById(R.id.item_main_travels_time);
            tv_endAddress = itemView.findViewById(R.id.item_main_travels_endAddress);
            tv_centerText = itemView.findViewById(R.id.item_main_travels_center_text);
            ball.setOnClickListener(this);
        }

        void bindData(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel) {
            this.unFinishTravel = unFinishTravel;
            countDownTimeType = 0;
            changeData();
        }

        private void changeData() {
            int travelType = unFinishTravel.getType();
            if (travelType == 0 || travelType == 3) {//车主行程
                ball.setBackground(context.getResources().getDrawable(R.mipmap.icon_home_car));
                switch (unFinishTravel.getStatus()) {
                    case 0://正在寻找乘客
                        setBallInfo("正在寻找", "乘客", unFinishTravel.getSeats()+"座余"+unFinishTravel.getSurplusSeats(), "");
                        break;
                    case 1://有乘客下单
                        setBallInfo("有顺道", "乘客",unFinishTravel.getSeats()+"座余"+unFinishTravel.getSurplusSeats(), "");
                        countDownTimeType = 2;
                        setCountDownTime();
//                        }
                        break;
                    case 2://已发车，行程中
                        setBallInfo("", "", "", "行程中");
                        break;
                    case 9://车主抢单，乘客未支付
                        setBallInfo("抢单成功", "", "乘客未支付", "");
                        break;
                    case 11://车主抢单，乘客已支付
                        setBallInfo("抢单成功", "", "已支付", "");
                        countDownTimeType = 2;
                        setCountDownTime();
//                        }
                        break;
                    case 12://车主抢单，已发车，行车中
                        setBallInfo("", "", "", "行程中");
                        break;
                }

            } else {//乘客行程
                ball.setBackground(context.getResources().getDrawable(R.mipmap.icon_home_man));
                switch (unFinishTravel.getStatus()) {
                    case 3://乘客发布的行程，等待接待
                        setBallInfo("正在通知", "车主", "待接单", "");
                        break;
                    case 4://乘客发布的行程，车主已抢单，等待支付
                        setBallInfo("车主接单", "去支付吧", "", "");
                        break;
                    case 5://乘客订的车主的行程，未支付
                        setBallInfo("行程还未", "支付", "", "");
                        break;
                    case 6://乘客订的车主的行程，已支付
                    case 7://乘客订的车主的行程，已发车
                        if (unFinishTravel.getStatus() == 6) setBallInfo("记得准时", "上车哦", "", "");
                        if (unFinishTravel.getStatus() == 7) setBallInfo("请点击确", "认上车", "", "");

                        countDownTimeType = 1;
                        setCountDownTime();
                        break;
                    case 8://乘客订的车主的行程，行程中
                        setBallInfo("", "", "", "行程中");
                        break;
                }
            }

            tv_time.setText(unFinishTravel.getStartTimeTxt());
            tv_endAddress.setText(unFinishTravel.getEndAddress());
        }

        private void setBallInfo(String text1, String text2, String text3, String centerText) {
            if (TextUtils.isEmpty(centerText)) {
                tv_centerText.setVisibility(View.GONE);
                tv_type.setVisibility(View.VISIBLE);
                tv_typeName.setVisibility(View.VISIBLE);
                tv_typeStatus.setVisibility(View.VISIBLE);

            } else {
                tv_centerText.setVisibility(View.VISIBLE);
                tv_type.setVisibility(View.GONE);
                tv_typeName.setVisibility(View.GONE);
                tv_typeStatus.setVisibility(View.GONE);
            }
            tv_type.setText(text1);
            tv_typeName.setText(text2);
            tv_typeStatus.setText(text3);
            tv_centerText.setText(centerText);
        }

        private void setCountDownTime() {
         if (countDownTimeType == 1) {
                long d = System.currentTimeMillis() - unFinishTravel.getStartTime();
                if (d > 0) {//推荐上车时间之后
                    if (d < 30 * 60 * 1000) {
                        d = 30 * 60 * 1000 - d;
                        setBallInfo("确认上车", "倒计时", DateUtil.getTimeStr(d, "mm:ss"), "");
                    } else {
                        setBallInfo("确认上车", "倒计时", "00:00", "");
                    }
                }
            } else if (countDownTimeType == 2) {

                long d = System.currentTimeMillis()+ 15 * 60 * 1000 - unFinishTravel.getStartTime();
                if (d > 0) {
                    if (d < 30 * 60 * 1000) {
                        d = 30 * 60 * 1000 - d;
                        setBallInfo("发车倒计", "时", DateUtil.getTimeStr(d, "mm:ss"), "");
                    } else {
                        setBallInfo("发车倒计", "时", "00:00", "");
                    }
                }

            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.item_main_travels_ball) {
                if(unFinishTravel==null){
                    return;
                }
                switch (unFinishTravel.getStatus()) {
                    case 0://正在寻找乘客
                        if (listener != null) {
                            listener.onNoDriver(unFinishTravel);
                        }
                        break;
                    case 1://有乘客下单
                    case 2://已发车，行程中
                    case 9://车主抢单，乘客未支付
                    case 11://车主抢单，乘客已支付
                    case 12://车主抢单，已发车，行车中
                        if (listener != null) {
                            listener.onDriverTravelClick(unFinishTravel);
                        }
                        break;

                    case 3://乘客发布的行程，等待接单
                        if (listener != null) {
                            listener.onNoDriver(unFinishTravel);
                        }
                        break;
                    case 6://乘客订的车主的行程，已支付
                    case 7://乘客订的车主的行程，已发车
                    case 8://乘客订的车主的行程，行程中
                        if (listener != null) {
                            listener.onPassengerTravelClick(unFinishTravel);
                        }
                        break;
                    case 4://乘客发布的行程，车主已抢单，等待支付
                        if (listener != null) {
                            listener.onNoPayTravelClick(unFinishTravel);
                        }
                        break;
                    case 5://乘客订的车主的行程，未支付
                        if (listener != null) {
                            listener.onNoPayOrderClick(unFinishTravel);
                        }
                        break;
                }
            }
        }
    }

    public interface OnTravelClickListener {
        void onDriverTravelClick(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel);

        void onPassengerTravelClick(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel);

        void onNoPayTravelClick(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel);

        void onNoPayOrderClick(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel);

        void onNoDriver(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel);
    }


}
