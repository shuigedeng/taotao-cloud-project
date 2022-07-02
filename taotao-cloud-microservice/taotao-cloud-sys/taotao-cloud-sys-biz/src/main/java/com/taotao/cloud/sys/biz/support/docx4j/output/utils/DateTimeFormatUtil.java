package com.taotao.cloud.sys.biz.support.docx4j.output.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间格式化
 */
public interface DateTimeFormatUtil {
    String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    String TIME_FORMAT_PATTERN = "HH:mm:ss";
    DateTimeFormatter DTF_YYYY_MM_DD_HH_MM_SS =
        DateTimeFormatter.ofPattern(String.format("%s %s", DATE_FORMAT_PATTERN, TIME_FORMAT_PATTERN));
    DateTimeFormatter DTF_YYYY_MM_DD = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
    DateTimeFormatter DTF_HH_MM_SS = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);


    /**
     * {@link LocalDateTime}格式化
     * @param localDateTime 日期时间
     * @return 日期时间
     */
    static String format(LocalDateTime localDateTime) {
        return DTF_YYYY_MM_DD_HH_MM_SS.format(localDateTime);
    }

    /**
     * {@link LocalDate}格式化
     * @param localDate 日期
     * @return 日期
     */
    static String format(LocalDate localDate) {
        return DTF_YYYY_MM_DD.format(localDate);
    }

    /**
     * {@link LocalTime}格式化
     * @param localTime 时间
     * @return 时间
     */
    static String format(LocalTime localTime) {
        return DTF_HH_MM_SS.format(localTime);
    }

    /**
     * {@link Date}格式化
     * @param date 日期时间
     * @return 日期时间
     */
    static String format(Date date) {
        return DTF_YYYY_MM_DD_HH_MM_SS.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }
}
