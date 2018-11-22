package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 显示全部高度的RecyclerView 并且不处理滑动事件
 * Created by Administrator on 2018/7/2.
 */

public class AllHeightRecyclerView extends RecyclerView {
    public AllHeightRecyclerView(Context context) {
        super(context);
        setNestedScrollingEnabled(false);
    }

    public AllHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setNestedScrollingEnabled(false);
    }

    public AllHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setNestedScrollingEnabled(false);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int allHeight=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, allHeight);
    }
}
