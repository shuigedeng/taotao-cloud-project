package com.taotao.cloud.redis.delay.annotation;

import com.taotao.cloud.redis.delay.config.EnableRedissonConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({EnableRedissonConfiguration.class})
public @interface EnableRedisson {

}
