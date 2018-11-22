package com.qcx.mini.widget.wheel.app;

import android.content.Context;
import android.util.AttributeSet;

import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.widget.wheel.WheelAdapter;
import com.qcx.mini.widget.wheel.WheelView;
import com.qcx.mini.widget.wheel.app.entity.DateEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class DayOfMonthWheelView extends WheelView {
    private long month;//生成当月的日期的时间戳
    private WAdapter adapter;
    private List<DateEntity> dates;

    public DayOfMonthWheelView(Context context) {
        super(context);
        dates=new ArrayList<>();
    }

    public DayOfMonthWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dates=new ArrayList<>();
    }

    public void setMonth(long month) {
        this.month = month;
        dates=getMonthDay(month);
        adapter=new WAdapter(dates);
        setAdapter(adapter);
    }

    public DateEntity getDate(){
        return dates.get(getCurrentItem());
    }

    public ArrayList<DateEntity> getMonthDay(long date) {
        ArrayList<DateEntity> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= max; i++) {
            if(month-DateUtil.DAY<cal.getTimeInMillis()){
                DateEntity entity = new DateEntity();
                entity.setText(i+"日");
                entity.setDate(cal.getTimeInMillis());
                result.add(entity);
            }
            cal.add(Calendar.DATE, 1);
        }
        return result;
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


//    private int getDaysOfMonth(long date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date(date));
//        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        return days;
//    }
}
