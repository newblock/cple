package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.qcx.mini.utils.LogUtil;


/**
 * Created by Administrator on 2018/6/22.
 *
 */

public class TestView extends LinearLayout {
    private int minOffset = 20;
    private Scroller scroller;
    private VelocityTracker mVelocityTracker;

    public TestView(Context context) {
        super(context);
        init(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout();
        int childCount = getChildCount();
        if (childCount > 0) {
            int height = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                int left = params.leftMargin;
                int top = height + params.topMargin;
                int right = left + child.getMeasuredWidth();
                int bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
                height += bottom + params.bottomMargin;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);

        int childCount = getChildCount();
        if (childCount > 0) {
            if (modeH == MeasureSpec.AT_MOST || modeW == MeasureSpec.AT_MOST) {
                int height = 0;
                int width = 0;
                for (int i = 0; i < childCount; i++) {
                    height += getChildAt(i).getMeasuredHeight();
                    width += getChildAt(i).getMeasuredWidth();
                }
                if (modeH != MeasureSpec.AT_MOST) {
                    height = MeasureSpec.getSize(heightMeasureSpec);
                }
                if (modeW != MeasureSpec.AT_MOST) {
                    width = MeasureSpec.getSize(widthMeasureSpec);
                }
                setMeasuredDimension(width, height);
            }
        }
    }

    float startY;
    float startX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i("ttttttttttttttttttttt ddd ACTION_DOWN");
                startX = ev.getRawX();
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("ttttttttttttttttttttt ddd ACTION_MOVE");
                float offest = ev.getRawY() - startY;
                if (Math.abs(offest) > minOffset) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.i("ttttttttttttttttttttt ddd ACTION_UP");
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    int cX;
    int cY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (cX == 0 || cY == 0) {
            if (getChildCount() > 0) {
                View view = getChildAt(0);
                cX = view.getLeft();
                cY = view.getTop();
            }
        }
        initVelocityTracker();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i("ttttttttttttttttttttt ACTION_DOWN");
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("ttttttttttttttttttttt ACTION_MOVE");
                mVelocityTracker.addMovement(event);
                float offest = event.getRawY() - startY;
                if (Math.abs(offest) > minOffset) {

                    if (getChildCount() > 0) {
                        View view = getChildAt(0);
                        int x = (int) (event.getRawX() - startX);
                        int y = (int) (event.getRawY() - startY);
                        scrollTo(-x, -y);
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                scroller.startScroll(-(int) (event.getRawX() - startX), -(int) (event.getRawY() - startY), (int) (event.getRawX() - startX), (int) (event.getRawY() - startY));
                mVelocityTracker.computeCurrentVelocity(1000);
                float y=mVelocityTracker.getYVelocity();
                float x=mVelocityTracker.getXVelocity();
                scroller.fling(200,200,(int)-x,(int)-y,-500000,50000,-50000,50000);
                if(mVelocityTracker!=null){
                    mVelocityTracker.recycle();
                    mVelocityTracker=null;
                }
                invalidate();
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    private void initVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }
}
