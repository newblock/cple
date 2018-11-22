package com.qcx.mini.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/3/22.
 */

public class FlowLayoutManger extends RecyclerView.LayoutManager {
    //保存所有的Item的上下左右的偏移量信息
    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    public FlowLayoutManger(){
        setAutoMeasureEnabled(true);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(getItemCount()<0) return;
        if (state.isPreLayout()) return;
        detachAndScrapAttachedViews(recycler);
        int offsetY=0;
        int offsetX=0;
        for(int i=0;i<getItemCount();){
            offsetX=0;
            int itemHeight=0;
            for(int j=0;j<3&&i<getItemCount();j++){
                View childView=recycler.getViewForPosition(i);
                if(childView==null){
                    LogUtil.i("childView==null position="+i);
                    continue;
                }
                addView(childView);
                measureChildWithMargins(childView, 0, 0);
                int width = getDecoratedMeasuredWidth(childView);
                int height = getDecoratedMeasuredHeight(childView);
                layoutDecorated(childView,offsetX,offsetY,offsetX+width,offsetY+height);
                offsetX+=width;
                if(itemHeight<height){
                    itemHeight=height;
                }
                i++;
            }
            offsetY+=itemHeight;
        }

    }
}
