package com.taotao.cloud.job.core.worker.annotation;


import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TtcJobHandler {


    /**
     * handler name
     */
    String name();



}
