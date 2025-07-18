package com.taotao.cloud.cache.other;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomProcess {
    // 可以添加注解参数
    String processor() default "default";
}
