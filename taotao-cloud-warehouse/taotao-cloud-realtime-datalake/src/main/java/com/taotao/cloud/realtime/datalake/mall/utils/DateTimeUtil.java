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

package com.taotao.cloud.realtime.datalake.mall.utils;

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
    public static String toYMDhms(Date date) {
        LocalDateTime localDateTime =
                LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dtf.format(localDateTime);
    }

    /**
     * 将字符串日期转换为时间毫秒数
     * @param dateStr
     * @return
     */
    public static Long toTs(String dateStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dtf);
        long ts = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return ts;
    }
}
