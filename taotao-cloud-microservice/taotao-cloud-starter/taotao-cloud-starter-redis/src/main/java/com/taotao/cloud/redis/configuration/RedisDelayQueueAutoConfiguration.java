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
package com.taotao.cloud.redis.configuration;

import com.taotao.cloud.redis.redisson.RedisDelayQueue;
import com.taotao.cloud.redis.redisson.RedisDelayQueueRunner;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedisDelayQueueAutoConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2022/01/29 15:57
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@ConditionalOnBean(RedissonClient.class)
public class RedisDelayQueueAutoConfiguration {

	@Bean
	public RedisDelayQueue redisDelayQueue() {
		return new RedisDelayQueue();
	}

	@Bean
	public RedisDelayQueueRunner redisDelayQueueRunner(RedisDelayQueue redisDelayQueue) {
		return new RedisDelayQueueRunner(redisDelayQueue);
	}


}
