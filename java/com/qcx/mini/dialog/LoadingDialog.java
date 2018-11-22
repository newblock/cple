package com.qcx.mini.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.qcx.mini.R;

/**
 * Created by Administrator on 2018/2/1.
 */

public class LoadingDialog extends BaseDialog {

    @Override
    public int getLayoutId() {
        return R.layout.dialog_loading;
    }
    ImageView imageView;
    @Override
    public void initView(View view) {
        imageView = view.findViewById(R.id.dialog_loading_img);
        imageView.setImageResource(R.mipmap.icon_loading);
    }

    private ObjectAnimator anim;

    private void startPropertyAnim(View view) {
        if(anim==null){
            anim = ObjectAnimator.ofFloat(view, "rotation", 0f, 720f);
            anim.setDuration(1500);//动画时间
            anim.setInterpolator(new LinearInterpolator());//动画插值
            anim.setRepeatCount(ValueAnimator.INFINITE);//设置动画重复次数
        }
        anim.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        startPropertyAnim(imageView);
    }

    @Override
    protected void initDialog(Dialog dialog) {
        super.initDialog(dialog);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(anim!=null){
            anim.cancel();
        }
    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getTheme() {
        return 0;
    }

}
