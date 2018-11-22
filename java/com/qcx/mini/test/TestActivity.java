package com.qcx.mini.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcx.mini.R;
import com.qcx.mini.activity.BalanceInfoActivity;
import com.qcx.mini.activity.RealNameActivity;
import com.qcx.mini.adapter.ItemizedAccountListAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.QuDialog;
import com.qcx.mini.entity.ItemizedAccountEntity;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.QRCodeUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @Override
    public int getLayoutID() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
//        ((TextView)findViewById(R.id.ttttt)).setText(String.format(Locale.CHINA,"%d*%d",UiUtils.getPixelV(),UiUtils.getPixelH()));

    }

}
