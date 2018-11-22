package com.qcx.mini.widget.wheel.app;

import android.content.Context;
import android.util.AttributeSet;

import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.widget.wheel.WheelAdapter;
import com.qcx.mini.widget.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class HourWheelView extends WheelView {
    private WAdapter adapter;
    private MinuteWheelView minuteWheelView;
    private boolean isNextDay;

    public HourWheelView(Context context) {
        super(context);
        init();
    }

    public HourWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        adapter=new WAdapter(getHours());
        setAdapter(adapter);
//        setCurrentItem(System.currentTimeMillis());
    }

    public long getHoursTime(){
        return getCurrentItem()*60*60*1000;
    }

    public String getCurrentItemText(){
        return adapter==null?"":adapter.getItem(getCurrentItem());
    }

    public void setCurrentItem(long data){
        int day = 24 * 60 * 60 * 1000;
        int hour = 60 * 60 * 1000;
        int position = (int) ((data + 8 * hour) % day / hour);
        if(minuteWheelView!=null&&minuteWheelView.isNextHour()){
            position++;
        }
        if(position>=getItemsCount()){
            position=0;
            isNextDay=true;
        }else {
            isNextDay=false;
        }
        setCurrentItem(position);
    }


    private List<String> getHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i + "点");
            } else {
                hours.add(i + "点");
            }
        }
        return hours;
    }

    public void setMinuteWheelView(MinuteWheelView minuteWheelView) {
        this.minuteWheelView = minuteWheelView;
    }

    public boolean isNextDay() {
        return isNextDay;
    }

    class WAdapter implements WheelAdapter {
        private List<String> data;

        public WAdapter(List<String> data) {
            if(data==null) data=new ArrayList<>();
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
            for(int i=0;i<data.size();i++){
                if(data.get(i).equals(o.toString())){
                    return i;
                }
            }
            return 0;
        }
    }
}
