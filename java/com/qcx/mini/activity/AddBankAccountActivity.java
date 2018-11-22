package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.QuDialog;
import com.qcx.mini.entity.BankAccountListEntity;
import com.qcx.mini.entity.Entity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.ToastUtil;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qcx.mini.utils.CommonUtil.getBankName;

/**
 * 添加银行卡
 *
 * todo 姓名没有做
 */
public class AddBankAccountActivity extends BaseActivity {
    StringBuilder cardNum;
    @BindView(R.id.add_bank_account_account_text)
    EditText et_account;
    @BindView(R.id.add_bank_account_bankName)
    TextView tv_bankName;
    @BindView(R.id.add_bank_account_submit)
    TextView tv_submit;
    @BindView(R.id.add_bank_account_logo)
    ImageView iv_bankLogo;

    private boolean canSubmit=false;
    private String bank;//银行标识
    private String bankCard;//银行卡号

    @OnClick(R.id.add_bank_account_submit)
    void submit(){
        if(!canSubmit){
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("bank",bank);
        params.put("bankCard",bankCard);
        Request.post(URLString.addBankCard, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity= (IntEntity) t;
                if(intEntity!=null&&intEntity.getStatus()==200){
                    ToastUtil.showToastCenter("添加成功");
                    Intent intent=new Intent();
                    BankAccountListEntity.BankAccount account=new BankAccountListEntity.BankAccount();
                    account.setBankCard(bankCard);
                    account.setBank(bank);
                    intent.putExtra("account",account);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }


    private QuDialog reminderDialog;
    @OnClick(R.id.add_bank_account_account_describe)
    void describe(){
        if(reminderDialog==null){
            reminderDialog=new QuDialog()
                    .setTitle("持卡人说明")
                    .setMessage("为保证资金安全,只能绑定认证用户本人\r\n的银行卡")
                    .setRightBtn("知道了", new QuDialog.OnClickListener() {
                        @Override
                        public void onClick(QuDialog dialog) {
                            dialog.dismiss();
                        }
                    });
        }
        reminderDialog.show(getSupportFragmentManager(),"");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_add_bank_account;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //0226
        initTitleBar("添加银行卡", true, false);
        cardNum = new StringBuilder();
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_submit.setBackgroundResource(R.drawable.bg_circular_gray1);
                canSubmit=false;
                if (et_account.getText().length() > 12) {
                    getBankCode(et_account.getText().toString());
                }else {
                    tv_bankName.setText("");
                    iv_bankLogo.setImageBitmap(null);
                }
            }
        });
    }

    private void getBankCode(final String cardNum) {
        String url = String.format(Locale.CHINA, "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=%s&cardBinCheck=true", cardNum);
        OkGo.<String>get(url)
                .execute(new EntityCallback(BankInfoEntity.class) {
                    @Override
                    public void onSuccess(Object t) {
                        BankInfoEntity bankInfo= (BankInfoEntity) t;
                        if(bankInfo!=null&&bankInfo.validated){
                            tv_bankName.setText(getBankName(bankInfo.getBank()));
                            tv_submit.setBackgroundResource(R.drawable.bg_circular_gradient_blue);
                            bank=bankInfo.getBank();
                            bankCard=cardNum;

                            Picasso.with(AddBankAccountActivity.this)
                                    .load(getBackLogo(bank))
                                    .into(iv_bankLogo);
                            canSubmit=true;
                        }else {
                            tv_submit.setBackgroundResource(R.drawable.bg_circular_gray1);
                            canSubmit=false;
                        }
                    }
                });
    }

    private String getBackLogo(String bankCode){
        return String.format(Locale.CHINA,"%s%s.png",URLString.bankLogo,bankCode);
    }

    private static class BankInfoEntity extends Entity{
        /**
         * {"bank":"CMB","validated":true,"cardType":"DC","key":"6214830257453836","messages":[],"stat":"ok"}
         *
         * {"validated":false,"key":"621483025745383","stat":"ok","messages":[{"errorCodes":"CARD_BIN_NOT_MATCH","name":"cardNo"}]}
         */
        private String bank;
        private boolean validated;
        private String cardType;
        private String key;
        private String stat;

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public boolean isValidated() {
            return validated;
        }

        public void setValidated(boolean validated) {
            this.validated = validated;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }
    }

}
