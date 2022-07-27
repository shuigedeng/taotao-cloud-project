package com.taotao.cloud.common.support.cron.pojo;

import java.util.Calendar;

/**
 * 保存日月年
 */
public final class DayOfYear implements Comparable<DayOfYear> {
    private int day;
    private int month;
    private int year;

    public DayOfYear(int day, int month , int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
	        return true;
        }
        if (o == null || getClass() != o.getClass()) {
	        return false;
        }

        DayOfYear dayOfYear = (DayOfYear) o;

        if (day != dayOfYear.day) {
	        return false;
        }
        if (month != dayOfYear.month) {
	        return false;
        }
        return year == dayOfYear.year;
    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + month;
        result = 31 * result + year;
        return result;
    }

    /**
     * 计算星期
     */
    public int week(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR , getYear());
        calendar.set(Calendar.DAY_OF_MONTH , getDay());
        calendar.set(Calendar.MONTH , getMonth() - 1);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }


    /**
     * 按照年月日的顺序逐个比较
     */
    @Override
    public int compareTo(DayOfYear o) {
        if (this.getYear() > o.getYear()) {
            return 1;
        }
        if (this.getYear() < o.getYear()) {
            return -1;
        }
        if (this.getMonth() > o.getMonth()) {
            return 1;
        }
        if (this.getMonth() < o.getMonth()) {
            return -1;
        }
        if (this.getDay() > o.getDay()) {
            return 1;
        }
        if (this.getDay() < o.getDay()) {
            return -1;
        }
        return 0;
    }
}
