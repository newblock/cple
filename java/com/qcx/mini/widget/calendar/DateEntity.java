package com.qcx.mini.widget.calendar;

/**
 * Created by Administrator on 2017/12/28.
 */

public class DateEntity {
    private long million ; //时间戳
    private String weekName ;  //周几
    private int weekNum ;  //一周中第几天，非中式
    private String date ; //日期
    private boolean isToday ;  //是否今天
    private String  day ;  //天
    private String luna ;  //阴历
    private boolean isSelected;//选中状态

    public void setMillion(long million) {
        this.million = million;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setLuna(String luna) {
        this.luna = luna;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getMillion() {
        return million;
    }

    public String getWeekName() {
        return weekName;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public String getDate() {
        return date;
    }

    public boolean isToday() {
        return isToday;
    }

    public String getDay() {
        return day;
    }

    public String getLuna() {
        return luna;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public String toString() {
        return "DateEntity{" +
                "million=" + million +
                ", weekName='" + weekName + '\'' +
                ", weekNum=" + weekNum +
                ", date='" + date + '\'' +
                ", isToday=" + isToday +
                ", day='" + day + '\'' +
                ", luna='" + luna + '\'' +
                ", isSelected='" + isSelected + '\'' +
                '}';
    }
}
