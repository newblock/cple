package com.qcx.mini.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/6/22.
 */

public class TestViewGroup extends FrameLayout {
    public TestViewGroup(Context context) {
        super(context);
    }

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if(getChildCount()>0){
            View childView=getChildAt(0);
            int childHeight=childView.getMeasuredHeight();
            int childWidth=childView.getMeasuredWidth();
            LogUtil.i(childWidth+"   "+childHeight);
            childView.layout(0,0,childWidth,childHeight);
        }
    }
}
