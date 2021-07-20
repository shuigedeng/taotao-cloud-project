package com.taotao.cloud.standalone.common.sensitive;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Classname SensitiveInfoSerialize
 * @Description 脱敏注解类
 * @Author shuigedeng
 * @since 2019/12/9 4:20 下午
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface SensitiveInfo {

    public SensitiveType value();
}
