package com.qcx.mini.dialog;

import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.widget.calendar.CalendarView;
import com.qcx.mini.widget.calendar.DateEntity;
import com.qcx.mini.widget.wheel.app.HourWheelView;
import com.qcx.mini.widget.wheel.app.MinuteWheelView;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class DriverReleaseTimeDialog extends BaseDialog {
    private CalendarView mCalendarView;
    private HourWheelView mHourWheelView;
    private MinuteWheelView mMinuteWheelView;
    private List<DateEntity> selectedDays;

    private OnDatesSelectedListener listener;

    public DriverReleaseTimeDialog setListener(OnDatesSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    private void init(View view) {
        mCalendarView = view.findViewById(R.id.dialog_driver_release_time_calendar);
        mCalendarView.setMaxChoicePosition(mCalendarView.getTodayPosition() + 6);
        mCalendarView.setMinChoicePosition(mCalendarView.getTodayPosition());
        mCalendarView.setMaxSelectedNum(7);
        mHourWheelView = view.findViewById(R.id.dialog_driver_release_time_hour);
        mMinuteWheelView = view.findViewById(R.id.dialog_driver_release_time_minute);
        mMinuteWheelView.setCyclic(false);
        mHourWheelView.setCyclic(false);
        mMinuteWheelView.setLevel(15);
        mHourWheelView.setMinuteWheelView(mMinuteWheelView);

        if(selectedDays!=null&&selectedDays.size()>0){
            mMinuteWheelView.setCurrentItem(selectedDays.get(0).getMillion());
            mHourWheelView.setCurrentItem(selectedDays.get(0).getMillion());
            mCalendarView.setSelectedDays(selectedDays);
        }else {
            long d=System.currentTimeMillis();
            mMinuteWheelView.setCurrentItem(d);
            mHourWheelView.setCurrentItem(d);
            if(mHourWheelView.isNextDay()){
                d+=DateUtil.DAY;
                mCalendarView.setMaxChoicePosition(mCalendarView.getTodayPosition() + 7);
                mCalendarView.setMinChoicePosition(mCalendarView.getTodayPosition()+1);
            }
            mCalendarView.setSelectedDay(d);
        }
        view.findViewById(R.id.dialog_driver_release_time_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    List<DateEntity> dates = mCalendarView.getSelectedDates();
                    if (dates != null) {
                        int size = dates.size();
                        for (int i = 0; i < size; i++) {
                            long million = dates.get(i).getMillion() + mHourWheelView.getHoursTime() + mMinuteWheelView.getMimuteTime();
                            dates.get(i).setMillion(million);
                            LogUtil.i(DateUtil.getTimeStr(dates.get(i).getMillion(), null));
                        }
                    }
                    listener.onSelected(dates,DriverReleaseTimeDialog.this);
                } else {
                    dismiss();
                }
            }
        });

        view.findViewById(R.id.dialog_driver_release_time_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public DriverReleaseTimeDialog setSelectedDays(List<DateEntity> selectedDays){
        this.selectedDays=selectedDays;
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_driver_release_time;
    }

    @Override
    public void initView(View view) {
        init(view);
    }


    public interface OnDatesSelectedListener {
        void onSelected(List<DateEntity> dates,DriverReleaseTimeDialog dialog);
    }

}
