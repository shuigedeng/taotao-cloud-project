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

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.cloud.gateway.service.RuleCacheService;
import com.taotao.cloud.gateway.service.impl.RuleCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 规则配置
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-06 17:39:13
 */
@Configuration
public class RuleConfiguration {

    @Bean
    public RuleCacheService ruleCacheService(RedisRepository redisRepository) {
        return new RuleCacheServiceImpl(redisRepository);
    }
}
