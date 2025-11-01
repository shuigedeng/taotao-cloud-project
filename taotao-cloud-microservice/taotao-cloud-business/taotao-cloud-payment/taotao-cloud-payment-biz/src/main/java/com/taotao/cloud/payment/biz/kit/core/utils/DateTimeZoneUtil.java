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

package com.taotao.cloud.payment.biz.kit.core.utils;


import com.xkzhangsan.time.converter.DateTimeConverterUtil;
import com.xkzhangsan.time.formatter.DateTimeFormatterUtil;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

/** 时间工具类 依赖 xk-time */
public class DateTimeZoneUtil implements Serializable {

    private static final long serialVersionUID = -1331008203306650395L;

    /**
     * 时间转 TimeZone
     *
     * <p>2020-08-17T16:46:37+08:00
     *
     * @param time 时间戳
     * @return {@link String} TimeZone 格式时间字符串
     * @throws Exception 异常信息
     */
    public static String dateToTimeZone(long time) throws Exception {
        return dateToTimeZone(new Date(time));
    }

    /**
     * 时间转 TimeZone
     *
     * <p>2020-08-17T16:46:37+08:00
     *
     * @param date {@link Date}
     * @return {@link String} TimeZone 格式时间字符串
     * @throws Exception 异常信息
     */
    public static String dateToTimeZone(Date date) throws Exception {
        String time;
        if (date == null) {
            throw new Exception("date is not null");
        }
        ZonedDateTime zonedDateTime = DateTimeConverterUtil.toZonedDateTime(date);
        time = DateTimeFormatterUtil.format(zonedDateTime, DateTimeFormatterUtil.YYYY_MM_DD_T_HH_MM_SS_XXX_FMT);
        return time;
    }

    /**
     * TimeZone 时间转标准时间
     *
     * <p>2020-08-17T16:46:37+08:00 to 2020-08-17 16:46:37
     *
     * @param str TimeZone格式时间字符串
     * @return {@link String} 标准时间字符串
     * @throws Exception 异常信息
     */
    public static String timeZoneDateToStr(String str) throws Exception {
        String time;
        if (StrUtil.isBlank(str)) {
            throw new Exception("str is not null");
        }
        ZonedDateTime zonedDateTime =
                DateTimeFormatterUtil.parseToZonedDateTime(str, DateTimeFormatterUtil.YYYY_MM_DD_T_HH_MM_SS_XXX_FMT);
        if (zonedDateTime == null) {
            throw new Exception("str to zonedDateTime fail");
        }
        time = zonedDateTime.format(DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT);
        return time;
    }

    public static void main(String[] args) throws Exception {
        String timeZone = dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
        String timeZone2 = dateToTimeZone(new Date());
        LogUtils.info(timeZone + " " + timeZone2);
        String date = timeZoneDateToStr(timeZone);
        LogUtils.info(date);
    }
}
