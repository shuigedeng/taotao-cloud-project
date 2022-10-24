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
package com.taotao.cloud.cache.redis.configuration;

import com.taotao.cloud.cache.redis.redisson.RedisDelayQueue;
import com.taotao.cloud.cache.redis.redisson.RedisDelayQueueRunner;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * RedisDelayQueueAutoConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/01/29 15:57
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(RedissonClient.class)
public class RedisDelayQueueAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(RedisDelayQueueAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	@Bean
	public RedisDelayQueue redisDelayQueue() {
		return new RedisDelayQueue();
	}

	@Bean
	public RedisDelayQueueRunner redisDelayQueueRunner(RedisDelayQueue redisDelayQueue) {
		return new RedisDelayQueueRunner(redisDelayQueue);
	}


}
