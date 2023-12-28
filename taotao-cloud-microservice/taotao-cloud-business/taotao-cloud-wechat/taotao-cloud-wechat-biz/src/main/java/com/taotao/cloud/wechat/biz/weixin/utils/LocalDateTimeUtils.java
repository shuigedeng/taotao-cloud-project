/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.wechat.biz.weixin.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/** LocalDateTime时间工具 */
public class LocalDateTimeUtils {

    public static final String YYYY = "yyyy";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String BASE_TIME_FORMAT =
            "[yyyyMMddHHmmss][yyyyMMddHHmm][yyyyMMddHH][yyyyMMdd][yyyyMM][yyyy][[-][/][.]MM][[-][/][.]dd]["
                    + " ][HH][[:][.]mm][[:][.]ss][[:][.]SSS]";
    /**
     * 【推荐】解析常用时间字符串，支持,并不局限于以下形式： [yyyy][yyyy-MM][yyyy-MM-dd][yyyy-MM-dd HH][yyyy-MM-dd
     * HH:mm][yyyy-MM-dd HH:mm:ss][yyyy-MM-dd HH:mm:ss:SSS] [yyyy][yyyy/MM][yyyy/MM/dd][yyyy/MM/dd
     * HH][yyyy/MM/dd HH:mm][yyyy/MM/dd HH:mm:ss][yyyy/MM/dd HH:mm:ss:SSS]
     * [yyyy][yyyy.MM][yyyy.MM.dd][yyyy.MM.dd HH][yyyy.MM.dd HH.mm][yyyy.MM.dd HH.mm.ss][yyyy.MM.dd
     * HH.mm.ss.SSS] [yyyy][yyyyMM][yyyyMMdd][yyyyMMddHH][yyyyMMddHHmm][yyyyMMddHHmmss] [MM-dd]
     * 不支持yyyyMMddHHmmssSSS，因为本身DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")就不支持这个形式
     *
     * @param timeString
     * @return
     */
    public static LocalDateTime parse(String timeString) {
        return LocalDateTime.parse(timeString, getDateTimeFormatterByPattern(BASE_TIME_FORMAT));
    }

    /**
     * 根据传进来的pattern返回LocalDateTime，自动补齐
     *
     * @param timeString
     * @param pattern
     * @return
     */
    public static LocalDateTime parseByPattern(String timeString, String pattern) {
        return LocalDateTime.parse(timeString, getDateTimeFormatterByPattern(pattern));
    }

    private static DateTimeFormatter getDateTimeFormatterByPattern(String pattern) {
        return new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .parseDefaulting(ChronoField.YEAR_OF_ERA, LocalDateTime.now().getYear())
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, LocalDateTime.now().getMonthValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                .toFormatter();
    }

    /**
     * 将timestamp转为LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime timestamToDatetime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 将LocalDataTime转为timestamp
     *
     * @param ldt
     * @return
     */
    public static long datatimeToTimestamp(LocalDateTime ldt) {
        ZoneId zone = ZoneId.systemDefault();
        return ldt.atZone(zone).toInstant().toEpochMilli();
    }

    public static void main(String[] args) {
        long timeStamp = 1382694957000l;
        LogUtils.info(timestamToDatetime(timeStamp));
    }
}
