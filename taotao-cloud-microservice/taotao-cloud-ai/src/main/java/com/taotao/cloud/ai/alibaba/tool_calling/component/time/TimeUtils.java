package com.taotao.cloud.ai.alibaba.tool_calling.component.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author yingzi
 * @date 2025/5/21 10:59
 */

public class TimeUtils {

    public static String getTimeByZoneId(String zoneId) {

        // Get the time zone using ZoneId
        ZoneId zid = ZoneId.of(zoneId);

        // Get the current time in this time zone
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zid);

        // Defining a formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        // Format ZonedDateTime as a string
        String formattedDateTime = zonedDateTime.format(formatter);

        return formattedDateTime;
    }
}
