package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.dexfun.layout.DexLinearLayout;

/**
 * Created by Administrator on 2018/4/4.
 */

public class GestureLinearLayout extends DexLinearLayout {
    private OnGestureListener listener;

    public void setListener(OnGestureListener listener) {
        this.listener = listener;
    }

    public GestureLinearLayout(Context context) {
        super(context);
    }

    public GestureLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(listener!=null){
            listener.event(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface OnGestureListener{
        void event(MotionEvent ev);
    }
}
