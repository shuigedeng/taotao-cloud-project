package com.taotao.cloud.core.sensitive.sensitive.annotation.custom;


import com.taotao.cloud.core.sensitive.sensitive.annotation.metadata.SensitiveCondition;
import com.taotao.cloud.core.sensitive.sensitive.core.condition.ConditionFooPassword;
import java.lang.annotation.*;

/**
 * 自定义密码脱敏策略生效条件
 * @author binbin.hou
 * date 2019/1/17
 * @since 0.0.4
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveCondition(ConditionFooPassword.class)
public @interface SensitiveCustomPasswordCondition{
}
