package com.qcx.mini.widget.calendar;

import android.support.annotation.Nullable;

import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/12/28.
 */

public class CalendarHelper {
    private int todayPosition = -1;
    private int offNum = 0;//1号前空白ITEM个数

    /**
     * 获取当前日期一月的日期
     *
     * @param date
     * @return
     */
    public ArrayList<DateEntity> getMonth(String date, @Nullable List<DateEntity> selectedDays) {
        ArrayList<DateEntity> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("yyyy-MM", Locale.CHINA).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String day = getValue(DateUtil.getCurrentDay());
        for (int i = 0; i < max; i++) {
            DateEntity entity = new DateEntity();
            entity.setDate(getValue(cal.get(cal.YEAR)) + "-" + getValue(cal.get(cal.MONTH) + 1) + "-" + getValue(cal.get(cal.DATE)));
            entity.setMillion(cal.getTimeInMillis());
            entity.setWeekNum(cal.get(Calendar.DAY_OF_WEEK));
            entity.setDay(getValue(cal.get(cal.DATE)));

            if (day.equals(entity.getDay())) {
                todayPosition = i;
                entity.setToday(true);
            } else {
                entity.setToday(false);
            }
            cal.add(Calendar.DATE, 1);
            if (selectedDays != null && selectedDays.size() > 0) {
                for (int k = 0; k < selectedDays.size(); k++) {
                    LogUtil.i("day1="+entity.getDate());
                    if (entity.getDate().equals(selectedDays.get(k).getDate())) {
                        entity.setSelected(true);
                    }
                }
            }
            result.add(entity);
        }
        int d = max - DateUtil.getCurrentDay();
        if (d < 6) {
            for (int i = 6; i > d; i--) {
                DateEntity entity = new DateEntity();
                entity.setDate(getValue(cal.get(cal.YEAR)) + "-" + getValue(cal.get(cal.MONTH) + 1) + "-" + getValue(cal.get(cal.DATE)));
                entity.setMillion(cal.getTimeInMillis());
                entity.setWeekNum(cal.get(Calendar.DAY_OF_WEEK));
                entity.setDay(getValue(cal.get(cal.DATE)));
                entity.setToday(false);
                cal.add(Calendar.DATE, 1);
                if (selectedDays != null && selectedDays.size() > 0) {
                    for (int k = 0; k < selectedDays.size(); k++) {
                        if (entity.getDate().equals(selectedDays.get(k).getDate())) {

                            entity.setSelected(true);
                        }
                    }
                }
                result.add(entity);
            }
        }
        //为了用空的值填补第一个之前的日期
        //先获取在本周内是周几
        int weekNum = result.get(0).getWeekNum() - 1;
        for (int j = 0; j < weekNum; j++) {
            DateEntity entity = new DateEntity();
            result.add(0, entity);
            todayPosition++;
            offNum++;
        }
        return result;

    }

    public DateEntity getDateEntity(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date));
        DateEntity entity = new DateEntity();
        entity.setDate(getValue(cal.get(cal.YEAR)) + "-" + getValue(cal.get(cal.MONTH) + 1) + "-" + getValue(cal.get(cal.DATE)));
        entity.setMillion(cal.getTimeInMillis());
        entity.setWeekNum(cal.get(Calendar.DAY_OF_WEEK));
        entity.setDay(getValue(cal.get(cal.DATE)));
        entity.setToday(false);
        return entity;
    }

    public int getTodayPosition() {
        return todayPosition;
    }

    public int getOffNum() {
        return offNum;
    }

    /**
     * 个位数补0操作
     *
     * @param num
     * @return
     */
    public static String getValue(int num) {
        return String.valueOf(num > 9 ? num : ("0" + num));
    }
}
