package com.qcx.mini.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.utils.UiUtils;

/**
 * Created by Administrator on 2018/1/24.
 */

public class QAlertDialog extends DialogFragment {
    public static int BTN_TWOBUTTON=2;
    public static int BTN_ONEBUTTON=1;
    private int BTN=BTN_TWOBUTTON;
    public static int IMG_ALERT=R.mipmap.icon_pop_alerts;
    public static int IMG_CONFIRM=R.mipmap.icon_pop_confirm;
    private int img=IMG_ALERT;
    private ImageView iv_img;
    private TextView tv_title,tv_content,tv_cancle,tv_sure;
    private String mTitleText,mContentText;
    private OnDialogClick cancelClickListener;
    private OnDialogClick sureClickListener;
    private String rightText="确认";
    private boolean cancelAble=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_q_alert,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog=getDialog();
        if(dialog!=null){
            Window window=dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(UiUtils.SCREENRATIO*280);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setBackgroundDrawableResource(R.drawable.bg_circular_white);
            dialog.setCancelable(cancelAble);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view){
        iv_img=view.findViewById(R.id.dialog_content_img);
        tv_title=view.findViewById(R.id.dialog_content_title);
        tv_content=view.findViewById(R.id.dialog_content_message);
        tv_sure=view.findViewById(R.id.dialog_content_sure);
        tv_cancle=view.findViewById(R.id.dialog_content_cancel);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancelClickListener!=null){
                    cancelClickListener.onClick(QAlertDialog.this);
                }else {
                    dismiss();
                }
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sureClickListener!=null){
                    sureClickListener.onClick(QAlertDialog.this);
                }else {
                    dismiss();
                }
            }
        });

        tv_sure.setText(rightText);
        iv_img.setImageResource(img);
        if(BTN==BTN_ONEBUTTON){
            tv_cancle.setVisibility(View.GONE);
        }else if(BTN==BTN_TWOBUTTON){
            tv_cancle.setVisibility(View.VISIBLE);
        }
        tv_title.setText(mTitleText);
        tv_content.setText(mContentText);
    }

    public QAlertDialog setBTN(int BTN) {
        this.BTN = BTN;
        return this;
    }

    public QAlertDialog setTitleText(String titleText) {
        this.mTitleText = titleText;
        return this;
    }

    public QAlertDialog setContentText(String contentText) {
        this.mContentText = contentText;
        return this;
    }

    public QAlertDialog setImg(int img) {
        this.img = img;
        return this;
    }

    public OnDialogClick getCancelClickListener() {
        return cancelClickListener;
    }

    public QAlertDialog setCancelClickListener(OnDialogClick cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
        return this;
    }

    public OnDialogClick getSureClickListener() {
        return sureClickListener;
    }

    public QAlertDialog setSureClickListener(OnDialogClick sureClickListener) {
        this.sureClickListener = sureClickListener;
        return this;
    }

    public QAlertDialog setCancelAble(boolean cancelAble) {
        this.cancelAble = cancelAble;
        return this;
    }

    public interface OnDialogClick{
        void onClick(QAlertDialog dialog);
    }

    public QAlertDialog setRightText(String rightText) {
        this.rightText = rightText;
        return this;
    }
}
