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

package com.taotao.cloud.feign.annotation;

import com.taotao.cloud.feign.properties.FeignProperties;
import feign.okhttp.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * <p>Description: Feign 使用 OkHttp 客户端 条件注解 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-02 09:30:19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnClass(OkHttpClient.class)
@ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
@ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "okhttp", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnFeignUseOkHttp {
}
