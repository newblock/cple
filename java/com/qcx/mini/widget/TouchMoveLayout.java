package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/1/17.
 */

public class TouchMoveLayout extends FrameLayout {
    private float lastX;
    private float lastY;
    private RecyclerView mRecycleView;

    public TouchMoveLayout(@NonNull Context context) {
        super(context);
    }

    public TouchMoveLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchMoveLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void initScrollView(View view){
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof RecyclerView) {
                mRecycleView = (RecyclerView) childView;
                return;
            }else if(childView instanceof ViewGroup){
                initScrollView(childView);
            }else {
                break;
            }
        }
    }

    private boolean recycleViewisTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || !recyclerView.canScrollVertically(-1)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    boolean isTopDown=false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean isTop = recycleViewisTop(mRecycleView);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isTop){
                    isTopDown=true;
                }else {
                    isTopDown=false;
                }
                lastX = ev.getRawX();
                lastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.i(isTop+" "+isTopDown);
                if(isTop&&isTopDown){
                    float moveY = ev.getRawY() - lastY;
                    if (listener != null) {
                        listener.onMoveY(moveY);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setmRecycleView(RecyclerView mRecycleView) {
        this.mRecycleView = mRecycleView;
    }

    public interface OnMoveDistanceListener {
        void onMoveY(float y);
    }

    OnMoveDistanceListener listener;

    public void setListener(OnMoveDistanceListener listener) {
        this.listener = listener;
    }
}
