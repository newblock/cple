package com.qcx.mini.base.swipeback;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.qcx.mini.R;

import java.lang.reflect.Field;

/**
 * activity的滑动返回
 * Project momodev
 * Package com.immomo.framework.base.swipeback
 * Created by tangyuchun on 5/15/16.
 */
public class SwipeBack {
    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBack() {
    }

    /**
     * 初始化滑动返回
     */
    public void initSwipeBackLayout(Activity activity, SlidingPaneLayout.PanelSlideListener listener) {
        if (activity == null || activity.getWindow() == null) {
            return;
        }
        mSwipeBackLayout = new SwipeBackLayout(activity);
        //通过反射改变mOverhangSize的值为0，这个mOverhangSize值为菜单到右边屏幕的最短距离，默认
        //是32dp，现在给它改成0
        try {
            //属性
            Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            f_overHang.setAccessible(true);
            f_overHang.set(mSwipeBackLayout, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSwipeBackLayout.setPanelSlideListener(listener);
        //设置 sliderFadeColor 右侧View滑动时遮盖一层颜色
        mSwipeBackLayout.setSliderFadeColor(Color.TRANSPARENT);
        //边缘的阴影
        mSwipeBackLayout.setShadowResourceLeft(R.drawable.swipe_back_shadow);
        //时差效果的距离 此处设置此选项无用
        //        mSwipeBackLayout.setParallaxDistance(200);

        //// TODO: 4/6/17 需要设置主题
        /**
         <!--滑动返回需要设置为透明 -->
         <item name="android:windowIsTranslucent">true</item>
         <item name="android:windowAnimationStyle">@style/Activity.SwipeBack_Anim</item>
         */
        //设置滑动返回的动画
        //        window.setWindowAnimations(R.style.Activity_SwipeBack_Anim);
    }

    /**
     * 将activity的rootView添加到mSwipeBackLayout的右边控件里
     * @param activity
     * @param contentView
     * @param params
     */
    public void addRightContent(Activity activity, View contentView, ViewGroup.LayoutParams params, boolean setBackground) {
        Window window = activity.getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        Drawable bgDrawable = decorView.getBackground();

        //窗口设置为透明
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 左边滑动view
        View leftPanel = new View(activity);
        leftPanel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSwipeBackLayout.addView(leftPanel, 0);

        //将 activity的 windowBackground 颜色值设置给右侧的panel，同时，需要将 window设置为透明
        //fix https://fabric.io/momo6/android/apps/com.immomo.momo/issues/5901867ebe077a4dccf9068e?time=last-thirty-days
        if (setBackground) {
            contentView.setBackgroundDrawable(bgDrawable);
        }

        if (params != null) {
            mSwipeBackLayout.addView(contentView, 1, params);
        } else {
            mSwipeBackLayout.addView(contentView, 1);
        }
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public boolean isSupportSwipeBack() {
        return mSwipeBackLayout != null && mSwipeBackLayout.isSupportSwipeBack();
    }

    public void setSupportSwipeBack(boolean pSupportSwipeBack) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setSupportSwipeBack(pSupportSwipeBack);
        }
    }
}
