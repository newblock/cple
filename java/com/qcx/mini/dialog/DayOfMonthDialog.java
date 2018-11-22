package com.qcx.mini.dialog;

import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.wheel.OnItemSelectedListener;
import com.qcx.mini.widget.wheel.app.DayOfMonthWheelView;
import com.qcx.mini.widget.wheel.app.MonthWheelView;

import static android.view.Gravity.BOTTOM;

/**
 * Created by Administrator on 2018/4/14.
 */

public class DayOfMonthDialog extends BaseDialog {
    private DayOfMonthWheelView dayOfMonthWheelView;
    private MonthWheelView monthWheelView;
    private OnDateClickListener listener;
    @Override
    public int getLayoutId() {
        return R.layout.dialog_day_of_month;
    }

    @Override
    public void initView(View view) {
        dayOfMonthWheelView=view.findViewById(R.id.dialog_day_of_month_day);
        dayOfMonthWheelView.setMonth(System.currentTimeMillis());
        dayOfMonthWheelView.setCyclic(false);
        monthWheelView=view.findViewById(R.id.dialog_day_of_month_month);
        monthWheelView.setCyclic(false);
        dayOfMonthWheelView.setCurrentItem(0);
        monthWheelView.setCurrentItem(0);

        monthWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if(index==0){
                    dayOfMonthWheelView.setMonth(System.currentTimeMillis());
                }else {
                    dayOfMonthWheelView.setMonth(monthWheelView.getDate().getDate());
                }
                dayOfMonthWheelView.setCurrentItem(0);
            }
        });
        view.findViewById(R.id.dialog_day_of_month_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onDate(dayOfMonthWheelView.getDate().getDate(),DayOfMonthDialog.this);
                }
            }
        });
        view.findViewById(R.id.dialog_day_of_month_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public DayOfMonthDialog setListener(OnDateClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public int getGravity() {
        return BOTTOM;
    }

    public interface OnDateClickListener{
        void onDate(long date,DayOfMonthDialog dialog);
    }
}
