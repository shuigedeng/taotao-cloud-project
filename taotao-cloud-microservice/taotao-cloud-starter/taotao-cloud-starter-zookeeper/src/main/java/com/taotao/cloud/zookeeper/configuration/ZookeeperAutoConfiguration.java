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
package com.taotao.cloud.zookeeper.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.support.lock.DistributedLock;
import com.taotao.cloud.zookeeper.lock.ZookeeperDistributedLock;
import com.taotao.cloud.zookeeper.model.ZkIdGenerator;
import com.taotao.cloud.zookeeper.properties.ZookeeperLockProperties;
import com.taotao.cloud.zookeeper.properties.ZookeeperProperties;
import com.taotao.cloud.zookeeper.template.ZookeeperTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ZookeeperAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:37:49
 */
@Configuration
@EnableConfigurationProperties({ZookeeperProperties.class, ZookeeperLockProperties.class})
@ConditionalOnProperty(prefix = ZookeeperProperties.PREFIX, name = "enabled", havingValue = "true")
public class ZookeeperAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ZookeeperAutoConfiguration.class, StarterName.ZOOKEEPER_STARTER);
	}

	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnMissingBean
	public CuratorFramework curatorFramework(ZookeeperProperties property) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(property.getBaseSleepTime(),
			property.getMaxRetries());

		return CuratorFrameworkFactory.builder()
			.connectString(property.getConnectString())
			.connectionTimeoutMs(property.getConnectionTimeout())
			.sessionTimeoutMs(property.getSessionTimeout())
			.retryPolicy(retryPolicy)
			.build();
	}

	@Bean
	public ZookeeperTemplate zookeeperTemplate(CuratorFramework curatorFramework) {
		return new ZookeeperTemplate(curatorFramework);
	}

	@Bean
	public ZkIdGenerator zkIdGenerator(CuratorFramework curatorFramework) {
		return new ZkIdGenerator(curatorFramework);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = ZookeeperLockProperties.PREFIX, name = "enabled", havingValue = "true")
	public DistributedLock zookeeperDistributedLock(CuratorFramework curatorFramework) {
		return new ZookeeperDistributedLock(curatorFramework);
	}

}
