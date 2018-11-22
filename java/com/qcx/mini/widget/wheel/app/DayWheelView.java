package com.qcx.mini.widget.wheel.app;

import android.content.Context;
import android.util.AttributeSet;

import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.widget.wheel.WheelAdapter;
import com.qcx.mini.widget.wheel.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.qcx.mini.utils.DateUtil.DAY;

/**
 * Created by Administrator on 2017/12/28.
 */

public class DayWheelView extends WheelView {
    private int type=1;
    private WAdapter adapter;
    private long maxDate=System.currentTimeMillis()+7*24*60*60*1000;
    private boolean isNextMonth=false;
    private HourWheelView hourWheelView;

    public DayWheelView(Context context) {
        super(context);
        init();
    }

    public DayWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        if(type==0){
            adapter=new WAdapter(getType0Data());
            setAdapter(adapter);
        }
        if(type==1){
            adapter=new WAdapter(getType1Data());
            setAdapter(adapter);
        }
    }

    public void setCurrentItem(long data){
        int position = DateUtil.daysBetween(new Date(System.currentTimeMillis()),new Date(data));
        switch (type){
            case 0:
            case 1:
                if(data<DateUtil.getDayBegin().getTime()){
                    setCurrentItem(0);
                    return;
                }
        }
        if(hourWheelView!=null&&hourWheelView.isNextDay()){
            position++;
        }
        if(position>=getItemsCount()){
            position=0;
            isNextMonth=true;
        }else {
            isNextMonth=false;
        }
        setCurrentItem(position);
    }

    public void setHourWheelView(HourWheelView hourWheelView) {
        this.hourWheelView = hourWheelView;
    }

    public long getDayTime(){
        if(type==0){
            return DateUtil.getDayBegin().getTime()+getCurrentItem()*DateUtil.DAY;
        }
        if (type==1){
            return DateUtil.getDayBegin().getTime()+getCurrentItem()*DateUtil.DAY;
        }
        return 0;
    }

    public String getCurrentItemText(){
        return adapter==null?"":adapter.getItem(getCurrentItem());
    }

    private List<String> getType0Data(){
        List<String> data=new ArrayList<>();
        data.add("今天");
        data.add("明天");
        data.add("后天");
        return data;
    }

    private List<String> getType1Data(){
        List<String> data=new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        while (maxDate>calendar.getTimeInMillis()){
            data.add(formatDay("MM月dd日",calendar.getTimeInMillis()));
            calendar.add(Calendar.DATE,1);
        }

        return data;
    }

    public String formatDay(String pattern,long time){
        long today=DateUtil.getDayBegin().getTime();
        int d=(int)((time-today)/DAY);
        String dayStr="";
        if(d==0){
            dayStr="今天";
        }else if(d==1){
            dayStr="明天";
        }else if(d==2){
            dayStr="后天";
        }else {
            SimpleDateFormat format=new SimpleDateFormat(pattern, Locale.CHINA);
            dayStr=format.format(time).concat(" 周").concat(DateUtil.getWeek(time));
        }
        return dayStr;

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
