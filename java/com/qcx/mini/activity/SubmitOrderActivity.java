package com.qcx.mini.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.MainClass;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.PayInfoEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.ResultCode;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.CircleTextView;
import com.qcx.mini.widget.SwitchView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SubmitOrderActivity extends BaseActivity {
    private double price;
    private int surplusSeats;
    private int bookSeats;
    private boolean buyingSafety;
    private double[] start;
    private double[] end;
    private String startAddress;
    private String endAddress;
    private String startTime;
    private long travelId;


    @BindView(R.id.submit_order_price)
    TextView tv_price;
    @BindView(R.id.submit_order_seats1)
    CircleTextView tv_seat1;
    @BindView(R.id.submit_order_seats2)
    CircleTextView tv_seat2;
    @BindView(R.id.submit_order_seats3)
    CircleTextView tv_seat3;
    @BindView(R.id.submit_order_seats4)
    CircleTextView tv_seat4;
    @BindView(R.id.submit_order_coupons_text)
    TextView tv_coupons;
    @BindView(R.id.submit_order_insurance_switch)
    SwitchView tv_insuranceSwitch;
    @BindView(R.id.submit_order_submit)
    TextView tv_submit;
    @BindView(R.id.submit_order_insurance_price)
    TextView tv_insurancePrice;

    @OnClick(R.id.submit_order_seats1)
    void seat1() {
        seatChange(1);
    }

    @OnClick(R.id.submit_order_seats2)
    void seat2() {
        seatChange(2);
    }

    @OnClick(R.id.submit_order_seats3)
    void seat3() {
        seatChange(3);
    }

    @OnClick(R.id.submit_order_seats4)
    void seat4() {
        seatChange(4);
    }

    @OnClick(R.id.submit_order_back)
    void back() {
        finish();
    }

    @OnClick(R.id.submit_order_submit)
    void submit() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("bookSeats", bookSeats);
        params.put("buyingSafety", false);
        params.put("start", start);
        params.put("end", end);
        params.put("startAddress", startAddress);
        params.put("endAddress", endAddress);
        params.put("startTime", startTime);
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
                        startActivity(new Intent(SubmitOrderActivity.this, PayActivity.class));
                        EventBus.getDefault().post(EventStatus.SHOW_MAIN_RED_POINT);
                        finish();
                        break;
                    case HAVE_NO_PAY_ORDER:
                        new AlertDialog.Builder(SubmitOrderActivity.this).setMessage("您有未支付的订单")
                                .setPositiveButton("去支付", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(SubmitOrderActivity.this, PayActivity.class));
                                        dialog.cancel();
                                        finish();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
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
    public int getLayoutID() {
        return R.layout.activity_submit_order;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        surplusSeats = getIntent().getIntExtra("surplusSeats", 0);
        start = getIntent().getDoubleArrayExtra("start");
        end = getIntent().getDoubleArrayExtra("end");
        startAddress = getIntent().getStringExtra("startAddress");
        endAddress = getIntent().getStringExtra("endAddress");
        long time = getIntent().getLongExtra("time", 0);
        if (time != 0) {
            startTime = DateUtil.getTimeStr(time, "yyyy-MM-dd HH:mm:ss");
        }
        travelId = getIntent().getLongExtra("travelId", 0);
        price = getIntent().getDoubleExtra("price", 0);
        tv_price.setText(String.format(Locale.CHINA, "%s元/位", CommonUtil.formatPrice(price,2)));
        seatChange(1);

        tv_insurancePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        initSeats(surplusSeats);
    }

    private void seatChange(int num) {
        if (num <= surplusSeats) {
            bookSeats = num;
            tv_submit.setText(String.format("合计%s 提交订单", CommonUtil.formatPrice(bookSeats * price,2)));
        } else {
            num = bookSeats;
            ToastUtil.showToast("请重新选择");
        }
        switch (num) {
            case 1:
                tv_seat1.setCircleColor(0xFF499EFF);
                tv_seat2.setCircleColor(0xFFEDEDF0);
                tv_seat3.setCircleColor(0xFFEDEDF0);
                tv_seat4.setCircleColor(0xFFEDEDF0);
                break;
            case 2:
                tv_seat1.setCircleColor(0xFFEDEDF0);
                tv_seat2.setCircleColor(0xFF499EFF);
                tv_seat3.setCircleColor(0xFFEDEDF0);
                tv_seat4.setCircleColor(0xFFEDEDF0);
                break;
            case 3:
                tv_seat1.setCircleColor(0xFFEDEDF0);
                tv_seat2.setCircleColor(0xFFEDEDF0);
                tv_seat3.setCircleColor(0xFF499EFF);
                tv_seat4.setCircleColor(0xFFEDEDF0);
                break;
            case 4:
                tv_seat1.setCircleColor(0xFFEDEDF0);
                tv_seat2.setCircleColor(0xFFEDEDF0);
                tv_seat3.setCircleColor(0xFFEDEDF0);
                tv_seat4.setCircleColor(0xFF499EFF);
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
}
