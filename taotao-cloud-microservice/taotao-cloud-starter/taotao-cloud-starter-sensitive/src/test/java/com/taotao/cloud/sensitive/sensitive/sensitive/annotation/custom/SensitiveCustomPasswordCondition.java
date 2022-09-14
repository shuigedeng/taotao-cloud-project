package com.taotao.cloud.sensitive.sensitive.sensitive.annotation.custom;


import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.metadata.SensitiveCondition;
import com.taotao.cloud.sensitive.sensitive.sensitive.core.condition.ConditionFooPassword;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义密码脱敏策略生效条件
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveCondition(ConditionFooPassword.class)
public @interface SensitiveCustomPasswordCondition{
}
