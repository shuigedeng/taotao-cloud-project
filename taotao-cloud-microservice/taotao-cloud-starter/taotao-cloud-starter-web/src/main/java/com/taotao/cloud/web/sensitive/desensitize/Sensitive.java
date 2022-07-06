package com.taotao.cloud.web.sensitive.desensitize;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.taotao.cloud.web.enums.SensitiveStrategy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 敏感注解
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-06 14:37:57
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Sensitive {

	/**
	 * 策略
	 *
	 * @return {@link SensitiveStrategy }
	 * @since 2022-07-06 14:37:59
	 */
	SensitiveStrategy strategy();
}
