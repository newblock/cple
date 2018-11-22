package com.qcx.mini.dialog;

import android.view.View;
import android.view.WindowManager;

import com.qcx.mini.R;
/**
 * Created by Administrator on 2018/1/24.
 */

public class QAlertDialog2 extends BaseDialog {
    private OnDialogClick listener;

    public QAlertDialog2 setListener(OnDialogClick listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_q_alert_2;
    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.dialog_content_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(QAlertDialog2.this);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listener!=null){
            listener.onDismiss();
        }
    }

    public interface OnDialogClick{
        void onClick(QAlertDialog2 dialog);
        void onDismiss();
    }
}
