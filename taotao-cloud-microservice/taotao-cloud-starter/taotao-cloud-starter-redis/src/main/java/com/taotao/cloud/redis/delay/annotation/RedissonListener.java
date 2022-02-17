package com.taotao.cloud.redis.delay.annotation;

import java.lang.annotation.*;


@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonListener {

    String[] queues();

    String errorHandler() default "";

    String isolationStrategy() default "";

    String messageConverter() default "";

    /**
     * concurrency of the consumer num
     *
     * @return listener count
     */
    int concurrency() default 1;

    /**
     * poll a list data from redis each time if grater than 1
     *
     * @return max fetch count
     */
    int maxFetch() default 1;

}
