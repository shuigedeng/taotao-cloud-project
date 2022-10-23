package com.taotao.cloud.lock.kylin.spring.boot.autoconfigure.zookeeper;

import com.taotao.cloud.lock.kylin.condition.ZookeeperCondition;
import com.taotao.cloud.lock.kylin.executor.zookeeper.ZookeeperLockExecutor;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.Order;

/**
 * Zookeeper锁自动配置器
 *
 * @author wangjinkui
 */
@Conditional(ZookeeperCondition.class)
@EnableConfigurationProperties(ZookeeperLockProperties.class)
public class ZookeeperLockAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(
		ZookeeperLockAutoConfiguration.class);

	@Autowired
	private ZookeeperLockProperties properties;

	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnMissingBean(CuratorFramework.class)
	public CuratorFramework curatorFramework() {
		LOGGER.debug("kylin-lock init zookeeper properties:{}", properties);

		/**
		 * ExponentialBackoffRetry的重试策略
		 * 给定一个初始sleep时间baseSleepTimeMs，在这个基础上结合重试次数，通过以下公式计算出当前需要sleep的时间：
		 * 当前sleep时间 = baseSleepTimeMs * Math.max(1, random.nextInt(1 << (retryCount + 1)))
		 */
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(),
			properties.getMaxRetries());

		return CuratorFrameworkFactory.builder()
			.connectString(properties.getZkServers())
			.sessionTimeoutMs(properties.getSessionTimeout())
			.connectionTimeoutMs(properties.getConnectionTimeout())
			.namespace(properties.getNamespace())
			.retryPolicy(retryPolicy)
			.build();
	}

	@Bean
	@Order(200)
	public ZookeeperLockExecutor zookeeperLockExecutor(CuratorFramework curatorFramework) {
		return new ZookeeperLockExecutor(curatorFramework);
	}


}
