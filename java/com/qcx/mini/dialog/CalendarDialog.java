package com.qcx.mini.dialog;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.adapter.ItemCalendarAdapter;
import com.qcx.mini.entity.DateEntity;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class CalendarDialog extends BaseDialog {
    private Calendar calendar;
    private List<Integer> monthPosition=new ArrayList<>();
    private RecyclerView rv_month;
    private ItemCalendarAdapter calendarAdapter;
    private OnDialogClickListener listener;
    private long today;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_calendar;
    }

    @Override
    public void initView(View view) {
        rv_month = view.findViewById(R.id.dialog_calendar_recyclerView);
        view.findViewById(R.id.dialog_calendar_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onTopRightClick(calendarAdapter.getStartTime(),calendarAdapter.getEndTime());
                }
                dismiss();
            }
        });
        view.findViewById(R.id.dialog_calendar_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onTopLeftClick();
                }
                dismiss();
            }
        });

        initCalendar();
        GridLayoutManager manager = new GridLayoutManager(getContext(), 7);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                for(int i=0;i<monthPosition.size();i++){
                    if(position==monthPosition.get(i)){
                        return 7;
                    }
                    if(position==monthPosition.get(i)+1){
                        return calendarAdapter.getItem(position).getWeekNum();
                    }
                }
                return 1;
            }
        });
        rv_month.setLayoutManager(manager);
        calendarAdapter = new ItemCalendarAdapter(getContext());
        calendarAdapter.setMonthPosition(monthPosition);
        calendarAdapter.setToday(today);
        calendarAdapter.setDatas(getData1(calendar, 13));
        rv_month.setAdapter(calendarAdapter);
        calendarAdapter.setListener(new ItemCalendarAdapter.OnDateClickListener() {
            @Override
            public void onClick(DateEntity date) {
                LogUtil.i(DateUtil.getTimeStr(date.getMillion(),null)+"today="+DateUtil.getTimeStr(today,null));
                if(date.getMillion()<today){
                    return;
                }
                long startTime=calendarAdapter.getStartTime();
                long endTime=calendarAdapter.getEndTime();
                if(startTime==0){
                   startTime=date.getMillion();
                }else if(startTime==date.getMillion()){
                    startTime=0;
                }else if(endTime==0){
                    endTime=date.getMillion();
                } else if(endTime==date.getMillion()){
                    endTime=0;
                }else if(startTime>date.getMillion()){
                    startTime=date.getMillion();
                }else {
                    endTime=date.getMillion();
                }
                if(startTime>endTime&&endTime!=0||startTime==0){
                    long d=startTime;
                    startTime=endTime;
                    endTime=d;
                }
                calendarAdapter.setStartTime(startTime);
                calendarAdapter.setEndTime(endTime);
                calendarAdapter.notifyDataSetChanged();
            }
        });
    }

    public CalendarDialog setListener(OnDialogClickListener listener) {
        this.listener = listener;
        return this;
    }

    private void initCalendar(){
        calendar=Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        today=calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    private List<DateEntity> getData1(Calendar calendar,int monthSize) {
        List<DateEntity> days = new ArrayList<>();
        for (int i = 0; i < monthSize; i++) {
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int d = 1; d <= maxDay; d++) {
                DateEntity date = new DateEntity();
                date.setMillion(calendar.getTimeInMillis());
                date.setMonth(calendar.get(Calendar.MONTH) + 1);
                date.setYear(calendar.get(Calendar.YEAR));
                date.setWeekNum(calendar.get(Calendar.DAY_OF_WEEK));
                date.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                if(d==1){
                    days.add(date);
                    monthPosition.add(days.size()-1);
                }
                days.add(date);
                LogUtil.i(DateUtil.getTimeStr(date.getMillion(), null) + " week=" + date.getWeekNum());
                calendar.add(Calendar.DATE, 1);
            }
        }
        return days;
    }

    public interface OnDialogClickListener{
        void onTopLeftClick();
        void onTopRightClick(long startTime,long endTime);

    }
}
