package com.taotao.cloud.rpc.common.idworker.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {


    public static long midnightMillis() {
        // today
        Calendar date = Calendar.getInstance();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date.getTimeInMillis();
    }

    public static void main(String[] args) {
        // 2013-12-25 00:00:00.000
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Timestamp(midnightMillis())));
    }


}
