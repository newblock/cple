package com.qcx.mini.widget.wheel.app;

import android.content.Context;
import android.util.AttributeSet;

import com.qcx.mini.widget.wheel.WheelAdapter;
import com.qcx.mini.widget.wheel.WheelView;
import com.qcx.mini.widget.wheel.app.entity.DateEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class MonthWheelView extends WheelView {
    private WAdapter adapter;
    private List<DateEntity> monthDate;//每月的开始时间

    public MonthWheelView(Context context) {
        super(context);
        initMonth();
    }

    public MonthWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMonth();
    }

    public DateEntity getDate(){
        return monthDate.get(getCurrentItem());
    }


    public void initMonth() {
        monthDate=new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        for(int i=0;i<6;i++){
            DateEntity entity=new DateEntity();
            entity.setDate(calendar.getTimeInMillis());
            entity.setText(calendar.get(Calendar.MONTH)+1+"月");
            monthDate.add(entity);
            calendar.add(Calendar.MONTH, 1);
        }
        adapter=new WAdapter(monthDate);
        setAdapter(adapter);
    }

    class WAdapter implements WheelAdapter {
        private List<DateEntity> data;

        public WAdapter(List<DateEntity> data) {
            if (data == null) data = new ArrayList<>();
            this.data = data;
        }

        public WAdapter() {

        }

        @Override
        public int getItemsCount() {
            return data.size();
        }

        @Override
        public String getItem(int index) {
            return data.get(index).getText();
        }

        @Override
        public int indexOf(Object o) {
            if(o==null) return 0;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getText().equals(o.toString())) {
                    return i;
                }
            }
            return 0;
        }
    }


}
