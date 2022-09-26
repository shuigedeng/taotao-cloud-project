package com.taotao.cloud.oss.common.condition;

import com.taotao.cloud.oss.common.propeties.OssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有条件oss启用
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-23 10:48:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "enabled", havingValue = "true")
public @interface ConditionalOnOssEnabled {

}
