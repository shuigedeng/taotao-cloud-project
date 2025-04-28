package com.taotao.cloud.ccsr.server.starter.annotation;

import com.taotao.cloud.ccsr.server.starter.CcsrServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CcsrServerAutoConfiguration.class) // 直接关联配置类
public @interface EnableCcsrServer {
    boolean enable() default true;
}
