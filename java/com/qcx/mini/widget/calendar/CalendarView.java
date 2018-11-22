package com.qcx.mini.widget.calendar;

import android.content.Context;
import android.content.Entity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.widget.wheel.app.HourWheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/12/28.
 */

public class CalendarView extends LinearLayout {
    private CalendarHelper helper;
    private CalendarAdapter adapter;

    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_view,this,false);
        GridView mGridView = view.findViewById(R.id.calendar_view_day);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_month = view.findViewById(R.id.calendar_view_month_text);
        addView(view);

        helper=new CalendarHelper();
        String dateStr= new SimpleDateFormat("yyyy-MM", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        adapter=new CalendarAdapter(helper.getMonth(dateStr,null),context);
        mGridView.setAdapter(adapter);
        tv_month.setText(dateStr.substring(5,7).concat("月"));
    }

    public void setSelectedDays(List<DateEntity> selectedDays){
        String dateStr= new SimpleDateFormat("yyyy-MM", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        adapter.setDates(helper.getMonth(dateStr,selectedDays));
    }

    public void setSelectedDay(long date){
        String dateStr= new SimpleDateFormat("yyyy-MM", Locale.CHINA).format(date);
        List<DateEntity> days=new ArrayList<>();
        days.add(helper.getDateEntity(date));

        adapter.setDates(helper.getMonth(dateStr,days));
    }

    public List<DateEntity> getSelectedDates() {
        return adapter.getSelectedDates();
    }

    public int getTodayPosition(){
        return helper.getTodayPosition();
    }

    public void setMaxSelectedNum(int maxSelectedNum) {
        adapter.setMaxSelectedNum( maxSelectedNum);
    }

    public void setMinChoicePosition(long minChoicePosition) {
        adapter.setMinChoicePosition( minChoicePosition);
    }

    public void setMaxChoicePosition(long maxChoicePosition) {
        adapter.setMaxChoicePosition( maxChoicePosition);
    }

    public void setSelectedBackColor(int selectedBackColor) {
        adapter.setSelectedBackColor( selectedBackColor);
    }

    public void setSelectInTextColor(int selectInTextColor) {
        adapter.setSelectInTextColor( selectInTextColor);
    }

    public void setSelectOutTextColor(int selectOutTextColor) {
        adapter.setSelectOutTextColor( selectOutTextColor);
    }

    public void setSelectedTextColor(int selectedTextColor) {
        adapter.setSelectedTextColor( selectedTextColor);
    }

}
