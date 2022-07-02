package com.taotao.cloud.sys.biz.support.docx4j.output.utils;


import com.taotao.cloud.sys.biz.support.docx4j.output.OutputConstants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

/**
 * 字符串对象转换
 */
public interface StringConverterUtil {
    /**
     * 任意对象转字符串
     * @param o 任意类型
     * @return 字符串
     */
    static String convert(Object o) {
        if (Objects.isNull(o)) {
            return OutputConstants.EMPTY;
        }

        if (o instanceof String) {
            return (String) o;
        }

        if (o instanceof LocalDateTime) {
            return DateTimeFormatUtil.format((LocalDateTime) o);
        }

        if (o instanceof LocalDate) {
            return DateTimeFormatUtil.format((LocalDate) o);
        }

        if (o instanceof LocalTime) {
            return DateTimeFormatUtil.format((LocalTime) o);
        }

        if (o instanceof Date) {
            return DateTimeFormatUtil.format((Date) o);
        }

        return o.toString();
    }
}
