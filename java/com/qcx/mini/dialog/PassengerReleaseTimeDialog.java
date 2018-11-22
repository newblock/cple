package com.qcx.mini.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.qcx.mini.R;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.widget.wheel.app.DayWheelView;
import com.qcx.mini.widget.wheel.app.HourWheelView;
import com.qcx.mini.widget.wheel.app.MinuteWheelView;

/**
 * Created by Administrator on 2017/12/26.
 */

public class PassengerReleaseTimeDialog extends DialogFragment {
    private DayWheelView mDayPicker;
    private HourWheelView mHourPicker;
    private MinuteWheelView mMinutePicker;
    private OnTimeSelectLisenter lisenter;
    private long selectTime;
    private boolean isShowDay = true;

    public PassengerReleaseTimeDialog setLisenter(OnTimeSelectLisenter lisenter) {
        this.lisenter = lisenter;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_passenger_release_time, null);
        init(view);

        final Dialog dialog = new Dialog(getActivity(), R.style.style_bottom_dialog);
        dialog.setContentView(view);
        dialog.show();

        Window window = dialog.getWindow();
        if(window!=null){
            window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
        return dialog;
    }

    private void init(View view) {
        mDayPicker = view.findViewById(R.id.dialog_passenger_release_time_day);
        mDayPicker.setCyclic(false);
        mHourPicker = view.findViewById(R.id.dialog_passenger_release_time_hour);
        mHourPicker.setCyclic(false);
        mMinutePicker = view.findViewById(R.id.dialog_passenger_release_time_minute);
        mMinutePicker.setCyclic(false);
        mHourPicker.setMinuteWheelView(mMinutePicker);
        mDayPicker.setHourWheelView(mHourPicker);

        mMinutePicker.setLevel(15);
        mMinutePicker.setCurrentItem(selectTime);
        mHourPicker.setCurrentItem(selectTime);
        mDayPicker.setCurrentItem(selectTime);
        if (isShowDay) {
            mDayPicker.setVisibility(View.VISIBLE);
        } else {
            mDayPicker.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHourPicker.getLayoutParams();
            params.weight = 2;
            mDayPicker.setLayoutParams(params);
        }

        view.findViewById(R.id.dialog_passenger_release_time_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.dialog_passenger_release_time_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lisenter != null) {
                    long date;
                    String text;
                    if (isShowDay) {
                        date = mDayPicker.getDayTime() + mHourPicker.getHoursTime() + mMinutePicker.getMimuteTime();
                        text = String.format("%s%s%s", mDayPicker.getCurrentItemText(), mHourPicker.getCurrentItemText(), mMinutePicker.getCurrentItemText());
                    } else {
                        date = mHourPicker.getHoursTime() + mMinutePicker.getMimuteTime() - 8 * DateUtil.HOUR;
                        text = String.format("%s%s", mHourPicker.getCurrentItemText(), mMinutePicker.getCurrentItemText());
                    }
                    selectTime = date;
                    lisenter.date(date, PassengerReleaseTimeDialog.this, text);
                }
            }
        });
    }

    private long getTime() {
        return mDayPicker.getDayTime() + mHourPicker.getHoursTime() + mMinutePicker.getMimuteTime();
    }

    public PassengerReleaseTimeDialog setSelectTime(long time) {
        this.selectTime = time;
        return this;
    }

    public interface OnTimeSelectLisenter {
        void date(long date, PassengerReleaseTimeDialog dialog, String text);
    }

    public PassengerReleaseTimeDialog setShowDay(boolean showDay) {
        isShowDay = showDay;
        return this;
    }
}
