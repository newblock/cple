package com.qcx.mini.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.utils.UiUtils;

/**
 * Created by Administrator on 2018/7/4.
 */

public class FirstTextItemDecoration extends RecyclerView.ItemDecoration {
    private String text;
    private Paint mPaint;

    private int textHeight;
    private int textSize;
    private int textColor;

    private int height;
    private int mgLeft;

    public FirstTextItemDecoration(String text, int textSize, int textColor,int height,int mgLeft) {
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.height=height;
        this.mgLeft=mgLeft;

        mPaint=new Paint();
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setAntiAlias(true);
        if(TextUtils.isEmpty(text)){
            Rect rect=new Rect();
            mPaint.getTextBounds(text,0,text.length(),rect);
            textHeight= rect.height();
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount=parent.getChildCount();
        if(childCount<1){
            return;
        }
        View view=parent.getChildAt(0);
        if(view==null){
            return;
        }
        int top=view.getTop();
        c.drawText(text,mgLeft,top-(height-textHeight)/3,mPaint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view)==0){
            outRect.top=height;
        }
    }
}
