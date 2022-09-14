package com.taotao.cloud.sensitive.sensitive.sensitive.core.util.condition;


import com.taotao.cloud.common.utils.lang.ObjectUtils;
import com.taotao.cloud.common.utils.reflect.ClassUtils;
import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.metadata.SensitiveCondition;
import com.taotao.cloud.sensitive.sensitive.sensitive.api.ICondition;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * 脱敏条件工具类
 */
public final class SensitiveConditions {

    private SensitiveConditions(){}

    /**
     * 获取用户自定义条件
     *
     * @param annotations 字段上的注解
     * @return 对应的用户自定义条件
     */
    public static Optional<ICondition> getConditionOpt(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            SensitiveCondition sensitiveCondition = annotation.annotationType().getAnnotation(SensitiveCondition.class);
            if (ObjectUtils.isNotNull(sensitiveCondition)) {
                Class<? extends ICondition> customClass = sensitiveCondition.value();
                ICondition condition =  ClassUtils.newInstance(customClass);
                return Optional.ofNullable(condition);
            }
        }
        return Optional.empty();
    }

}
