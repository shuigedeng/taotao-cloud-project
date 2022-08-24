/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.crypto.ext.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * <p>Description: 加密解密标记注解 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/4 11:48
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface Crypto {

    /**
     * 请求参数记否解密，默认值 true
     *
     * @return true 请求参数解密；false 请求参数不解密
     */
    boolean requestDecrypt() default true;

    /**
     * 响应体是否加密，默认值 true
     *
     * @return true 响应体加密；false 响应体不加密
     */
    boolean responseEncrypt() default true;
}
