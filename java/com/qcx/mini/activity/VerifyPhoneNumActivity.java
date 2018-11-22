package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.NetUtil;
import com.qcx.mini.net.QuCallback;
import com.qcx.mini.widget.numSoftKeyboart.NumSoftKeyboard;
import com.qcx.mini.widget.numSoftKeyboart.OnNumInputListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class VerifyPhoneNumActivity extends BaseActivity {
    private CountDownTimer countDownTimer;
    private StringBuilder mVerifyCode;
    private int maxCodeLength = 6;
    private int[] codeLength = new int[]{4, 6};

    @BindView(R.id.verify_phone_num_count_down_text)
    TextView tv_countDown;
    @BindView(R.id.verify_phone_num_num_soft_keyboard)
    NumSoftKeyboard mNumSoftKeyboard;
    @BindView(R.id.verify_phone_num_verify_code_text)
    TextView tv_code;
    @BindView(R.id.verify_phone_num_submit)
    TextView tv_submit;
    @BindView(R.id.verify_phone_num_verify_phone)
    TextView tv_phone;

    @OnClick(R.id.verify_phone_num_submit)
    void submit() {
        if (canSubmit) {
            NetUtil.checkCaptcha(mVerifyCode.toString(), new QuCallback<IntEntity>() {
                @Override
                public void onSuccess(IntEntity intEntity) {
                    if (intEntity.getStatus() == 200) {
                        startActivity(new Intent(VerifyPhoneNumActivity.this, SetPasswordActivity.class));
                        finish();
                    }else if(intEntity.getStatus()==-3){
                        onError("验证码输入错误");
                    }else {
                        onError("验证失败,请重试");
                    }
                }
            });

        }
    }

    @OnClick(R.id.verify_phone_num_count_down_text)
    void sendC() {
        sendCaptcha();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_verify_phone_num;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("验证手机号", true, false);
        tv_phone.setText(String.format(Locale.CHINA, "请输入手机号%d****%d接收到的短信验证码", User.getInstance().getPhoneNumber() / 100000000L, User.getInstance().getPhoneNumber() % 1000L));
        mNumSoftKeyboard.setListener(new OnNumInputListener() {
            @Override
            public void onInputNum(int num) {
                if (mVerifyCode == null) {
                    mVerifyCode = new StringBuilder();
                }

                if (mVerifyCode.length() < maxCodeLength) {
                    mVerifyCode.append(num);
                    verifyCodeChanged();
                }

            }

            @Override
            public void onDeleteNum() {
                if (mVerifyCode != null && mVerifyCode.length() > 0) {
                    mVerifyCode.deleteCharAt(mVerifyCode.length() - 1);
                    verifyCodeChanged();
                }
            }
        });
        verifyCodeChanged();
        sendCaptcha();
    }

    private boolean isSend = false;

    private void sendCaptcha() {
        if (isSend) {
            return;
        }
        showLoadingDialog();
        isSend = true;
        NetUtil.sendCaptcha(User.getInstance().getPhoneNumber(), new QuCallback<IntEntity>() {
            @Override
            public void onSuccess(IntEntity intEntity) {
                hideLoadingDialog();
                if (intEntity.getStatus() == 200) {
                    startCountDown();
                } else {
                    onError("");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                isSend = false;
                hideLoadingDialog();
                tv_countDown.setTextColor(0xFF4186E7);
                tv_countDown.setText("重新获取");
            }
        });
    }

    private boolean canSubmit = false;

    private void verifyCodeChanged() {
        tv_code.setText(mVerifyCode);
        canSubmit = false;
        if (mVerifyCode != null) {
            for (int i : codeLength) {
                if (i == mVerifyCode.length()) {
                    canSubmit = true;
                    break;
                }
            }
        }
        if (canSubmit) {
            tv_submit.setBackgroundResource(R.drawable.bg_circular_gradient_blue);
        } else {
            tv_submit.setBackgroundResource(R.drawable.bg_circular_gray1);
        }
    }

    private void startCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        tv_countDown.setTextColor(0xFFC8C9CC);
        countDownTimer = new CountDownTimer(60000, 999) {
            @Override
            public void onTick(long millisUntilFinished) {
                long d = millisUntilFinished / 1000;
                tv_countDown.setText(String.format(Locale.CHINA, "%ds后重发", d));
            }

            @Override
            public void onFinish() {
                isSend = false;
                tv_countDown.setTextColor(0xFF4186E7);
                tv_countDown.setText("重新获取");
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
