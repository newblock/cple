package com.qcx.mini.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.QAlertDialog;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {
    private PayEntity payEntity;
    private int payType=0;//0阿里支付,1微信支付

    @BindView(R.id.pay_count_down)
    TextView tv_countDown;
    @BindView(R.id.pay_ticket_price)
    TextView tv_ticketPrice;
    @BindView(R.id.pay_num)
    TextView tv_num;
    @BindView(R.id.pay_price)
    TextView tv_price;
    @BindView(R.id.pay_submit)
    TextView tv_submit;
    @BindView(R.id.pay_zhifubao_check)
    ImageView iv_zhifubao;
    @BindView(R.id.pay_weixin_check)
    ImageView iv_weixin;
    @BindView(R.id.pay_ticket_price_view)
    View v_TicketPrice;

    @OnClick(R.id.pay_back)
    void back(){
        finish();
    }

    @OnClick(R.id.pay_zhifubao_view)
    void zhifubao(){
        iv_zhifubao.setImageResource(R.mipmap.icon_check);
        iv_weixin.setImageResource(R.mipmap.icon_uncheck);
        payType=0;
    }

    @OnClick(R.id.pay_weixin_view)
    void weixin(){
        iv_weixin.setImageResource(R.mipmap.icon_check);
        iv_zhifubao.setImageResource(R.mipmap.icon_uncheck);
        payType=1;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_pay;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        getData();
    }

    @OnClick(R.id.pay_submit)
    void pay(){
        if(!isPay){
            ToastUtil.showToast("请稍后重试");
            return;
        }
        if(payEntity==null) {
            ToastUtil.showToast("请稍后");
            return;
        }
        if(payType==0){
            aliPay(payEntity.getNopaySign().getAli());
        }else {
            weChatPay(payEntity.getNopaySign());
        }
    }

    @OnClick(R.id.pay_cancel_order)
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
               .show(getSupportFragmentManager(),"cancelOrderDialog");
    }



    private void getData(){
        Map<String,Object> params=new HashMap<>();
//        params.put("phone", User.getInstance().getPhoneNumber());
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
        tv_ticketPrice.setText(String.format("%s元/位",CommonUtil.formatPrice(payEntity.getTicketPrice(),2)));
        tv_num.setText(String.valueOf(payEntity.getBookSeats()));
        tv_price.setText(String.format("%s元/位",CommonUtil.formatPrice(payEntity.getPrice(),2)));
        tv_submit.setText(String.format("合计%s元 提交订单",CommonUtil.formatPrice(payEntity.getPrice(),2)));
        if(payEntity.isSeckill()){
            v_TicketPrice.setVisibility(View.GONE);
        }else {
            v_TicketPrice.setVisibility(View.VISIBLE);
        }

        long timeA = payEntity.getNopaySign().getCreatetime();
        long timeB = payEntity.getTimeStamp();
        long tmp =(timeA-timeB)/1000+payEntity.getPayTimeout()*60;
        starCountDown(tmp);
    }

    private boolean isPay=false;
    private Timer mTimer;
    private TimerTask timerTask;
    private void starCountDown(final long time){
        if(mTimer==null){
            mTimer=new Timer();
            timerTask=new TimerTask() {
                long d=time;
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    final String text;
                    if(d>1800){
                        isPay=false;
                        text="时间错误";
                        d--;
                    } else if(d>180){
                        isPay=true;
                        text="剩余支付时间180+S";
                        d--;
                    }else if(d>0){
                        isPay=true;
                        text="剩余支付时间"+d+"S";
                        d--;
                    }else {
                        isPay=false;
                        text="支付超时";
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_countDown.setText(text);
                        }
                    });
                }
            };
        }
        mTimer.schedule(timerTask,0,1000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer!=null)mTimer.cancel();
        EventBus.getDefault().unregister(this);
    }

    private void aliPay(String ali){
        LogUtil.e(ali);
        if(TextUtils.isEmpty(ali)){
            ToastUtil.showToast("获取支付信息失败，请稍后重试");
        }else {
            new AliPay(this).setOnAliPayListener(new AliPay.OnAliPayListener() {
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
        LogUtil.i("weChatPay ="+entity.getWxMap().getPartnerid()+" getPrepayid="+entity.getWxMap().getPrepayid()+
                "  getNoncestr="+entity.getWxMap().getNoncestr()+" getTimestamp="+entity.getWxMap().getTimestamp()+" getSign="+entity.getWxMap().getSign());
        if(entity.getWxMap()==null||entity.getWxMap().getPrepayid()==null){
            ToastUtil.showToast("获取支付信息失败，请稍后重试");
        }else {
            new WeChatPay(this)
                    .pay(entity.getWxMap().getPartnerid()
                            , entity.getWxMap().getPrepayid()
                            , entity.getWxMap().getNoncestr()
                            , entity.getWxMap().getTimestamp()
                            , entity.getWxMap().getSign());
        }


//            new WeChatPay(this)
//                    .pay(mPayEntity.getNopaySign().getPartnerid()
//                            , mPayEntity.getNopaySign().getPrepayid()
//                            , mPayEntity.getNopaySign().getNoncestr()
//                            , mPayEntity.getNopaySign().getTimestamp()
//                            , mPayEntity.getNopaySign().getSign());

    }

    private void paySuccess(){
        finishActivity(TravelDetail_NoPayActivity.class.getSimpleName());
        finishActivity(TravelDetail_PassengerActivity.class.getSimpleName());
        finishActivity(TravelNoneActivity.class.getSimpleName());
        finishActivity(SearchActivity.class.getSimpleName());
        EventBus.getDefault().post(EventStatus.SHOW_MAIN_RED_POINT);
        try {
            String ordersId=payEntity.getOrdersList().get(0).getOrdersTravelId();
            Intent intent=new Intent(this,TravelDetail_PassengerActivity.class);
            intent.putExtra("ordersID",ordersId);
            intent.putExtra("travelID",payEntity.getTravelId());
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
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
                finishActivity(TravelDetail_NoPayActivity.class.getSimpleName());
                finishActivity(TravelDetail_PassengerActivity.class.getSimpleName());
                finish();
            }

            @Override
            public void onError(String errorInfo) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventStatus status){
        if(status==EventStatus.PAY_SUCCESS){
            paySuccess();
        }
    }
}
