package com.qcx.mini.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.MainClass;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.PayActivity;
import com.qcx.mini.activity.SubmitOrderActivity;
import com.qcx.mini.activity.TravelDetail_PassengerActivity;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.PayEntity;
import com.qcx.mini.entity.PayInfoEntity;
import com.qcx.mini.entity.UnOrderDriverTravelDetailEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.ResultCode;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.CircleTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/29.
 */

public class SubmitOrderDialog extends BaseDialog {
    private UnOrderDriverTravelDetailEntity.UnOrderTravelDetail travel;
    private PayDialog.PayDialogListener payListener;
    private int reserveTravelType;
    private double orderPrice;
    private int bookSeats;
    private long travelId;

    @BindView(R.id.dialog_submit_order_price)
    TextView tv_travelPrice;
    @BindView(R.id.dialog_submit_order_price_all)
    TextView tv_orderPrice;
    @BindView(R.id.dialog_submit_pickup_price)
    TextView tv_pickupPrice;
    @BindView(R.id.dialog_submit_pickup_view)
    View v_pickup;

    @BindView(R.id.dialog_submit_order_seats1)
    CircleTextView tv_seat1;
    @BindView(R.id.dialog_submit_order_seats2)
    CircleTextView tv_seat2;
    @BindView(R.id.dialog_submit_order_seats3)
    CircleTextView tv_seat3;
    @BindView(R.id.dialog_submit_order_seats4)
    CircleTextView tv_seat4;

    @OnClick(R.id.dialog_submit_order_seats1)
    void seat1() {
        seatChange(1);
    }

    @OnClick(R.id.dialog_submit_order_seats2)
    void seat2() {
        seatChange(2);
    }

    @OnClick(R.id.dialog_submit_order_seats3)
    void seat3() {
        seatChange(3);
    }

    @OnClick(R.id.dialog_submit_order_seats4)
    void seat4() {
        seatChange(4);
    }

    @OnClick(R.id.dialog_submit_order_back)
    void back() {
        dismiss();
    }

    @OnClick(R.id.dialog_submit_order_submit)
    void submit() {
        if(travel==null){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("bookSeats", bookSeats);
        params.put("buyingSafety", false);
        if(reserveTravelType==ConstantValue.ReservedTravelType.RESERVED_TRAVEL_PICKUP){
            params.put("start", travel.getMatchStart());
            params.put("end", travel.getMatchEnd());
            params.put("startAddress", travel.getMatchStartAddress());
            params.put("endAddress", travel.getMatchEndAddress());
            params.put("startTime", DateUtil.getTimeStr(travel.getPickUpStartTime(),"yyyy-MM-dd HH:mm:ss"));
            params.put("isPickUp", true);
            params.put("extraDistance", travel.getExtraDistance());
        }else {
            params.put("start", travel.getRecommendStartLocation());
            params.put("end", travel.getRecommendEndLocation());
            params.put("startAddress", travel.getRecommendStartAddress());
            params.put("endAddress", travel.getRecommendEndAddress());
            params.put("startTime", DateUtil.getTimeStr(travel.getRecommendStartTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        params.put("travelId", travelId);
        params.put("isOldApp", MainClass.isOldApp());
        Request.post(URLString.createOrder, params, new EntityCallback(PayInfoEntity.class) {
            @Override
            public void onSuccess(Object t) {
                PayInfoEntity payInfo = (PayInfoEntity) t;
                LogUtil.i(payInfo.getStatus() + "");
                ResultCode resultCode=ResultCode.getResultCode(payInfo.getStatus());
                switch (resultCode) {
                    case SUCCESS:
                        toPay(null);
                        EventBus.getDefault().post(EventStatus.SHOW_MAIN_RED_POINT);
                        dismiss();
                        break;
                    case HAVE_NO_PAY_ORDER:
                        getNoPayData();
                        break;
                    case UNKNOWN:
                        ToastUtil.showToast("创建订单失败");
                        break;
                    default:
                        ToastUtil.showToast(resultCode.getCodeDescribe());
                        break;
                }
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_submit_order;
    }

    @Override
    public void initView(View view) {
        if(travel!=null){
            initSeats(travel.getSurplusSeats());
            seatChange(1);
            tv_travelPrice.setText(String.format(Locale.CHINA,"%s元/座",CommonUtil.formatPrice(travel.getTravelPrice(),1)));
            if(reserveTravelType==ConstantValue.ReservedTravelType.RESERVED_TRAVEL_PICKUP){
                v_pickup.setVisibility(View.VISIBLE);
                tv_pickupPrice.setText(String.format(Locale.CHINA,"%s元/座",CommonUtil.formatPrice(travel.getExtraMoney(),1)));
            }else {
                v_pickup.setVisibility(View.GONE);
            }
        }
        getNoPayData();
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    private void seatChange(int num) {
        if(travel==null){
            return;
        }
        if(reserveTravelType==ConstantValue.ReservedTravelType.RESERVED_TRAVEL_PICKUP){
            orderPrice=travel.getTravelPrice()*num+travel.getExtraMoney();
        }else {
            orderPrice=travel.getTravelPrice()*num;
        }
        bookSeats=num;
        tv_orderPrice.setText(CommonUtil.formatPrice(orderPrice,1));
        switch (num) {
            case 1:
                tv_seat1.setCircleColor(0xFF4186E7);
                tv_seat2.setCircleColor(0xFFEDEDF0);
                tv_seat3.setCircleColor(0xFFEDEDF0);
                tv_seat4.setCircleColor(0xFFEDEDF0);
                break;
            case 2:
                tv_seat1.setCircleColor(0xFFEDEDF0);
                tv_seat2.setCircleColor(0xFF4186E7);
                tv_seat3.setCircleColor(0xFFEDEDF0);
                tv_seat4.setCircleColor(0xFFEDEDF0);
                break;
            case 3:
                tv_seat1.setCircleColor(0xFFEDEDF0);
                tv_seat2.setCircleColor(0xFFEDEDF0);
                tv_seat3.setCircleColor(0xFF4186E7);
                tv_seat4.setCircleColor(0xFFEDEDF0);
                break;
            case 4:
                tv_seat1.setCircleColor(0xFFEDEDF0);
                tv_seat2.setCircleColor(0xFFEDEDF0);
                tv_seat3.setCircleColor(0xFFEDEDF0);
                tv_seat4.setCircleColor(0xFF4186E7);
                break;

        }
    }

    void initSeats(int surplusSeats) {
        switch (surplusSeats) {
            case 1:
                tv_seat1.setVisibility(View.VISIBLE);
                tv_seat2.setVisibility(View.GONE);
                tv_seat3.setVisibility(View.GONE);
                tv_seat4.setVisibility(View.GONE);
                break;
            case 2:
                tv_seat1.setVisibility(View.VISIBLE);
                tv_seat2.setVisibility(View.VISIBLE);
                tv_seat3.setVisibility(View.GONE);
                tv_seat4.setVisibility(View.GONE);
                break;
            case 3:
                tv_seat1.setVisibility(View.VISIBLE);
                tv_seat2.setVisibility(View.VISIBLE);
                tv_seat3.setVisibility(View.VISIBLE);
                tv_seat4.setVisibility(View.GONE);
                break;
            case 4:
                tv_seat1.setVisibility(View.VISIBLE);
                tv_seat2.setVisibility(View.VISIBLE);
                tv_seat3.setVisibility(View.VISIBLE);
                tv_seat4.setVisibility(View.VISIBLE);
                break;

        }

    }

    private void getNoPayData(){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.nopay, params, new EntityCallback(PayEntity.class) {
            @Override
            public void onSuccess(Object t) {
                final PayEntity payEntity= (PayEntity) t;
                if(payEntity!=null&&payEntity.getNopaySign()!=null){
                    try{
                        toNoPayTravelDetail( payEntity.getOrdersList().get(0).getTravelId(),payEntity.getOrdersTravelId());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String errorInfo) {
            }
        });
    }

    private void toNoPayTravelDetail(final long travelId, final String ordersId){
        new QAlertDialog()
                .setBTN(QAlertDialog.BTN_TWOBUTTON)
                .setImg(QAlertDialog.IMG_ALERT)
                .setTitleText("您有未支付的订单")
                .setRightText("去支付")
                .setCancelClickListener(new QAlertDialog.OnDialogClick() {
                    @Override
                    public void onClick(QAlertDialog dialog) {
                        dialog.dismiss();
                        dismiss();
                    }
                })
                .setSureClickListener(new QAlertDialog.OnDialogClick() {
                    @Override
                    public void onClick(QAlertDialog dialog) {
                        Intent intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                        intent.putExtra("travelID", travelId);
                        intent.putExtra("ordersID", ordersId);
                        startActivity(intent);
                        dialog.dismiss();
                        dismiss();
                    }
                })
                .show(getFragmentManager(),"");
    }


    private void toPay(@Nullable PayEntity payEntity){
        if(payEntity!=null){
            new PayDialog()
                    .setListener(payListener)
                    .setPayEntity(payEntity)
                    .show(getFragmentManager(),"");
        }else {
            new PayDialog()
                    .setListener(payListener)
                    .show(getFragmentManager(),"");
        }
    }


    public SubmitOrderDialog setPayListener(PayDialog.PayDialogListener payListener) {
        this.payListener = payListener;
        return this;
    }

    public SubmitOrderDialog setTravel(UnOrderDriverTravelDetailEntity.UnOrderTravelDetail travel) {
        this.travel = travel;
        return this;
    }

    public SubmitOrderDialog setReserveTravelType(int reserveTravelType) {
        this.reserveTravelType = reserveTravelType;
        return this;
    }

    public SubmitOrderDialog setTravelId(long travelId) {
        this.travelId = travelId;
        return this;
    }
}
