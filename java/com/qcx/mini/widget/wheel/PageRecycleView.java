package com.qcx.mini.widget.wheel;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/1/5.
 */

public class PageRecycleView extends RecyclerView {

    private float mXDown;
    private float mXMove;
    private float mXLastMove;
    private int mTouchSlop=5;

    public PageRecycleView(Context context) {
        super(context);
    }

    public PageRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mXDown = ev.getRawX();
//                mXLastMove = mXDown;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mXMove = ev.getRawX();
//                float diff = Math.abs(mXMove - mXDown);
//                mXLastMove = mXMove;
//                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
//                if (diff > mTouchSlop) {
//                    return true;
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                mXMove = event.getRawX();
//                mXLastMove = mXMove;
//                break;
//            case MotionEvent.ACTION_UP:
//                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
//                float m = mXDown - event.getRawX();
//                if (m > 0 && m > getWidth() / 6) {
//                    LogUtil.i("ttttt if");
//
//                } else if (m < 0 && m < -getWidth() / 6) {
//                    LogUtil.i("ttttt else");
//                }
////                invalidate();
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

}
