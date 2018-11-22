package com.qcx.mini.activity;

import android.os.Bundle;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;

import butterknife.OnClick;

public class QCXAboutActivity extends BaseActivity{

    @OnClick(R.id.qcx_about_cooperation_view)
    void cooperation(){
        DialogUtil.call(this,null);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_qcx_about;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("关于",true,false);
    }
}
