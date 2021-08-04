package com.taotao.cloud.zookeeper.configuration;

import com.taotao.cloud.zookeeper.lock.ZookeeperDistributedLock;
import com.taotao.cloud.zookeeper.properties.ZookeeperLockProperties;
import com.taotao.cloud.zookeeper.properties.ZookeeperProperties;
import com.taotao.cloud.zookeeper.template.ZookeeperTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * zookeeper 配置类
 */
public class ZookeeperAutoConfiguration {

	/**
	 * 初始化连接
	 */
	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = ZookeeperProperties.PREFIX, name = "enabled", havingValue = "true")
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
	@ConditionalOnBean({CuratorFramework.class})
	public ZookeeperTemplate zookeeperTemplate(CuratorFramework curatorFramework) {
		return new ZookeeperTemplate(curatorFramework);
	}

	@Bean(name = "zookeeperDistributedLock")
	@ConditionalOnBean({CuratorFramework.class})
	@ConditionalOnProperty(prefix = ZookeeperLockProperties.PREFIX, name = "enabled", havingValue = "true")
	public ZookeeperDistributedLock zookeeperDistributedLock(CuratorFramework curatorFramework) {
		return new ZookeeperDistributedLock(curatorFramework);
	}
}
