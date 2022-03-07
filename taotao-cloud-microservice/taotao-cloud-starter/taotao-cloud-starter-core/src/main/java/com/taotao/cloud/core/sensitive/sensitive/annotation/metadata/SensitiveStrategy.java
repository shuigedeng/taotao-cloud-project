package com.taotao.cloud.core.sensitive.sensitive.annotation.metadata;


import com.taotao.cloud.core.sensitive.sensitive.api.IStrategy;
import java.lang.annotation.*;

/**
 * 用于自定义 sensitive 脱敏策略注解
 * 1. 自定义的策略默认生效。
 * 2. 如果有多个 condition, 则优先执行一次满足条件的策略。
 *
 */
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveStrategy {

    /**
     * 自定义脱敏的策略实现
     * @return 策略实现类信息
     */
    Class<? extends IStrategy> value();

}
