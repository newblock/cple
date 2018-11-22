package com.qcx.mini.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.dexfun.layout.DexLayoutActivity;
import com.qcx.mini.R;
import com.qcx.mini.activity.MainActivity;
import com.qcx.mini.dialog.LoadingDialog;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/27.
 *
 */

public abstract class BaseActivity extends DexLayoutActivity {
    private static List<Activity> mAcitvity = new ArrayList<>();//当前打开的ACTIVITY
    private LoadingDialog mLoadingDialog;
    public RxPermissions mRxPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        mRxPermissions = new RxPermissions(this);
        if (!setStatusBar()) {
            UiUtils.setStatusBarLightMode(this, Color.TRANSPARENT);
            UiUtils.setFitsSystemWindows(this, false);
        }
        if (mAcitvity != null) {
            mAcitvity.add(this);
        }
        initView(savedInstanceState);
    }

    public abstract int getLayoutID();

    public abstract void initView(Bundle savedInstanceState);

    public void initTitleBar(String title, boolean showLeft, boolean showRight) {
        try {
            TextView titleBar_title = findViewById(R.id.title_bar_title);
//            if(titleBar_title!=null){
            titleBar_title.setText(title);
//            }

            View titleBar_left = findViewById(R.id.title_bar_left);
            if (titleBar_left != null) {
                if (!showLeft) titleBar_left.setVisibility(View.GONE);
                else titleBar_left.setVisibility(View.VISIBLE);
                titleBar_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTitleLeftClick(v);
                    }
                });
            }

            View titleBar_right = findViewById(R.id.title_bar_right);
            if (titleBar_right != null) {
                if (!showRight) titleBar_right.setVisibility(View.GONE);
                else titleBar_right.setVisibility(View.VISIBLE);
                titleBar_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTitleRightClick(v);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("don't find a titlebar view");
        }
    }

    public void initTitleBar(String title) {
        initTitleBar(title, true, true);
    }

    public void onTitleLeftClick(View v) {
        finish();
    }

    public void onTitleRightClick(View v) {
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            try {
                mLoadingDialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        if (!mLoadingDialog.isVisible()&&!mLoadingDialog.isAdded()&&!mLoadingDialog.isRemoving()) {
            try {
                mLoadingDialog.show(getSupportFragmentManager(), "");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean setStatusBar() {
        return false;
    }

    public void finishActivity(String activityName) {
        for (int i = 0; i < mAcitvity.size(); i++) {
            if (mAcitvity.get(i).getClass().getSimpleName().equals(activityName)) {
                mAcitvity.get(i).finish();
                mAcitvity.remove(i);
            }
        }
    }

    public boolean haveMainAcitvity() {
        if (mAcitvity != null) {
            for (int i = 0; i < mAcitvity.size(); i++) {
                if (mAcitvity.get(i).getClass().getSimpleName().equals(MainActivity.class.getSimpleName())) {
                    return true;
                }
            }

        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAcitvity != null) mAcitvity.remove(this);
        if (mRxPermissions != null) {
            mRxPermissions = null;
        }
    }
}
