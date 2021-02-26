/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.ribbon.annotation;

import com.taotao.cloud.ribbon.fegin.FeignAutoConfiguration;
import com.taotao.cloud.ribbon.fegin.FeignHttpInterceptorConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启feign拦截器传递数据给下游服务，包含基础数据和http的相关数据
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/5 13:40
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableTaoTaoCloudHttpClient
@EnableFeignClients(basePackages = "com.taotao.cloud.*.api.feign")
@EnableAutoConfiguration(excludeName = "org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration")
@Import({FeignAutoConfiguration.class, FeignHttpInterceptorConfig.class})
public @interface EnableTaoTaoCloudFeign {

}
