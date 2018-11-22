package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.verify.VerifyException;
import com.qcx.mini.verify.VerifyUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改提现账号
 */
public class CashAccountChangeActivity extends BaseActivity {
    private String account;
    @BindView(R.id.cash_account_change_input)
    EditText et_input;

    @OnClick(R.id.cash_account_change_submit)
    void submit() {
        String account = et_input.getText().toString();
        try {
            VerifyUtil.verifyAliCashAccount(account);
            saveAlipayAccount(account);
        } catch (VerifyException e) {
            e.printStackTrace();
            ToastUtil.showToast(e.getMessage());
        }
    }

    @OnClick(R.id.cash_account_change_del)
    void del(){
        if(TextUtils.isEmpty(et_input.getText())){
            return;
        }
        new QAlertDialog()
                .setTitleText("您是否要删除提现账号？")
                .setSureClickListener(new QAlertDialog.OnDialogClick() {
                    @Override
                    public void onClick(QAlertDialog dialog) {
                        saveAlipayAccount("");
                        dialog.dismiss();
                    }
                })
                .setBTN(QAlertDialog.BTN_TWOBUTTON)
                .setImg(QAlertDialog.IMG_ALERT)
                .show(getSupportFragmentManager(),"");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_cash_account_change;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("修改账号", true, false);
        account = getIntent().getStringExtra("account");
        et_input.setText(account);

        if (!TextUtils.isEmpty(account)) {
            et_input.setSelection(account.length());
        }
    }

    private void saveAlipayAccount(final String aliPay) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("aliPay", aliPay);
        Request.post(URLString.saveAlipay, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity = (IntEntity) t;
                if (intEntity.getStatus() == 200) {
                    Intent intent = new Intent();
                    account=aliPay;
                    intent.putExtra("account", account);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    onError("修改失败");
                }
            }
        });
    }
}
