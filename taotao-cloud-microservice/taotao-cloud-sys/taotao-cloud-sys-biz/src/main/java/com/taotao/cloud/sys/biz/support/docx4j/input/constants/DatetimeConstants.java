package com.taotao.cloud.sys.biz.support.docx4j.input.constants;

import java.time.format.DateTimeFormatter;

/**
 * 日期时间格式
 */
public interface DatetimeConstants {
    String XLS_MM_DD_YY = "M/d/yy";
    String XLS_YYYY_MM_DD = "yyyy/M/d";
    String XLS_HH_MM_SS = "H:m:s";
    String XLS_YYYY_MM_DD_HH_MM_SS = String.format("%s %s", XLS_YYYY_MM_DD, XLS_HH_MM_SS);
    /**
     * excel日期格式化
     */
    DateTimeFormatter DTF_XLS_YYYY_MM_DD = DateTimeFormatter.ofPattern(XLS_YYYY_MM_DD);
    /**
     * excel时间格式化
     */
    DateTimeFormatter DTF_XLS_HH_MM_SS = DateTimeFormatter.ofPattern(XLS_HH_MM_SS);
    /**
     * excel日期时间格式化
     */
    DateTimeFormatter DTF_XLS_YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern(XLS_YYYY_MM_DD_HH_MM_SS);
}
