package com.taotao.cloud.ccsr.client.starter.annotation;

import com.taotao.cloud.ccsr.client.starter.CcsrClientAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CcsrClientAutoConfiguration.class) // 关键点：直接关联配置类
public @interface EnableCcsrClient {
    boolean enable() default true;
}
