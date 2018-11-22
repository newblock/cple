package com.qcx.mini.widget.wheel.app;

import android.content.Context;
import android.util.AttributeSet;

import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.widget.wheel.WheelAdapter;
import com.qcx.mini.widget.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class MinuteWheelView extends WheelView {
    private int level = 10;
    private WAdapter adapter;
    private boolean isNextHour=false;

    public MinuteWheelView(Context context) {
        super(context);
    }

    public MinuteWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        adapter = new WAdapter(getMinute());
        setAdapter(adapter);
        setCurrentItem(System.currentTimeMillis());
    }

    public void setLevel(int level) {
        this.level = level;
        adapter=new WAdapter(getMinute());
        setAdapter(adapter);
    }

    public void setCurrentItem(long data) {
        int hour = 60 * 60 * 1000;
        int minute = 60 * 1000;
        int position = ((int) ((data % hour) / (minute))) / level;
        if (data % minute > 0 || ((data % hour) / (minute)) % level > 0) {
            position++;
        }
        if(position>=getItemsCount()){
            position=0;
            isNextHour=true;
        }else {
            isNextHour=false;
        }
        setCurrentItem(position);
    }

    public long getMimuteTime() {
        return getCurrentItem() * level * 60 * 1000;
    }

    public String getCurrentItemText() {
        return adapter == null ? "" : adapter.getItem(getCurrentItem());
    }


    private List<String> getMinute() {
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i += level) {
            if (i < 10) {
                minutes.add("0" + i + "分");
            } else {
                minutes.add(i + "分");
            }
        }
        return minutes;
    }

    public boolean isNextHour() {
        return isNextHour;
    }

    class WAdapter implements WheelAdapter {
        private List<String> data;

        public WAdapter(List<String> data) {
            if (data == null) data = new ArrayList<>();
            this.data = data;
        }

        @Override
        public int getItemsCount() {
            return data.size();
        }

        @Override
        public String getItem(int index) {
            return data.get(index);
        }

        @Override
        public int indexOf(Object o) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).equals(o.toString())) {
                    return i;
                }
            }
            return 0;
        }
    }
}
