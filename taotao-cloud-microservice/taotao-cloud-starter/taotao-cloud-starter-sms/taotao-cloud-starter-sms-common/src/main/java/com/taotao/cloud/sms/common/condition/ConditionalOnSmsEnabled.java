package com.taotao.cloud.sms.common.condition;

import com.taotao.cloud.sms.common.properties.SmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有条件短信了
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-26 17:49:59
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "enabled", havingValue = "true")
public @interface ConditionalOnSmsEnabled {

}
