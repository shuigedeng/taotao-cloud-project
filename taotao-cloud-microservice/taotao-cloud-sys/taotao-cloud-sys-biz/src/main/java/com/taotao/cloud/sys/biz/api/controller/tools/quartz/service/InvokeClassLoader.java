package com.taotao.cloud.sys.biz.api.controller.tools.quartz.service;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InvokeClassLoader {
}
