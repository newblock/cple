package com.qcx.mini.base.swipeback;

import android.support.annotation.LayoutRes;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexfun.layout.DexLayoutActivity;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;


/**
 * Project momodev
 * Package com.immomo.framework.view.swipeback
 * Created by tangyuchun on 11/3/16.
 */

public abstract class BaseSwipeBackActivity extends DexLayoutActivity {
    private SwipeBack mSwipeBack;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (isSupportSwipeBack()) {
            initSwipeBack();
        }

        if (mSwipeBack != null && mSwipeBack.getSwipeBackLayout() != null) {
            View rootView = LayoutInflater.from(this).inflate(layoutResID, null);
            mSwipeBack.addRightContent(this, rootView, null, isSetBackground());
            super.setContentView(mSwipeBack.getSwipeBackLayout());
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (isSupportSwipeBack()) {
            initSwipeBack();
        }

        if (mSwipeBack != null && mSwipeBack.getSwipeBackLayout() != null) {
            mSwipeBack.addRightContent(this, view, null, isSetBackground());
            super.setContentView(mSwipeBack.getSwipeBackLayout());
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (isSupportSwipeBack()) {
            initSwipeBack();
        }

        if (mSwipeBack != null && mSwipeBack.getSwipeBackLayout() != null) {
            mSwipeBack.addRightContent(this, view, params, isSetBackground());
            super.setContentView(mSwipeBack.getSwipeBackLayout());
        } else {
            super.setContentView(view, params);
        }
    }

    /**
     * 是否支持滑动返回，默认支持，如果子类不想有滑动返回功能，重写此方法，返回false即可
     * 如果此方法返回false，则根本不会创建 SwipeBack
     *
     * @return
     */
    protected boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 是否统一设置背景色
     * @return
     */
    protected boolean isSetBackground() {
        return true;
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBack() {
        mSwipeBack = new SwipeBack();
        mSwipeBack.initSwipeBackLayout(this, panelSlideListener);
    }

    protected void onSwipeBack() {
        finish();
        //滑动退出时，将设置单独的退出动画,此动画时间很短，是为了解决在滑动退出时，状态栏有移动的动画
        //如果不加动画过渡，发现在 7.0手机上，会闪烁
        if (isSupportSwipeBack()) {
            this.overridePendingTransition(0, R.anim.activity_slide_out_right_finish);
        }
    }

    protected void onSwipeBackStarted() {
        //页面关闭时，统一关闭输入法
//        UIUtils.hideInputMethod(this);
    }

    private SlidingPaneLayout.PanelSlideListener panelSlideListener = new SlidingPaneLayout.PanelSlideListener() {
        private boolean isFirstSlide = true;

        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            if (isFirstSlide) {
                isFirstSlide = false;
                onSwipeBackStarted();
            }
        }

        @Override
        public void onPanelOpened(View panel) {
            onSwipeBack();
        }

        @Override
        public void onPanelClosed(View panel) {
            isFirstSlide = true;
        }
    };
}
