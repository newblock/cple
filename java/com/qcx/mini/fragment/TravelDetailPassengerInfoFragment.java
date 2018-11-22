package com.qcx.mini.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.TravelDetail_DriverActivity;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.ItemsDialog;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.HeadEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.listener.TravelDetailPassengerClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/29.
 */

public class TravelDetailPassengerInfoFragment extends BaseFragment {
    private HeadEntity entity;
    private boolean isShowTravelInfo = true;
    private TravelDetailPassengerClickListener clickListener;
    private int tag;

    @BindView(R.id.fragment_travel_detail_passenger_info_travel)
    View v_travel;
    @BindView(R.id.fragment_travel_detail_passenger_info_passenger)
    View v_passenger;
    @BindView(R.id.fragment_travel_detail_passenger_info_view)
    LinearLayout ll_view;
    @BindView(R.id.fragment_travel_detail_no_passenger)
    LinearLayout v_noPassenger;
    @BindView(R.id.fragment_travel_detail_no_passenger_view)
    LinearLayout v_noPassengerView;


    private ImageView iv_icon;
    private ImageView iv_attention;
    private TextView tv_name;
    private TextView tv_travelStartTime;
    private TextView tv_travelStartAddress;
    private TextView tv_travelEndAddress;
    public TextView tv_travelPriceType;
    private TextView tv_travelPrice;
    private View v_passengerTravelView;
    private TextView tv_passengerNum;

    @OnClick(R.id.fragment_travel_detail_passenger_info_function_phone)
    void call(){
        if(entity!=null){
            DialogUtil.call(getContext(),new String[]{String.valueOf(entity.getPhone())});
        }
    }

    @OnClick(R.id.fragment_travel_detail_passenger_info_function_navi)
    void navi() {
        if (entity == null) return;
        String[] items = {"导航至上车点", "导航至下车点"};
        final double[] start = entity.getStart();
        final double[] end = entity.getEnd();
        final String startAddress = entity.getStartAddress();
        final String endAddress = entity.getEndAddress();

        final ItemsDialog dialog = new ItemsDialog(getContext(), items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        DialogUtil.navigation(start, startAddress, ConstantValue.NavigationMode.DRIVE,getContext());
                        break;
                    case 1:
                        DialogUtil.navigation(end, endAddress, ConstantValue.NavigationMode.DRIVE,getContext());
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.fragment_travel_detail_passenger_info_function_more)
    void passengerMore(){
        if(entity!=null&&!TextUtils.isEmpty(entity.getOrdersId())){
            String[] items = {"删除此乘客","取消行程","客服"};
            final ItemsDialog dialog = new ItemsDialog(getContext(), items, null);
            dialog.itemTextColor(Color.BLACK)
                    .cancelText(Color.BLACK)
                    .cancelText("取消")
                    .isTitleShow(false)
                    .layoutAnimation(null).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            if(clickListener!=null){
                                clickListener.onDeletePassenger(entity);
                            }
                            break;
                        case 1:
                            if(clickListener!=null){
                                clickListener.onCancelTravel();
                            }
                            break;
                        case 2:
                            DialogUtil.call(getContext(),null);
                            break;
                    }
                    dialog.dismiss();
                }
            });
        }else if(entity!=null){

            String[] items = {"取消行程","客服"};
            final ItemsDialog dialog = new ItemsDialog(getContext(), items, null);
            dialog.itemTextColor(Color.BLACK)
                    .cancelText(Color.BLACK)
                    .cancelText("关闭")
                    .isTitleShow(false)
                    .layoutAnimation(null).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            if(clickListener!=null){
                                clickListener.onCancelTravel();
                            }
                            break;
                        case 1:
                            DialogUtil.call(getContext(),null);
                            break;
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.fragment_travel_detail_no_passenger_more)
    void noPassengerMore(){
        LogUtil.i("set phone position  item2="+entity.getPhone()+" tag="+tag);
        String[] items = {"取消行程","客服"};
        final ItemsDialog dialog = new ItemsDialog(getContext(), items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if(clickListener!=null){
                            clickListener.onCancelTravel();
                        }
                        break;
                    case 1:
                        DialogUtil.call(getContext(),null);
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.fragment_travel_detail_no_passenger_share)
    void onShare(){
        if(clickListener!=null){
            clickListener.onShare();
        }
    }

    @OnClick(R.id.fragment_travel_detail_no_passenger_delete)
    void onDelete(){
        if(clickListener!=null){
            clickListener.onDeleteSeat();
        }
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        iv_icon = view.findViewById(R.id.item_travel_top_icon);
        tv_name = view.findViewById(R.id.item_travel_top_info);
        iv_attention = view.findViewById(R.id.item_travel_top_attention);


        tv_travelStartTime = view.findViewById(R.id.item_travel_start_time);
        tv_travelStartAddress = view.findViewById(R.id.item_travel_startAddress);
        tv_travelEndAddress = view.findViewById(R.id.item_travel_endAddress);
        tv_travelPriceType = view.findViewById(R.id.item_travel_price_type);
        tv_travelPrice = view.findViewById(R.id.item_travel_price);
        v_passengerTravelView = view.findViewById(R.id.item_travel_peoples_view);
        tv_passengerNum = view.findViewById(R.id.item_travel_peoples_text);

        v_passengerTravelView.setVisibility(View.VISIBLE);
        tv_passengerNum.setVisibility(View.VISIBLE);

        LayoutTransition lt = new LayoutTransition();
//        lt.disableTransitionType(LayoutTransition.DISAPPEARING);
        lt.setDuration(200);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "alpha", 1F, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0).
                setDuration(lt.getDuration(LayoutTransition.DISAPPEARING));
        lt.setAnimator(LayoutTransition.DISAPPEARING, animator2);

        ll_view.setLayoutTransition(lt);
        v_noPassenger.setLayoutTransition(lt);

        setShowTravelInfo(isShowTravelInfo);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_travel_detail_passenger_info;
    }

    public boolean isShowTravelInfo() {
        return isShowTravelInfo;
    }

    public void setShowTravelInfo(boolean showTravelInfo) {
        isShowTravelInfo = showTravelInfo;

        if (v_travel == null) {
            return;
        }
        if (isShowTravelInfo) {
            v_travel.setVisibility(View.VISIBLE);
            v_passenger.setVisibility(View.VISIBLE);
            v_noPassengerView.setVisibility(View.VISIBLE);
        } else {
            v_travel.setVisibility(View.GONE);
            v_passenger.setVisibility(View.GONE);
            v_noPassengerView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private void setTransition(LayoutTransition mTransitioner) {

        /**
         * view出现时 view自身的动画效果
         */

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(null, "rotationY", 90F, 0F).
                setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING));

        mTransitioner.setAnimator(LayoutTransition.APPEARING, animator1);


        /**
         * view 消失时，view自身的动画效果
         */
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "rotationX", 0F, 90F, 0F).
                setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
        mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animator2);
        /**
         * view 动画改变时，布局中的每个子view动画的时间间隔
         */
        mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
        mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
        /**
         * 为什么这里要这么写？具体我也不清楚，ViewGroup源码里面是这么写的，我只是模仿而已
         * 不这么写貌似就没有动画效果了，所以你懂的！
         */
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
        /**
         * view出现时，导致整个布局改变的动画
         */
        PropertyValuesHolder animator3 = PropertyValuesHolder.ofFloat("scaleX", 1F, 2F, 1F);
        final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, animator3).
                setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
        changeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setScaleX(1.0f);
            }
        });
        mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
        /**
         * view消失，导致整个布局改变时的动画
         */
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.5f, 2f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder pvhRotation =
                PropertyValuesHolder.ofKeyframe("scaleX", kf0, kf1, kf2);
        final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).
                setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        changeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setScaleX(1.0f);
            }
        });
        mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
    }

    public void setData(HeadEntity headEntity) {
        this.entity = headEntity;
        LogUtil.i("set phone position  item2="+entity.getPhone()+" tag="+tag);
        if (entity != null&&entity.getPhone()>0) {
            ll_view.setVisibility(View.VISIBLE);
            v_noPassenger.setVisibility(View.GONE);
            Picasso.with(getContext())
                    .load(entity.getPicture())
                    .error(R.mipmap.img_me)
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .into(iv_icon);

            StringBuilder builder = new StringBuilder();
            String name = entity.getName();
            if (!TextUtils.isEmpty(name)) {
                builder.append(name);
                builder.append("·");
            }
            switch (entity.getSex()) {
                case ConstantValue.SexType.MAN:
                    builder.append("男");
                    break;
                case ConstantValue.SexType.WOMAN:
                    builder.append("女");
                    break;
            }
            tv_name.setText(builder);
            if(entity.isAttention()){
                iv_attention.setImageResource(R.mipmap.btn_followed_mini);
            }else {
                iv_attention.setImageResource(R.mipmap.btn_follow_mini);
            }
            iv_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(entity.isAttention()){
                        cancelAttention(entity,iv_attention);
                    }else {
                        attention(entity,iv_attention);
                    }
                }
            });
            tv_travelStartTime.setText(DateUtil.getTimeStr(entity.getStartTime(), "MM月dd日HH:mm"));
            tv_travelStartAddress.setText(entity.getStartAddress());
            tv_travelEndAddress.setText(entity.getEndAddress());
            tv_travelPrice.setText(CommonUtil.formatPrice(entity.getPrice(),1));
            tv_passengerNum.setText(String.valueOf(entity.getPassengerNum()));
        }else {
            ll_view.setVisibility(View.GONE);
            v_noPassenger.setVisibility(View.VISIBLE);
        }
    }

    public void setClickListener(TravelDetailPassengerClickListener clickListener) {
        this.clickListener = clickListener;
    }


    private void attention(final HeadEntity data, final ImageView iv_attention) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());//attentione
        params.put("attentione", data.getPhone());
        Request.post(URLString.changAttention, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    data.setAttention(true);
                    iv_attention.setImageResource(R.mipmap.btn_followed_mini);
                    if(clickListener!=null){
                        clickListener.onAttenetionChanged();
                    }
                    new CenterImgDialog().show(getFragmentManager(), "");
                } else {
                    onError("操作失败");
                }
            }

        });
    }

    public void cancelAttention(final HeadEntity data, final ImageView iv_attention) {
        DialogUtil.unfollowDialog(getActivity(), data.getPhone(), new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    data.setAttention(false);
                    iv_attention.setImageResource(R.mipmap.btn_follow_mini);
                    if(clickListener!=null){
                        clickListener.onAttenetionChanged();
                    }
                } else {
                    onError("操作失败");
                }
            }
        });
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
