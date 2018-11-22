package com.qcx.mini.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2018/8/17.
 */

public class DividerDecoration extends RecyclerView.ItemDecoration {
    private int height;
    private int paddingLeft;
    private int paddingRight;
    private int startPosition=0;
    private int stopPosition=Integer.MAX_VALUE;
    private int color;
    private Paint paint;

    public DividerDecoration(int height) {
        this.height = height;
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(height);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public void setStopPosition(int stopPosition) {
        this.stopPosition = stopPosition;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position=parent.getChildAdapterPosition(view);
        if(startPosition<=position&&position<=stopPosition){
            outRect.bottom=height;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(color!=-1){
            paint.setColor(color);
            int itemCount=parent.getChildCount();
            int left=parent.getPaddingLeft()+paddingLeft;
            int right=parent.getWidth()-paddingRight;
            for(int i=0;i<itemCount;i++){
                View view=parent.getChildAt(i);
                if(view==null){
                    return;
                }
                int position=parent.getChildAdapterPosition(view);
                if(position<startPosition||position>stopPosition){
                    continue;
                }
                int top;
                    top=view.getBottom()-height;
                int bottom=view.getBottom();
                c.drawLine(left,top,right,top,paint);
            }
        }
    }
}
