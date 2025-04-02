package com.taotao.cloud.job.worker.annotation;


import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface KJobHandler {


    /**
     * handler name
     */
    String name();



}
