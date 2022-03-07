package com.taotao.cloud.core.sensitive.sensitive.core.util.entry;


import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.sensitive.sensitive.annotation.SensitiveEntry;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 系统中内置的明细映射
 *
 */
public final class SensitiveEntryUtil {

    private SensitiveEntryUtil() {
    }

    /**
     * 是否有脱敏明细注解信息
     *
     * @param field 字段上的注解
     * @return 是否
     * @author dev-sxl
     */
    public static boolean hasSensitiveEntry(Field field) {
        SensitiveEntry sensitiveEntry = field.getAnnotation(SensitiveEntry.class);
        if (ObjectUtil.isNotNull(sensitiveEntry)) {
            return true;
        }

        for (Annotation annotation : field.getAnnotations()) {
            sensitiveEntry = annotation.annotationType().getAnnotation(SensitiveEntry.class);
            if (ObjectUtil.isNotNull(sensitiveEntry)) {
                return true;
            }
        }
        return false;
    }

}
