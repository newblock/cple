package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

/**
 * 翻页式滑动
 * Created by Administrator on 2018/1/4.
 */

public class PageRecycleView extends RecyclerView {
    private int totalPage;
    private int currentPage;
    private int shortestDistance; // 超过此距离的滑动才有效
    private float downX = 0; // 手指按下的X轴坐标
    private float slideDistance = 0; // 滑动的距离
    private float scrollX = 0; // X轴当前的位置

    public PageRecycleView(Context context) {
        super(context);
    }

    public PageRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        // 计算总页数
//        totalPage = adapter.getItemCount();
        LogUtil.i("totalPage=" + totalPage);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getAdapter() != null) totalPage = getAdapter().getItemCount();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        shortestDistance = getMeasuredWidth() / 8;
    }

    /**
     * 0: 停止滚动且手指移开; 1: 开始滚动; 2: 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
     */
    private int scrollState = 0; // 滚动状态
    private float mXMove;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXMove = e.getRawX();
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mXMove = e.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                float d = mXMove - e.getRawX();
                if (d > shortestDistance) {
                    if (currentPage < totalPage) {
                        currentPage += 1;
                    } else {
                        currentPage = totalPage;
                    }
                }
                if (d < -shortestDistance) {
                    if (currentPage > 0) {
                        currentPage -= 1;
                    } else {
                        currentPage = 0;
                    }
                }
                smoothScrollBy((int) ((currentPage - 1) * (getWidth() - 24 * UiUtils.SCREENRATIO) - scrollX), 0);

                return true;
        }

        return super.onTouchEvent(e);
    }

    public void setPage(int page) {
        currentPage = page;
        scrollBy((int) ((page - 1) * (getWidth() - 24 * UiUtils.SCREENRATIO) - scrollX), 0);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        scrollX += dx;
        if (scrollState == 1) {
            slideDistance += dx;
        }
        super.onScrolled(dx, dy);
    }
}
