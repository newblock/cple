package com.qcx.mini.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;

public class AuthenticationStep3Activity extends BaseActivity {

    @Override
    public int getLayoutID() {
        return R.layout.activity_authentication_step3;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("车主认证",true,false);
    }
}
