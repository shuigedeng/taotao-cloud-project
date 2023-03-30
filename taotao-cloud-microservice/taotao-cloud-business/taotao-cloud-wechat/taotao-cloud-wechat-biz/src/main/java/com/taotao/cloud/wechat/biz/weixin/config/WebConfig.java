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

package com.taotao.cloud.wechat.biz.weixin.config;

import com.joolun.weixin.interceptor.ThirdSessionInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** web配置 */
@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final RedisTemplate redisTemplate;

    /**
     * 拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /** 进入ThirdSession拦截器 */
        registry.addInterceptor(new ThirdSessionInterceptor(redisTemplate))
                .addPathPatterns("/weixin/api/**") // 拦截/api/**接口
                .excludePathPatterns(
                        "/weixin/api/ma/wxuser/login",
                        "/weixin/api/ma/orderinfo/notify-order",
                        "/weixin/api/ma/orderinfo/notify-logisticsr",
                        "/weixin/api/ma/orderrefunds/notify-refunds"); // 放行接口
    }
}
