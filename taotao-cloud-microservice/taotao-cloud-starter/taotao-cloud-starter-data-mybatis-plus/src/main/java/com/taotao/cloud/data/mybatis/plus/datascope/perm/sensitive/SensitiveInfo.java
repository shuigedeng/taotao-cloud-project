package com.taotao.cloud.data.mybatis.plus.datascope.perm.sensitive;

import cn.bootx.common.core.enums.SensitiveType;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**   
* 敏感信息过滤
* @author xxm  
* @date 2021/10/25 
*/
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface SensitiveInfo {

    /**
     * 敏感信息类型
     */
    SensitiveType value() default SensitiveType.OTHER;

    /**
     * 敏感类型为其他可用
     * front – 保留：前面的front位数；从1开始
     */
    int front() default 4;

    /**
     * 敏感类型为其他可用
     * end – 保留：后面的end位数；从1开始
     */
    int end() default 4;
}
