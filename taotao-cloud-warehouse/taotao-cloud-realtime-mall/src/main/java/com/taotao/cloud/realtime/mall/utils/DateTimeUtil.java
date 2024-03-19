package com.taotao.cloud.realtime.mall.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * Date: 2021/2/20
 * Desc:  日期转换的工具类
 *     SimpleDateFormat存在线程安全问题,底层调用 calendar.setTime(date);
 *     解决：在JDK8，提供了DateTimeFormatter替代SimpleDateFormat
 *
 */
public class DateTimeUtil {
    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        System.out.println(ZoneId.systemDefault());
    }
    /**
     * 将Date日期转换为字符串
     * @return
     */
    public static String toYMDhms(Date date){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dtf.format(localDateTime);
    }

    /**
     * 将字符串日期转换为时间毫秒数
     * @param dateStr
     * @return
     */
    public static Long toTs(String dateStr){
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dtf);
        long ts = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return ts;
    }
}
