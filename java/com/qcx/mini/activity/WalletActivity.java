package com.qcx.mini.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;

import butterknife.BindView;

public class WalletActivity extends BaseActivity {
    @Override
    public int getLayoutID() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("钱包");
    }
}
