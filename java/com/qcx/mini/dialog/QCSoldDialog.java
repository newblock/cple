package com.qcx.mini.dialog;

import android.view.Gravity;
import android.view.View;

import com.qcx.mini.R;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/31.
 */

public class QCSoldDialog extends BaseDialog {

    @OnClick(R.id.dialog_qc_sold_submit)
    void onClickc(){
        new InputPasswordDialog()
                .show(getFragmentManager(),"");
    }
    @Override
    public int getLayoutId() {
        return R.layout.dialog_qc_sold;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public float getDimAmount() {
        return 0;
    }
}
