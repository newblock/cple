package com.qcx.mini.widget.pageRecyclerView;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/2/8.
 */

public class HorizontalPageManger extends RecyclerView.LayoutManager {

    private int horizontalScrollOffset = 0;
    private int totalWidth = 0;
    private int pageMargin=0;
    //保存所有的Item的上下左右的偏移量信息
    private SparseArray<Rect> allItemFrames = new SparseArray<>();
    //记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
    private SparseBooleanArray hasAttachedItems = new SparseBooleanArray();


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //如果没有item，直接返回
        if (getItemCount() <= 0) return;
        // 跳过preLayout，preLayout主要用于支持动画
        if (state.isPreLayout()) {
            return;
        }

        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);

        int offsetX=0;
        for(int i=0;i<getItemCount();i++){
            View view=recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            //最后，将View布局
//            layoutDecorated(view, offsetX, 0,offsetX +width,  height);
            //将竖直方向偏移量增大height

            Rect frame = allItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            frame.set(offsetX, 0, offsetX+width, height);
            // 将当前的Item的Rect边界数据保存
            allItemFrames.put(i, frame);
            // 由于已经调用了detachAndScrapAttachedViews，因此需要将当前的Item设置为未出现过
            hasAttachedItems.put(i, false);

            offsetX =offsetX+ width;
            if(i!=getItemCount()-1){
                offsetX+=pageMargin;
            }
        }
        totalWidth=Math.max(offsetX,getHorizontalSpace());
        recycleAndFillItems(recycler, state);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        //实际要滑动的距离
        int travel = dx;

//        如果滑动到最顶部
        if (horizontalScrollOffset + dx < 0) {
            travel = -horizontalScrollOffset;
        } else if (horizontalScrollOffset + dx > totalWidth - getHorizontalSpace()) {//如果滑动到最底部
            travel = totalWidth - getHorizontalSpace() - horizontalScrollOffset;
        }

//        将竖直方向的偏移量+travel
        horizontalScrollOffset += travel;

        // 平移容器内的item
        offsetChildrenHorizontal(-travel);
        recycleAndFillItems(recycler, state);
        return travel;
    }

    /**
     * 回收不需要的Item，并且将需要显示的Item从缓存中取出
     */
    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) { // 跳过preLayout，preLayout主要用于支持动画
            return;
        }

        // 当前scroll offset状态下的显示区域
        Rect displayFrame = new Rect(horizontalScrollOffset, 0, horizontalScrollOffset+getHorizontalSpace(),getVerticalSpace());

        /**
         * 将滑出屏幕的Items回收到Recycle缓存中
         */
        Rect childFrame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childFrame.left = getDecoratedLeft(child);
            childFrame.top = getDecoratedTop(child);
            childFrame.right = getDecoratedRight(child);
            childFrame.bottom = getDecoratedBottom(child);
            //如果Item没有在显示区域，就说明需要回收
            if (!Rect.intersects(displayFrame, childFrame)) {
                //回收掉滑出屏幕的View
                LogUtil.i("removeAndRecycleView");
                removeAndRecycleView(child, recycler);
                hasAttachedItems.put(i,false);
            }
        }

        //重新显示需要出现在屏幕的子View
        for (int i = 0; i < getItemCount(); i++) {
            if (Rect.intersects(displayFrame, allItemFrames.get(i))) {
                View scrap = recycler.getViewForPosition(i);
                measureChildWithMargins(scrap, 0, 0);
                addView(scrap);

                Rect frame = allItemFrames.get(i);
                //将这个item布局出来
                layoutDecorated(scrap,
                        frame.left- horizontalScrollOffset,
                        frame.top ,
                        frame.right- horizontalScrollOffset,
                        frame.bottom);
                hasAttachedItems.put(i,true);

            }
        }
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }



    public void setPageMargin(int pageMargin) {
        this.pageMargin = pageMargin;
    }

}
