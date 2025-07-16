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

package com.taotao.cloud.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 跨域配置
 *
 * 如果部署是nginx + springcloud gateway 那么在nginx部署跨域配置
 * 如果部署是 springcloud gateway 那么需要打开此文件部署跨域配置
 *
 * 上面两者的各个微服务都不再需要配置跨域 如配置之后 会形成两层跨域甚至三层跨域
 *
 * @author shuigedeng
 * @since 2020/4/29 22:11
 * @version 2022.03
 */
@Configuration
public class CorsConfiguration {

    @Bean
    @Profile({"dev"})
    public CorsWebFilter devCorsFilter() {
        org.springframework.web.cors.CorsConfiguration config =
                new org.springframework.web.cors.CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.setMaxAge(18000L);
        config.addAllowedMethod("*");
        // 配置前端js允许访问的自定义响应头
        config.addExposedHeader("setToken");
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    @Bean
    @Profile({"test"})
    public CorsWebFilter testCorsFilter() {
        org.springframework.web.cors.CorsConfiguration config =
                new org.springframework.web.cors.CorsConfiguration();
        // 是否允许携带cookie跨域
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("https://*.test.taotaocloud.top");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(18000L);
        // 配置前端js允许访问的自定义响应头
        // config.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
