package com.qcx.mini.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.qcx.mini.utils.LogUtil;

import java.lang.reflect.Field;

/**
 * 解决ViewPager 重新attachedToWindow翻页动画失效
 * Created by Administrator on 2018/1/5.
 */

public class MyViewPager extends ViewPager {

    public MyViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.set(this, false);
            getAdapter().notifyDataSetChanged();
            setCurrentItem(getCurrentItem());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean post(Runnable action) {
        return super.post(action);
    }
}
