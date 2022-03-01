package com.taotao.cloud.core.cron.pojo;


import com.taotao.cloud.core.cron.util.DateUtil;

/**
 * 保存时分秒
 */
public final class TimeOfDay implements Comparable<TimeOfDay> {
    private int hour;
    private int minute;
    private int second;

    public TimeOfDay(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "TimeOfDay{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
	        return true;
        }
        if (o == null || getClass() != o.getClass()) {
	        return false;
        }

        TimeOfDay ofDay = (TimeOfDay) o;

        if (hour != ofDay.hour) {
	        return false;
        }
        if (minute != ofDay.minute) {
	        return false;
        }
        return second == ofDay.second;
    }

    @Override
    public int hashCode() {
        int result = hour;
        result = 31 * result + minute;
        result = 31 * result + second;
        return result;
    }

    /**
     * 按照时分秒的顺序逐个比较
     */
    @Override
    public int compareTo(TimeOfDay o) {
        if (this.getHour() > o.getHour()) {
            return 1;
        }
        if (this.getHour() < o.getHour()) {
            return -1;
        }
        if (this.getMinute() > o.getMinute()) {
            return 1;
        }
        if (this.getMinute() < o.getMinute()) {
            return -1;
        }
        if (this.getSecond() > o.getSecond()) {
            return 1;
        }
        if (this.getSecond() < o.getSecond()) {
            return -1;
        }
        return 0;
    }

    public boolean equalsWithTolerance(TimeOfDay another , int seconds){
        return DateUtil.equalsWithTolerance(this , another, seconds);
    }
}
