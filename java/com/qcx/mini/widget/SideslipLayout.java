package com.qcx.mini.widget;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SystemUtil;
import com.qcx.mini.utils.UiUtils;

/**
 * 侧滑翻页
 * Created by Administrator on 2017/12/22.
 */

public class SideslipLayout extends LinearLayout {
    LayoutParams params;
    int targetIndex;//当前界面
    int lastPageIndex;//
    private View[] childViews;
    private ISideslipLayoutAdapter adapter;
    private int padding = (int) (16 * UiUtils.getPixelH() / 375.0);
    private int maxCount;
    /**
     * 用于完成滚动操作的实例
     */
    private Scroller mScroller;

    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;

    /**
     * 手机按下时的屏幕坐标
     */
    private float mXDown;

    /**
     * 手机当时所处的屏幕坐标
     */
    private float mXMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float mXLastMove;

    /**
     * 界面可滚动的左边界
     */
    private int leftBorder;

    /**
     * 界面可滚动的右边界
     */
    private int rightBorder;

    public SideslipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
//        params=new LinearLayout.LayoutParams(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (params == null) {
                params = (LayoutParams) childView.getLayoutParams();
                params.width = width - (int)(2*padding);
                if(i!=0){
//                    params.setMargins(padding/2,0,0,0);
                }
            }
            // 为ScrollerLayout中的每一个子控件测量大小
            childView.setLayoutParams(params);
//            if(i!=0){
//                childView.setPadding(padding/2,0,0,0);
//            }
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
//                int left=((LayoutParams)childView.getLayoutParams()).leftMargin;
               if(i==0){
                   childView.layout(i * childView.getMeasuredWidth()+padding, 0, (i + 1) * childView.getMeasuredWidth()+padding, childView.getMeasuredHeight());
               }else {
                   childView.layout(i * (childView.getMeasuredWidth()+padding/2)+padding, 0, (i + 1) * (childView.getMeasuredWidth()+padding/2)+padding, childView.getMeasuredHeight());

               }
            }
            // 初始化左右边界值
            if (getChildCount() > 0) {
                leftBorder = getChildAt(0).getLeft();
                rightBorder = getChildAt(getChildCount() - 1).getRight();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                int scrolledX = (int) (mXLastMove - mXMove);
                if (getScrollX() + scrolledX < leftBorder-padding) {
                    if (!(scrolledX < 0 && targetIndex == 0)) scrollTo(leftBorder - padding, 0);
                    return true;
                } else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
                    if (!(scrolledX > 0 && targetIndex == getChildCount() - 1))
                        scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy(scrolledX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                float m = mXDown - event.getRawX();
                if (m > 0 && m > getWidth() / 6) {
                    if (targetIndex < getChildCount() - 1) {
                        targetIndex++;
                    }
                } else if (m < 0 && m < -getWidth() / 6) {
                    if (targetIndex > 0) targetIndex--;
                }
                if(lastPageIndex!=targetIndex){//页面有改动
                    if(adapter!=null&&targetIndex<adapter.getItemCount()-2){
                        adapter.onCreateView(this,childViews[(targetIndex+2)%3],targetIndex+2);
                    }
                }
                lastPageIndex=targetIndex;
                int dx = targetIndex * (getWidth() -padding)-(targetIndex*padding/2) - getScrollX();
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public void setAdapter(ISideslipLayoutAdapter adapter){
        this.removeAllViews();
        this.adapter=adapter;
        maxCount=adapter.getItemCount();
        if(maxCount<3){
            childViews=new View[maxCount];
        }else {
            childViews=new View[3];
        }
        for(int i=0;i<childViews.length;i++){
            childViews[i]=adapter.onCreateView(this,null,i);
        }
        for (int i=0;i<adapter.getItemCount();i++){
            removeView(childViews[i%3]);
            addView(childViews[i%3]);
        }
    }

    public void toPage(int pageNum){
        if(pageNum>=maxCount){
            pageNum=maxCount-1;
        }else if(pageNum<0){
            pageNum=0;
        }
        int dx = pageNum * (getWidth() -padding)-(pageNum*padding/2);
        mScroller.startScroll(-padding, 0, dx, 0);
        targetIndex=pageNum;
//        if(pageNum!=0){
//            if(adapter!=null&&pageNum<adapter.getItemCount()) adapter.onCreateView(this,childViews[(targetIndex-1)%3],targetIndex-1);
//        }
//        if(adapter!=null&&pageNum<adapter.getItemCount()) adapter.onCreateView(this,childViews[(targetIndex)%3],targetIndex);
//        if(adapter!=null&&pageNum<adapter.getItemCount()) adapter.onCreateView(this,childViews[(targetIndex+1)%3],targetIndex+1);
//        if(adapter!=null&&pageNum<adapter.getItemCount()) adapter.onCreateView(this,childViews[(targetIndex+2)%3],targetIndex+2);
    }
}
