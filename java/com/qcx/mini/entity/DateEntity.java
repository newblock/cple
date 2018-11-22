package com.qcx.mini.entity;

public class DateEntity {
	private long million ; //时间戳
	private int day ;  //
	private int weekNum ;  //一周中第几天
	private int month;//月份
	private int year;//年份
	private boolean isToday ;  //是否今天
	private boolean isSelected;

	public long getMillion() {
		return million;
	}

	public void setMillion(long million) {
		this.million = million;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeekNum() {
		return weekNum;
	}

	public void setWeekNum(int weekNum) {
		this.weekNum = weekNum;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public boolean isToday() {
		return isToday;
	}

	public void setToday(boolean today) {
		isToday = today;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	@Override
	public String toString() {
		return "DateEntity{" +
				"million=" + million +
				", weekNum=" + weekNum +
				", isToday=" + isToday +
				", monnth=" + month +
				", year=" + year +
				", isSelected='" + isSelected + '\'' +
				'}';
	}
}
