package com.qcx.mini.dialog;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.PayEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.AliPay;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.WeChatPay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/7.
 */

public class PayDialog extends BaseDialog {
    private PayEntity payEntity;
    private int patType= ConstantValue.PayType.ZHIFUBAO;
    private PayDialogListener listener;


    @BindView(R.id.dialog_pay_title)
    TextView tv_countDown;

    @BindView(R.id.dialog_pay_ticket_price)
    TextView tv_ticketPrice;

    @BindView(R.id.dialog_pay_passenger_num)
    TextView tv_num;

    @BindView(R.id.dialog_pay_price_all)
    TextView tv_price;

    @BindView(R.id.dialog_pay_extra_money_view)
    View v_pickUp;
    @BindView(R.id.dialog_pay_extra_price)
    TextView tv_extraMoney;

    @BindView(R.id.dialog_pay_submit)
    TextView tv_submit;

    @BindView(R.id.dialog_pay_zfb_check_btn)
    ImageView iv_zhifubao;
    @BindView(R.id.dialog_pay_wx_check_btn)
    ImageView iv_weixin;

    @OnClick(R.id.dialog_pay_zfb_check_view)
    void zhifubao(){
        iv_zhifubao.setImageResource(R.mipmap.icon_check);
        iv_weixin.setImageResource(R.mipmap.icon_uncheck);
        patType= ConstantValue.PayType.ZHIFUBAO;
    }

    @OnClick(R.id.dialog_pay_wx_check_view)
    void weixin(){
        iv_weixin.setImageResource(R.mipmap.icon_check);
        iv_zhifubao.setImageResource(R.mipmap.icon_uncheck);
        patType= ConstantValue.PayType.WEIXING;
    }

    @OnClick(R.id.dialog_pay_submit)
    void pay(){
        if(!isPay){
            ToastUtil.showToast("请稍后重试");
            return;
        }
        if(payEntity==null) {
            ToastUtil.showToast("请稍后");
            return;
        }
        if(patType== ConstantValue.PayType.ZHIFUBAO){
            aliPay(payEntity.getNopaySign().getAli());
        }else {
            weChatPay(payEntity.getNopaySign());
        }
    }

    @OnClick(R.id.dialog_pay_back)
    void delete(){
        dismiss();
    }


    @OnClick(R.id.dialog_fragment_cancel_order)
    void cancel(){
        new QAlertDialog()
                .setBTN(QAlertDialog.BTN_TWOBUTTON)
                .setImg(QAlertDialog.IMG_ALERT)
                .setSureClickListener(new QAlertDialog.OnDialogClick() {
                    @Override
                    public void onClick(QAlertDialog dialog) {
                        cancelOrder();
                        dialog.dismiss();
                    }
                })
                .setTitleText("您是否要取消该订单？")
                .show(getFragmentManager(),"cancelOrderDialog");
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_pay;
    }

    @Override
    public void initView(View view) {
        if(payEntity==null){
            getData();
        }else {
            initInfo();
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    private void getData(){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.nopay, params, new EntityCallback(PayEntity.class) {
            @Override
            public void onSuccess(Object t) {
                payEntity= (PayEntity) t;
                if(payEntity!=null&&payEntity.getNopaySign()!=null){
                    initInfo();
                }else {
                    onError("获取支付信息失败，请稍后重试");
                }
            }

            @Override
            public void onError(String errorInfo) {
                if(!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
            }
        });
    }


    private void initInfo(){
        if(tv_ticketPrice!=null){
            tv_ticketPrice.setText(String.format("%s元/位", CommonUtil.formatPrice(payEntity.getTicketPrice(),1)));
            tv_num.setText(String.valueOf(payEntity.getBookSeats()));
            tv_price.setText(CommonUtil.formatPrice(payEntity.getPrice(),1));
            tv_submit.setText(String.format("确认支付 %s元",CommonUtil.formatPrice(payEntity.getPrice(),1)));

            if(payEntity.isPickUp()){
                v_pickUp.setVisibility(View.VISIBLE);
                tv_extraMoney.setText(String.format("%s元", CommonUtil.formatPrice(payEntity.getExtraMoney(),1)));
            }else {
                v_pickUp.setVisibility(View.GONE);
            }

            long timeA = payEntity.getNopaySign().getCreatetime();
            long timeB = payEntity.getTimeStamp();
            long tmp =(timeA-timeB)+payEntity.getPayTimeout()*60*1000;
            countDown(tmp);
        }
    }


    private boolean isPay=false;
    private CountDownTimer timer;
    private void countDown(long time) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(time, 999) {
            @Override
            public void onTick(long millisUntilFinished) {
                long d = millisUntilFinished / 1000;
                final String text;
                if(d>1800){
                    isPay=false;
                    text="时间错误";
                } else if(d>180){
                    isPay=true;
                    text="剩余支付时间180+S";
                }else if(d>0){
                    isPay=true;
                    text="剩余支付时间"+d+"S";
                }else {
                    isPay=false;
                    text="支付超时";
                }
                tv_countDown.setText(text);
            }

            @Override
            public void onFinish () {
                if(listener!=null){
                    listener.payOvertime(PayDialog.this);
                }
            }
        };
        timer.start();
    }

    @Override
    public void onDestroyView() {
        if(timer!=null){
            timer.cancel();
        }
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventStatus status){
        if(status==EventStatus.PAY_SUCCESS){
            paySuccess();
        }
    }

    private void aliPay(String ali){
        LogUtil.e(ali);
        if(TextUtils.isEmpty(ali)){
            ToastUtil.showToast("获取支付信息失败，请稍后重试");
        }else {
            new AliPay(getActivity()).setOnAliPayListener(new AliPay.OnAliPayListener() {
                @Override
                public void onSuccess() {
                    super.onSuccess();
                    paySuccess();
                }

                @Override
                public void onWait() {
                    super.onWait();
                    // : 17/6/12 这里应该可以算支付成功吧
                    paySuccess();
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                    // : 17/6/12 不做处理 用户可能会切换支付方式
                }
            }) .doPay(new String(Base64.decode(ali, Base64.DEFAULT)));
        }

    }

    private void weChatPay(PayEntity.NopaySign entity){
       if(entity.getWxMap()==null||entity.getWxMap().getPrepayid()==null){
            ToastUtil.showToast("获取支付信息失败，请稍后重试");
        }else {
            new WeChatPay(getContext())
                    .pay(entity.getWxMap().getPartnerid()
                            , entity.getWxMap().getPrepayid()
                            , entity.getWxMap().getNoncestr()
                            , entity.getWxMap().getTimestamp()
                            , entity.getWxMap().getSign());
        }

    }

    private void paySuccess(){
        EventBus.getDefault().post(EventStatus.SHOW_MAIN_RED_POINT);
        if (listener!=null&&payEntity!=null&&payEntity.getOrdersList()!=null&&payEntity.getOrdersList().size()>0){
            listener.paySuccess(payEntity.getOrdersList().get(0).getOrdersTravelId());
        }
        dismiss();
    }

    private void cancelOrder(){
        if(payEntity==null) {
            ToastUtil.showToast("请稍后");
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("token",User.getInstance().getToken());
        params.put("ordersId",payEntity.getOrdersId());
        Request.post(URLString.ordersCancel, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                ToastUtil.showToast("订单取消成功");
                if(listener!=null){
                    listener.cancelledOrder();
                }
                dismiss();
            }

            @Override
            public void onError(String errorInfo) {

            }
        });
    }

    public PayDialog setPayEntity(PayEntity payEntity) {
        this.payEntity = payEntity;
        return  this;
    }

    public PayDialog setListener(PayDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public interface PayDialogListener{
        void paySuccess(String ordersId);
        void cancelledOrder();
        void payOvertime(PayDialog dialog);
    }

}
