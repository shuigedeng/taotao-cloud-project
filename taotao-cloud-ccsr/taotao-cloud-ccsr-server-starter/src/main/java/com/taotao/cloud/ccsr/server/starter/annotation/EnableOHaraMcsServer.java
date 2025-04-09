package com.taotao.cloud.ccsr.server.starter.annotation;

import com.taotao.cloud.ccsr.server.OHaraMcsServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OHaraMcsServerAutoConfiguration.class) // 直接关联配置类
public @interface EnableOHaraMcsServer {
    boolean enable() default true;
}
