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
package com.taotao.cloud.idgenerator.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.idgenerator.properties.IdGeneratorProperties;
import com.taotao.cloud.idgenerator.generator.RedisIdGenerator;
import com.taotao.cloud.idgenerator.generator.RedisLockIdGenerator;
import com.taotao.cloud.idgenerator.generator.ZookeeperIdGenerator;
import com.taotao.cloud.lock.support.DistributedLock;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * IdGeneratorAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:17:02
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = IdGeneratorProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({IdGeneratorProperties.class})
public class IdGeneratorAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(IdGeneratorAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	@ConditionalOnBean({RedisRepository.class})
	@ConditionalOnProperty(prefix = IdGeneratorProperties.PREFIX, name = "type", havingValue = "redis", matchIfMissing = true)
	public RedisIdGenerator idGenerator(RedisRepository redisRepository) {
		return new RedisIdGenerator(redisRepository);
	}

	@Bean
	@ConditionalOnBean({RedisRepository.class, DistributedLock.class})
	@ConditionalOnProperty(prefix = IdGeneratorProperties.PREFIX, name = "type", havingValue = "redis_lock")
	public RedisLockIdGenerator redisLockIdGenerator(RedisRepository redisRepository,
													 DistributedLock distributedLock) {
		return new RedisLockIdGenerator(redisRepository, distributedLock);
	}

	@Bean
	@ConditionalOnBean({CuratorFramework.class})
	@ConditionalOnProperty(prefix = IdGeneratorProperties.PREFIX, name = "type", havingValue = "zookeeper")
	public ZookeeperIdGenerator zookeeperIdGenerator(CuratorFramework curatorFramework) {
		return new ZookeeperIdGenerator(curatorFramework);
	}
}
