package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.InputPasswordDialog;
import com.qcx.mini.entity.BankAccountListEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.BigDecimalUtils;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class WithdrawActivity extends BaseActivity {
    private final static int CHANGE_ACCOUNT = 20;
    private double allMoney;
    private double rate = 0.006;
    private BankAccountListEntity.BankAccount account;
    @BindView(R.id.withdraw_money)
    TextView tv_money;
    @BindView(R.id.withdraw_input_money)
    EditText et_input;
    @BindView(R.id.withdraw_account_text)
    TextView tv_account;
    @BindView(R.id.withdraw_money_hint)
    TextView tv_hint;
    @BindView(R.id.withdraw_account_img)
    ImageView iv_bank;
    double[] money;//[0] 提现金额,[1]提现手续费

    @OnClick(R.id.withdraw_all)
    void all() {
        double[] doubles = moneyChanged(allMoney);
        et_input.setText(String.valueOf(doubles[0]));
        et_input.setSelection(et_input.length());
    }

    @OnClick(R.id.withdraw_account_view)
    void changeAccount() {
        Intent intent;
        if(account==null||TextUtils.isEmpty(account.getBankCard())||account.getBankCard().length()<10){
             intent = new Intent(this, AddBankAccountActivity.class);
        }else {
            intent = new Intent(this, BankAccountListActivity.class);
        }
        intent.putExtra("account", account);
        startActivityForResult(intent, CHANGE_ACCOUNT);
    }

    private InputPasswordDialog inputPasswordDialog;
    @OnClick(R.id.withdraw_submit)
    void submit() {
        if(money==null||money.length!=2||money[0]<=0||money[1]<=0){
            return;
        }
        if(inputPasswordDialog==null){
            inputPasswordDialog=new InputPasswordDialog()
                    .setListener(new InputPasswordDialog.OnPasswordInputListener() {
                        @Override
                        public void inputFinish(String password,InputPasswordDialog dialog) {
                            withdraw(password);
                            dialog.dismiss();
                        }
                    });
        }
        inputPasswordDialog.setTitle("提现");
        inputPasswordDialog.setPrice(money[0]);
        inputPasswordDialog.setDescribe(String.format(Locale.CHINA,"额外扣除%s元服务费",CommonUtil.forMatPriceNoZero(money[1],2)));
        inputPasswordDialog.show(getSupportFragmentManager(),"");
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("money", allMoney);
        intent.putExtra("account", account);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHANGE_ACCOUNT:
                    if (data != null) {
                        account = data.getParcelableExtra("account");
                        showAccount();
                    }
                    break;
            }
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("提现", true, false);
        allMoney = getIntent().getDoubleExtra("money", 0);
        account = getIntent().getParcelableExtra("account");
        tv_money.setText(String.format("当前余额%s元", CommonUtil.formatPrice(allMoney, 2)));
        showAccount();
        et_input.setFilters(new InputFilter[]{lengthFilter});
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String moneyText = et_input.getText().toString();
                if (!TextUtils.isEmpty(moneyText)) {
                    moneyText = moneyText.replace(",", "");
                    money=moneyChanged(Double.valueOf(moneyText));
                }else {
                    money=null;
                }
            }
        });
    }

    /**
     * 获取提现金额和手续费
     * @param money 输入的提现金额
     * @return double[0] 提现金额,double[1] 手续费
     */
    private double[] moneyChanged(double money) {
        double[] m = new double[2];
        double rM = BigDecimalUtils.multiply(money, rate);
        rM=BigDecimalUtils.round(rM,2);
        if (rM < 2) {
            rM = 2;
        }
        if (money > allMoney) {
            tv_hint.setTextColor(0xFFD0021B);
            tv_hint.setText("提现金额过大");
        } else if (rM + money > allMoney) {
            m[0] = BigDecimalUtils.divide((1+rate),allMoney,2);
            if (m[0] < 2) {
                m[0] = 2;
            }
            m[1] = BigDecimalUtils.subtract(allMoney, m[0]);
            tv_hint.setTextColor(0xFFD0021B);
            tv_hint.setText(String.format("实际到账%s元,手续费%s元", CommonUtil.formatPrice(m[0], 2), CommonUtil.formatPrice(m[1], 2)));
        } else {
            m[0] = money;
            m[1] = rM;
            tv_hint.setTextColor(0xFFC8C9CC);
            tv_hint.setText(String.format(Locale.CHINA, "提现手续费%s%%,费用%s元", CommonUtil.forMatPriceNoZero(rate * 100, 2), CommonUtil.formatPrice(m[1], 2)));

        }

        return m;
    }

    private void showAccount() {
        if (account != null && !TextUtils.isEmpty(account.getBankCard()) && account.getBankCard().length() > 10) {
            String accountStr = String.format(Locale.CHINA, "%s(%s)", CommonUtil.getBankName(account.getBank()), account.getBankCard().substring(account.getBankCard().length() - 4, account.getBankCard().length()));
            Picasso.with(this)
                    .load(CommonUtil.getCardImgUrl(account.getBank()))
                    .into(iv_bank);
            tv_account.setText(accountStr);
        } else {
            tv_account.setText("请添加提现账号");
            iv_bank.setImageBitmap(null);
        }
    }

    private void withdraw(String password){
        try {
            if (account == null || TextUtils.isEmpty(account.getBankCard())||account.getBankCard().length()<10) {
                ToastUtil.showToast("无效的提现账号");
                return;
            }
            String p = et_input.getText().toString();
            final double m = Double.parseDouble(p);
            if (allMoney < 2) {
                ToastUtil.showToast("余额大于2元可提现");
                return;
            }
            if (m < 2) {
                ToastUtil.showToast("提现金额不得低于2元");
                return;
            }
            if (m > allMoney) {
                ToastUtil.showToast("提现金额过大");
                return;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("payType", 0);
            params.put("enchshment", m);
            params.put("password",password);
            params.put("bankcard",account.getBankCard());
            showLoadingDialog();
            Request.post(URLString.withDraw, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity intEntity = (IntEntity) t;
                    switch (intEntity.getStatus()){
                        case 200:
                            ToastUtil.showToast("提现成功");
                            allMoney -= m;
                            finish();
                            break;
                        case -519://支付密码不正确返回
                            ToastUtil.showToast("密码输入错误");
                            break;
                        case -106://未实名认证
                            ToastUtil.showToast("未实名认证");
                            break;
                        case -510://余额不够支付提现手续费
                            ToastUtil.showToast("提现金额过大");
                            break;
                        case -4://银行卡不正确 || 提现金额有误
                            ToastUtil.showToast("银行卡或提现金额有误");
                            break;
                    }
                    hideLoadingDialog();
                }

                @Override
                public void onError(String errorInfo) {
                    super.onError(errorInfo);
                    hideLoadingDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast("请输入正确的提现金额");
        }
    }

    private InputFilter lengthFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // source:当前输入的字符
            // start:输入字符的开始位置
            // end:输入字符的结束位置
            // dest：当前已显示的内容
            // dstart:当前光标开始位置
            // dent:当前光标结束位置

            LogUtil.i("source=" + source + ",start=" + start + ",end=" + end
                    + ",dest=" + dest.toString() + ",dstart=" + dstart
                    + ",dend=" + dend);
            if (dest.length() == 0 && source.equals(".")) {
                return "0.";
            }
            String dValue = dest.toString();
            int index = dValue.indexOf('.');
            if (index > 0 && dstart > index && dValue.length() - index > 2) {
                return "";
            }
            return source;
        }

    };

}
