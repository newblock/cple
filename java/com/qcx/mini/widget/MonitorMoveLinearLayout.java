package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.dexfun.layout.DexLinearLayout;
import com.qcx.mini.listener.OnMoveListener;
import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/5/29.
 */

public class MonitorMoveLinearLayout extends DexLinearLayout {
    private float lastX;
    private float lastY;
    private OnMoveListener listener;

    public MonitorMoveLinearLayout(Context context) {
        super(context);
    }

    public MonitorMoveLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MonitorMoveLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(OnMoveListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float moveX;
        float moveY;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getRawX();
                lastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(listener!=null){
                    moveX=ev.getRawX()-lastX;
                    moveY=ev.getRawY()-lastY;
                    listener.onMove(moveX,moveY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(listener!=null){
                    moveX=ev.getRawX()-lastX;
                    moveY=ev.getRawY()-lastY;
                    listener.onUp(moveX,moveY);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
