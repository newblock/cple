package com.qcx.mini.widget.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qcx.mini.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class CalendarAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<DateEntity> dates;//数据源
    List<DateEntity> selectedDates;//选中的日期

    private int maxSelectedNum=1;//最大选中天数
    private long minChoicePosition;//选择范围min
    private long maxChoicePosition;//选择范围max

    private int selectedBackColor= R.drawable.bg_calendar_item;//选中背景
    private int selectInTextColor= Color.BLACK;//可选日期的字体颜色
    private int selectOutTextColor=0xFFB9BDC3;//不可选日期的字体颜色
    private int selectedTextColor=Color.WHITE;//选中的字体颜色

    public CalendarAdapter(List<DateEntity> dates, Context context) {
        this.dates = dates;
        if(this.dates==null) this.dates=new ArrayList<>();
        selectedDates=new ArrayList<>();
        minChoicePosition=0;
        maxChoicePosition=this.dates.size();
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    public void setMaxSelectedNum(int maxSelectedNum) {
        this.maxSelectedNum = maxSelectedNum;
        notifyDataSetChanged();
    }

    public void setMinChoicePosition(long minChoicePosition) {
        this.minChoicePosition = minChoicePosition;
        notifyDataSetChanged();
    }

    public void setMaxChoicePosition(long maxChoicePosition) {
        this.maxChoicePosition = maxChoicePosition;
        notifyDataSetChanged();
    }

    public List<DateEntity> getSelectedDates() {
        return selectedDates;
    }

    public void setDates(List<DateEntity> dates){
        if(dates==null) dates=new ArrayList<>();
        this.dates=dates;
        selectedDates.clear();
        for(int i=0;i<dates.size();i++){
            if(dates.get(i).isSelected()){
                selectedDates.add(dates.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void setSelectedBackColor(int selectedBackColor) {
        this.selectedBackColor = selectedBackColor;
        notifyDataSetChanged();

    }

    public void setSelectInTextColor(int selectInTextColor) {
        this.selectInTextColor = selectInTextColor;
        notifyDataSetChanged();
    }

    public void setSelectOutTextColor(int selectOutTextColor) {
        this.selectOutTextColor = selectOutTextColor;
        notifyDataSetChanged();
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public DateEntity getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CalendarViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_date,parent,false);
            holder=new CalendarViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(CalendarViewHolder)convertView.getTag();
        }
        final DateEntity date=getItem(position);
        holder.tv_day.setText(date.getDay());
        if(minChoicePosition<=position&&position<=maxChoicePosition){//可选范围内
            if(date.isToday()){

            }
            if(date.isSelected()){
                holder.tv_day.setTextColor(selectedTextColor);
                holder.tv_day.setBackground(context.getResources().getDrawable(selectedBackColor));
            }else {
                holder.tv_day.setTextColor(selectInTextColor);
                holder.tv_day.setBackgroundColor(Color.TRANSPARENT);
            }

        }else {
            holder.tv_day.setTextColor(selectOutTextColor);
        }

        holder.tv_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minChoicePosition<=position&&position<=maxChoicePosition){
                    if(date.isSelected()){
                        date.setSelected(false);
                        holder.tv_day.setTextColor(selectInTextColor);
                        holder.tv_day.setBackgroundColor(Color.TRANSPARENT);
                        selectedDates.remove(date);
                    }else {
                        date.setSelected(true);
                        holder.tv_day.setTextColor(selectedTextColor);
                        holder.tv_day.setBackground(context.getResources().getDrawable(selectedBackColor));
                        selectedDates.add(date);
                    }
                }
            }
        });
        return convertView;
    }

    class CalendarViewHolder{
        TextView tv_day;
        CalendarViewHolder(View view){
            tv_day=view.findViewById(R.id.item_data);
        }
    }
}
