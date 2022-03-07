package com.taotao.cloud.core.sensitive.sensitive.annotation.custom;


import com.taotao.cloud.core.sensitive.sensitive.annotation.metadata.SensitiveStrategy;
import com.taotao.cloud.core.sensitive.sensitive.core.custom.CustomPasswordStrategy;
import java.lang.annotation.*;

/**
 * 自定义密码脱敏策略
 * @author binbin.hou
 * date 2019/1/17
 * @since 0.0.4
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(CustomPasswordStrategy.class)
public @interface SensitiveCustomPasswordStrategy {
}
