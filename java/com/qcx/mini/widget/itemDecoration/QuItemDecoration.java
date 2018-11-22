package com.qcx.mini.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

/**
 * Created by Administrator on 2018/5/21.
 */

public class QuItemDecoration extends RecyclerView.ItemDecoration {
    private int startPosition;
    private int top;
    private int left;
    private int right;
    private int bottom;
    private int lastBottom;
    private int color=-1;
    private Paint paint;

    public QuItemDecoration() {
        init();
    }

    public QuItemDecoration(int startPosition, int top, int left, int right, int bottom, int lastBottom) {
        this.startPosition = startPosition;
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.lastBottom = lastBottom;
        init();
    }

    private void init(){
        paint=new Paint();
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(color!=-1){
            int itemCount=parent.getChildCount();
            int left=parent.getPaddingLeft()+this.left;
            int right=parent.getWidth()-this.right;
            for(int i=0;i<itemCount;i++){
                View view=parent.getChildAt(i);
                if(view==null){
                    return;
                }
                if(parent.getChildAdapterPosition(view)<startPosition){
                    continue;
                }
                int top;
                if(i==itemCount-1){
                    top=view.getBottom()-this.lastBottom;
                }else {
                    top=view.getBottom()-this.bottom;
                }
                int bottom=view.getBottom();
                c.drawRect(left,top,right,bottom,paint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if(position>=startPosition){
            outRect.top = top;
            outRect.left=left;
            outRect.right=right;
            outRect.bottom=bottom;
            if (position == state.getItemCount() - 1) {
                outRect.bottom = lastBottom;
            }
        }
    }

    public void setStartPosition(int position){
        this.startPosition=position;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public void setLastBottom(int lastBottom) {
        this.lastBottom = lastBottom;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }
}
