package com.taotao.cloud.web.sensitive.sensitive;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SensitiveInfo
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-16 14:00:45
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface SensitiveInfo {

	public SensitiveType value();
}
