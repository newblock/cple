package com.qcx.mini.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;

/**
 * Created by Administrator on 2017/12/8.
 */

public class DragView extends FrameLayout {
    private ViewDragHelper dragHelper;
    private OnScrollListener scrollListener;
    private boolean statusChanged=false;

    private int minDistance = 5;
    private int minMoveDistance = UiUtils.getSize(30);
    private float downLocation;
    private int left, top;
    private MoveType moveType = MoveType.DOWN;
    private boolean isMoving = false;
    private int bottomHideHeight = UiUtils.getSize(228);
    private int topBottomHideHeight = UiUtils.getSize(-50);//TOP状态时 底部隐藏的size
    private boolean dragAble = true;
    private int topY;
    private int bottomY;

    public enum MoveType {
        TOP, DOWN
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragView);
        if (typedArray != null) {
            bottomHideHeight = UiUtils.getSize(typedArray.getDimensionPixelSize(R.styleable.DragView_bottomHideHeight, 228));
            topBottomHideHeight = UiUtils.getSize(typedArray.getDimensionPixelSize(R.styleable.DragView_topBottomHideHeight, -50));
            typedArray.recycle();
        }
        if(dragHelper==null){
            dragHelper = ViewDragHelper.create(this, 1.0f, callback);
        }
    }

    private ViewDragHelper.Callback  callback=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int mPadding = getPaddingLeft();
            int maxLeft = getWidth() - mPadding - child.getWidth();
            left = Math.max(Math.min(maxLeft, left), mPadding);
            return left;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            if (moveType == MoveType.TOP) {
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                dragHelper.settleCapturedViewAt(0, dHeight + topBottomHideHeight);
            } else {
                params.gravity = Gravity.BOTTOM;
                view.setLayoutParams(params);
                top=getHeight()-(view.getHeight()-bottomHideHeight);
                dragHelper.settleCapturedViewAt(left, top);
            }
            invalidate();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
//                int mPadding = getPaddingTop();
//                int maxBottom = getHeight() - mPadding - child.getHeight();
//                top = Math.max(Math.min(maxBottom, top), mPadding);
            return top;
        }
    };

    View view;

    int dHeight = 0;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        if (getChildCount() > 0) {
            final View child = getChildAt(0);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            int childLeft = 0;
            int childTop = 0;
            int parentBottom = bottom - top;
            dHeight = parentBottom - height;

            int gravity = lp.gravity;
            final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
            topY = parentBottom - height - lp.bottomMargin + topBottomHideHeight;
            bottomY = parentBottom - height - lp.bottomMargin + bottomHideHeight;
            switch (verticalGravity) {
                case Gravity.TOP:
                    moveType=MoveType.TOP;
                    childTop = topY;
                    break;
                case Gravity.BOTTOM:
                    moveType=MoveType.DOWN;
                    childTop = bottomY;
                    break;
                default:
                    childTop = parentBottom - height - lp.bottomMargin + bottomHideHeight;
            }
            child.layout(childLeft, childTop, childLeft + width, childTop + height);
        }

        try {
            view = getChildAt(0);
            if (this.top == 0) {
                this.top = view.getTop();
                this.left = view.getLeft();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dragAble) {
            dragHelper.processTouchEvent(event);
        }
        boolean b = false;
        LogUtil.i("onTouchEvent " + b);
        return super.onTouchEvent(event);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (dragAble) {
            dragHelper.processTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            noIntercpet = true;
            downLocation = ev.getY();
            statusChanged=false;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float dis = ev.getY() - downLocation;
            if (dis > minMoveDistance) {
                if(moveType!=MoveType.DOWN){
                    moveType = MoveType.DOWN;
                    statusChanged=true;
                }
//                isMoving = true;
            } else if (dis < -minMoveDistance) {
                if(moveType!=MoveType.TOP){
                    moveType = MoveType.TOP;
                    statusChanged=true;
                }
//                isMoving = true;
            }
            noIntercpet = Math.abs(dis) < minDistance;
        }
        return noIntercpet && super.dispatchTouchEvent(ev);
    }

    private boolean noIntercpet = true;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean b = super.onInterceptTouchEvent(ev);
        return b;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper != null && dragHelper.continueSettling(true)) {
            invalidate();
        }else {
            if(scrollListener!=null){
                scrollListener.finish(statusChanged);
            }
        }
    }

    public void setBottomHideHeight(int bottomHideHeight) {
        this.bottomHideHeight = bottomHideHeight;
        invalidate();
    }

    public int getBottomHideHeight() {
        return bottomHideHeight;
    }

    public void setDragAble(boolean dragAble) {
        this.dragAble = dragAble;
        invalidate();
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public int getTopBottomHideHeight() {
        return topBottomHideHeight;
    }

    public void setScrollListener(OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface OnScrollListener{
        void finish(boolean statusChanged);
    }
}
