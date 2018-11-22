package com.qcx.mini.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.qcx.mini.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/26.
 *
 */

public abstract class BaseDialog extends DialogFragment {
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(getLayoutId(), container, false);
        bind = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog=getDialog();
        if(dialog!=null){
            initDialog(dialog);
            Window window=dialog.getWindow();
            if(window!=null){
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = getWidth();
                lp.height = getHeight();
                lp.gravity= getGravity();
                window.setDimAmount(getDimAmount());
                window.setAttributes(lp);
                window.setBackgroundDrawableResource(getBackgroundDrawableResource());
            }
        }
    }

    protected void initDialog(Dialog dialog){}

    @Override
    public int getTheme() {
        return R.style.style_bottom_dialog;
    }

    public int getHeight(){
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public int getWidth(){
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    public int getGravity(){
        return Gravity.CENTER;
    }

    public int getBackgroundDrawableResource(){
        return R.drawable.bg_transparent;
    }

    public float getDimAmount(){
        return 0.5f;
    }

    public abstract int getLayoutId();
    public abstract void initView(View view);
}
