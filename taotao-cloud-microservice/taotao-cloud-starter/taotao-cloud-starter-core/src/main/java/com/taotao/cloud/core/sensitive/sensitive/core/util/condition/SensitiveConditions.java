package com.taotao.cloud.core.sensitive.sensitive.core.util.condition;


import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.heaven.util.lang.reflect.ClassUtil;
import com.taotao.cloud.core.heaven.util.util.Optional;
import com.taotao.cloud.core.sensitive.sensitive.annotation.metadata.SensitiveCondition;
import com.taotao.cloud.core.sensitive.sensitive.api.ICondition;
import java.lang.annotation.Annotation;

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
     * @since 0.0.6
     */
    public static Optional<ICondition> getConditionOpt(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            SensitiveCondition sensitiveCondition = annotation.annotationType().getAnnotation(SensitiveCondition.class);
            if (ObjectUtil.isNotNull(sensitiveCondition)) {
                Class<? extends ICondition> customClass = sensitiveCondition.value();
                ICondition condition =  ClassUtil.newInstance(customClass);
                return Optional.ofNullable(condition);
            }
        }
        return Optional.empty();
    }

}
