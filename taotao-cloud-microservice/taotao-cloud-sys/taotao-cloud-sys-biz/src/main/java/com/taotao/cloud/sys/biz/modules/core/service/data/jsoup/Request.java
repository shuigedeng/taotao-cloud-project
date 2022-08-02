package com.taotao.cloud.sys.biz.modules.core.service.data.jsoup;

import org.springframework.http.HttpMethod;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Request {
    /**
     * 请求地址,查询参数拼接在后面
     * @return
     */
    String value();

    /**
     * 默认 get 请求
     * @return
     */
    HttpMethod httpMethod() default HttpMethod.GET;
}
