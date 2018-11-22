package com.qcx.mini.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.DateEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ItemCalendarAdapter extends BaseRecyclerViewAdapter<DateEntity,BaseRecycleViewHolder> {
    private List<Integer> monthPosition;
    private long startTime;
    private long endTime;
    private OnDateClickListener listener;
    private long today;
    private BaseRecycleViewHolder.Params params;

    public ItemCalendarAdapter(Context context) {
        super(context);
        monthPosition=new ArrayList<>();
    }

    public ItemCalendarAdapter(Context context, List<DateEntity> datas) {
        super(context, datas);
        monthPosition=new ArrayList<>();
    }

    public void setListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    public List<Integer> getMonthPosition() {
        return monthPosition;
    }

    public void setMonthPosition(List<Integer> monthPosition) {
        this.monthPosition = monthPosition;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getToday() {
        return today;
    }

    public void setToday(long today) {
        this.today = today;
    }

    @Override
    public int getItemViewType(int position) {
        for(int i=0;i<monthPosition.size();i++){
            if(position==monthPosition.get(i)){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0){
            return new ItemCalendarViewHolder(inflater.inflate(R.layout.item_calendar_date,parent,false));
        }else {
            return new ItemCalendarMonthViewHolder(inflater.inflate(R.layout.item_month_text,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemCalendarViewHolder extends BaseRecycleViewHolder {
        private DateEntity date;
        private TextView tv_day;
        private View v_color,v_color2;//TODO:两多余的，先这样吧
        public ItemCalendarViewHolder(View itemView) {
            super(itemView);
            tv_day=itemView.findViewById(R.id.item_data);
            v_color=itemView.findViewById(R.id.item_data_color);
            v_color2=itemView.findViewById(R.id.item_data_color2);
            tv_day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null&&date!=null){
                        listener.onClick(date);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            date= (DateEntity) data;
            tv_day.setText(String.valueOf(date.getDay()));
            if(date.getMillion()<today){
                tv_day.setTextColor(0xFFB9BDC3);
            }else {
                tv_day.setTextColor(0xFF484848);
            }
            v_color.setBackgroundColor(Color.TRANSPARENT);
            tv_day.setBackgroundColor(Color.WHITE);
            v_color2.setBackgroundColor(Color.TRANSPARENT);

            if(date.getMillion()==today){
                v_color.setBackground(context.getResources().getDrawable(R.drawable.bg_border_gray));
                tv_day.setBackgroundColor(Color.TRANSPARENT);
                v_color2.setBackgroundColor(Color.TRANSPARENT);
            }
            if(endTime==0&&startTime==date.getMillion()){
                v_color.setBackground(context.getResources().getDrawable(R.drawable.bg_calendar_selected));
                tv_day.setBackgroundColor(Color.TRANSPARENT);
                v_color2.setBackgroundColor(Color.TRANSPARENT);
                tv_day.setTextColor(Color.WHITE);
            }else {
                if(startTime==date.getMillion()){
                    v_color2.setBackground(context.getResources().getDrawable(R.drawable.bg_calendar_selected_start));
                    tv_day.setBackgroundColor(Color.TRANSPARENT);
                    tv_day.setTextColor(Color.WHITE);
                }
                if(endTime==date.getMillion()){
                    tv_day.setBackground(context.getResources().getDrawable(R.drawable.bg_calendar_selected_end));
                    tv_day.setTextColor(Color.WHITE);
                }
            }
            if(0<startTime&&startTime<date.getMillion()&&date.getMillion()<endTime){
                tv_day.setBackgroundColor(0xFF499EFF);
                tv_day.setTextColor(Color.WHITE);
            }

            if(0<endTime&&endTime<date.getMillion()&&date.getMillion()<startTime){
                tv_day.setBackgroundColor(0xFF499EFF);
                tv_day.setTextColor(Color.WHITE);
            }
        }
    }

    public class ItemCalendarMonthViewHolder extends BaseRecycleViewHolder {
        private DateEntity date;
        private TextView tv_day;
        public ItemCalendarMonthViewHolder(View itemView) {
            super(itemView);
            tv_day=itemView.findViewById(R.id.item_data);
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            date= (DateEntity) data;
            String monthText=date.getMonth()+"月";
            tv_day.setText(monthText);
        }
    }

    public interface OnDateClickListener{
        void onClick(DateEntity date);
    }
}
