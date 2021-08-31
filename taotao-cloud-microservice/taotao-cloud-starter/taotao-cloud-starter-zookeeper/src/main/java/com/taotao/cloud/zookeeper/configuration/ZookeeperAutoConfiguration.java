package com.taotao.cloud.zookeeper.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.zookeeper.lock.ZookeeperDistributedLock;
import com.taotao.cloud.zookeeper.properties.ZookeeperLockProperties;
import com.taotao.cloud.zookeeper.properties.ZookeeperProperties;
import com.taotao.cloud.zookeeper.template.ZookeeperTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * zookeeper 配置类
 */
@Configuration
public class ZookeeperAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ZookeeperAutoConfiguration.class, StarterName.ZOOKEEPER_STARTER);
	}

	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = ZookeeperProperties.PREFIX, name = "enabled", havingValue = "true")
	public CuratorFramework curatorFramework(ZookeeperProperties property) {
		LogUtil.started(CuratorFramework.class, StarterName.ZOOKEEPER_STARTER);

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
	@ConditionalOnBean({CuratorFramework.class})
	public ZookeeperTemplate zookeeperTemplate(CuratorFramework curatorFramework) {
		LogUtil.started(ZookeeperTemplate.class, StarterName.ZOOKEEPER_STARTER);

		return new ZookeeperTemplate(curatorFramework);
	}

	@Bean(name = "zookeeperDistributedLock")
	@ConditionalOnBean({CuratorFramework.class})
	@ConditionalOnProperty(prefix = ZookeeperLockProperties.PREFIX, name = "enabled", havingValue = "true")
	public ZookeeperDistributedLock zookeeperDistributedLock(CuratorFramework curatorFramework) {
		LogUtil.started(ZookeeperDistributedLock.class, StarterName.ZOOKEEPER_STARTER);

		return new ZookeeperDistributedLock(curatorFramework);
	}
}
