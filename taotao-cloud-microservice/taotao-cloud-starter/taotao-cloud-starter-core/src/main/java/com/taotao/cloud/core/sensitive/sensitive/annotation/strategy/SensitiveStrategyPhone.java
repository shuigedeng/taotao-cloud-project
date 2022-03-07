package com.taotao.cloud.core.sensitive.sensitive.annotation.strategy;


import com.taotao.cloud.core.sensitive.sensitive.annotation.metadata.SensitiveStrategy;
import com.taotao.cloud.core.sensitive.sensitive.api.impl.SensitiveStrategyBuiltIn;
import java.lang.annotation.*;

/**
 * 手机号脱敏注解
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(SensitiveStrategyBuiltIn.class)
public @interface SensitiveStrategyPhone {
}
