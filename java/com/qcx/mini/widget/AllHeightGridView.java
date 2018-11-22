package com.qcx.mini.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2018/4/23.
 */

public class AllHeightGridView extends GridView {
    public AllHeightGridView(Context context) {
        super(context);
    }

    public AllHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
