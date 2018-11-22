package com.qcx.mini.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ItemGrayDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    int mSpace;
    int lastPositionSpace;
    private final ColorDrawable mDivider;
    private int startPosition;

    public ItemGrayDecoration(int space,int color) {
        this.mSpace =(int) (space* UiUtils.SCREENRATIO);
        mPaint=new Paint();
        mPaint.setColor(color);
        mDivider = new ColorDrawable(color);
    }

    public void setStartPosition(int position){
        this.startPosition=position;
    }

    public void setLastPositionSpace(int lastPositionSpace) {
        this.lastPositionSpace = (int)(lastPositionSpace*UiUtils.SCREENRATIO);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position=parent.getChildAdapterPosition(view);
        if(position>=startPosition){
            if(position==state.getItemCount()-1){
                outRect.bottom=mSpace+lastPositionSpace;
            }else {
                outRect.bottom=mSpace;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorizontalLines(c,parent);
    }

    /**
     * 绘制垂直布局 水平分割线
     */
    int count=0;
    private void drawHorizontalLines(Canvas c, RecyclerView parent) {
        final int itemCount = parent.getChildCount();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < itemCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) return;
            if(parent.getChildAdapterPosition(child)<startPosition) {
//                return;
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top +mSpace;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
