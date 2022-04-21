package com.taotao.cloud.core.sensitive.sensitive.annotation.custom;


import com.taotao.cloud.core.sensitive.sensitive.annotation.metadata.SensitiveCondition;
import com.taotao.cloud.core.sensitive.sensitive.core.condition.ConditionFooPassword;
import java.lang.annotation.*;

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
