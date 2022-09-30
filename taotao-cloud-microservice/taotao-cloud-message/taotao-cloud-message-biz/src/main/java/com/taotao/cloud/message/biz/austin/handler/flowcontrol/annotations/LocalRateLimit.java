package com.taotao.cloud.message.biz.austin.handler.flowcontrol.annotations;

import com.taotao.cloud.message.biz.austin.handler.enums.RateLimitStrategy;
import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单机限流注解
 * Created by TOM
 * On 2022/7/21 17:03
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface LocalRateLimit {
	RateLimitStrategy rateLimitStrategy() default RateLimitStrategy.REQUEST_RATE_LIMIT;
}
