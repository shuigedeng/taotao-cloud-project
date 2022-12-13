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
package com.taotao.cloud.lock.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.lock.aop.LockAop;
import com.taotao.cloud.lock.properties.LockProperties;
import com.taotao.cloud.lock.support.DistributedLock;
import com.taotao.cloud.lock.support.redis.RedissonDistributedLock;
import com.taotao.cloud.lock.support.zookeeper.ZookeeperDistributedLock;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedisLockAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:17:02
 */
@AutoConfiguration
@EnableConfigurationProperties({LockProperties.class})
@ConditionalOnProperty(prefix = LockProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LockAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(LockAutoConfiguration.class, StarterName.LOCK_STARTER);
	}

	@Configuration
	@ConditionalOnBean(RedissonClient.class)
	@ConditionalOnProperty(prefix = LockProperties.PREFIX, name = "type", havingValue = "redis")
	public static class RedisLockAutoConfiguration implements InitializingBean {

		@Override
		public void afterPropertiesSet() throws Exception {
			LogUtils.started(RedisLockAutoConfiguration.class, StarterName.LOCK_STARTER);
		}

		@Bean
		@ConditionalOnMissingBean
		public DistributedLock redissonDistributedLock(RedissonClient redissonClient) {
			return new RedissonDistributedLock(redissonClient);
		}

	}

	@Configuration
	@ConditionalOnBean(CuratorFramework.class)
	@ConditionalOnProperty(prefix = LockProperties.PREFIX, name = "type", havingValue = "zookeeper")
	public static class ZookeeperLockAutoConfiguration implements InitializingBean {

		@Override
		public void afterPropertiesSet() throws Exception {
			LogUtils.started(ZookeeperLockAutoConfiguration.class, StarterName.LOCK_STARTER);
		}

		@Bean
		@ConditionalOnMissingBean
		public DistributedLock zookeeperDistributedLock(CuratorFramework curatorFramework) {
			return new ZookeeperDistributedLock(curatorFramework);
		}
	}

	@Bean
	public LockAop lockAop() {
		return new LockAop();
	}

}
