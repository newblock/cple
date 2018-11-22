package com.qcx.mini.widget.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/3/12.
 */

public class SuspendItemDecoration extends RecyclerView.ItemDecoration {
    private int mGroupHeight=100;
    private int mLeftMargin=10;
    private Paint mGroutPaint;
    private Paint mTextPaint;

    public SuspendItemDecoration(){
        super();
        mGroutPaint=new Paint();
        mGroutPaint.setColor(Color.RED);
        mTextPaint=new Paint();
        mTextPaint.setColor(Color.BLUE);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        String groupId = pos+"个";
        if (groupId == null) return;
        //只有是同一组的第一个才显示悬浮栏
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = mGroupHeight;
        }
    }
    //判断是不是组中的第一个位置
    //根据前一个组名，判断当前是否为新的组
    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            String prevGroupId = pos - 1+"个";
            String groupId = pos+"个";
            return !TextUtils.equals(prevGroupId, groupId);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int itemCount = state.getItemCount();
        final int childCount = parent.getChildCount();
        final int left = parent.getLeft() + parent.getPaddingLeft();
        final int right = parent.getRight() - parent.getPaddingRight();
        String preGroupName="";      //标记上一个item对应的Group
        String currentGroupName = "";       //当前item对应的Group
         for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupName = currentGroupName;
            currentGroupName = i+"个";
            if (currentGroupName == null || TextUtils.equals(currentGroupName, preGroupName))
                continue;
            int viewBottom = view.getBottom();
            float top = Math.max(mGroupHeight, view.getTop());//top 决定当前顶部第一个悬浮Group的位置
             LogUtil.i("Top="+top+" viewBottom="+viewBottom+" viewTop="+view.getTop());
            if (position + 1 < itemCount) {
                //获取下个GroupName
                String nextGroupName = position + 1+"个";
                //下一组的第一个View接近头部
                if (!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                    top = viewBottom;
                }
            }
            //根据top绘制group
            c.drawRect(left, top - mGroupHeight, right, top, mGroutPaint);
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            //文字竖直居中显示
            float baseLine = top - (mGroupHeight - (fm.bottom - fm.top)) / 2 - fm.bottom;
            c.drawText(currentGroupName, left + mLeftMargin, baseLine, mTextPaint);
        }
    }



}