package com.qcx.mini.base.swipeback;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

/**
 * Project momodev
 * Package com.immomo.framework.view.swipeback
 * Created by tangyuchun on 11/3/16.
 */

public class SwipeBackLayout extends SlidingPaneLayout {
    public SwipeBackLayout(Context context) {
        super(context);
        init(context);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        maxEdgeDistance = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, context.getResources().getDisplayMetrics()));
    }

    /**
     * 是否支持滑动退出
     */
    private boolean supportSwipeBack = true;
    /**
     * 设置可以触发滑动返回的最大距离（相对于屏幕边缘） 手指开始滑动距离大于此值时，将无法触发滑动
     */
    private int maxEdgeDistance = 100;
    /**
     * 是否只能在边缘处滑动 默认为true
     */
    private boolean swipedFromEdge = true;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果不支持滑动返回，则禁掉触摸事件即可
        if (!supportSwipeBack) {
            return false;
        }
        //如果只能从边缘滑动，且滑动起点小于设置的距离，才能触发滑动
        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (swipedFromEdge) {
                float x = ev.getX();
                if (x > maxEdgeDistance) {
                    return false;
                }
            }
        }
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isSupportSwipeBack() {
        return supportSwipeBack;
    }

    public void setSupportSwipeBack(boolean supportSwipeBack) {
        this.supportSwipeBack = supportSwipeBack;
    }

    /**
     * 设置可以触发滑动返回的最大距离（相对于屏幕边缘）
     *
     * @param maxEdgeDistance
     */
    public void setMaxEdgeDistance(int maxEdgeDistance) {
        this.maxEdgeDistance = maxEdgeDistance;
    }

    public void setSwipedFromEdge(boolean swipedFromEdge) {
        this.swipedFromEdge = swipedFromEdge;
    }
}
