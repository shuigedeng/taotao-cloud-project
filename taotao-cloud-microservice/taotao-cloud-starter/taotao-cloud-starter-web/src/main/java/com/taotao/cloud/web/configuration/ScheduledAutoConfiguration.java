package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.schedule.core.ScheduledApplicationRunner;
import com.taotao.cloud.web.schedule.core.ScheduledConfig;
import com.taotao.cloud.web.schedule.core.ScheduledManager;
import com.taotao.cloud.web.schedule.core.ScheduledPostProcessor;
import com.taotao.cloud.web.schedule.core.interceptor.ColonyStrengthen;
import com.taotao.cloud.web.schedule.core.interceptor.ExecutionFlagStrengthen;
import com.taotao.cloud.web.schedule.core.interceptor.LogStrengthen;
import com.taotao.cloud.web.schedule.model.ZookeeperNodeData;
import com.taotao.cloud.web.schedule.properties.ScheduledPluginProperties;
import com.taotao.cloud.web.schedule.properties.ScheduledProperties;
import com.taotao.cloud.web.schedule.properties.ThreadPoolTaskSchedulerProperties;
import com.taotao.cloud.zookeeper.template.ZookeeperTemplate;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * ScheduledAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:07:00
 */
@Configuration
@ConditionalOnProperty(prefix = ScheduledProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {ThreadPoolTaskSchedulerProperties.class,
	ScheduledPluginProperties.class, ScheduledProperties.class})
public class ScheduledAutoConfiguration {

	public static String ROOT_PATH = "/taotao-cloud-scheduled";
	public static String COLONY_STRENGTHEN = "/ColonyStrengthen";

	@Autowired
	private ThreadPoolTaskSchedulerProperties threadPoolTaskSchedulerProperties;
	@Autowired
	private ScheduledPluginProperties scheduledPluginProperties;

	@Bean("threadPoolTaskScheduler")
	@ConditionalOnMissingBean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(threadPoolTaskSchedulerProperties.getPoolSize());
		taskScheduler.setThreadNamePrefix(threadPoolTaskSchedulerProperties.getThreadNamePrefix());
		taskScheduler.setWaitForTasksToCompleteOnShutdown(
			threadPoolTaskSchedulerProperties.getWaitForTasksToCompleteOnShutdown());
		taskScheduler.setAwaitTerminationSeconds(
			threadPoolTaskSchedulerProperties.getAwaitTerminationSeconds());
		taskScheduler.initialize();
		return taskScheduler;
	}

	@Bean(name = "scheduledConfig")
	public ScheduledConfig superScheduledConfig() {
		return new ScheduledConfig();
	}

	@Bean
	@ConditionalOnBean(ScheduledConfig.class)
	public ScheduledManager superScheduledManager() {
		return new ScheduledManager();
	}

	@Bean
	@DependsOn({"scheduledConfig"})
	public ScheduledPostProcessor superScheduledPostProcessor() {
		return new ScheduledPostProcessor();
	}

	@Bean
	@DependsOn("threadPoolTaskScheduler")
	public ScheduledApplicationRunner superScheduledApplicationRunner() {
		return new ScheduledApplicationRunner();
	}

	@Bean
	@ConditionalOnProperty(prefix = ScheduledPluginProperties.PREFIX, name = "executionFlag", havingValue = "true")
	@ConditionalOnMissingBean
	public ExecutionFlagStrengthen executionFlagStrengthen() {
		return new ExecutionFlagStrengthen();
	}

	@Bean
	@ConditionalOnProperty(prefix = ScheduledPluginProperties.PREFIX, name = "executionLog", havingValue = "true")
	@ConditionalOnMissingBean
	public LogStrengthen logStrengthen() {
		return new LogStrengthen();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(ZookeeperTemplate.class)
	@ConditionalOnProperty(prefix = ScheduledPluginProperties.PREFIX, name = "colony", havingValue = "true")
	public ZookeeperNodeData zookeeperNodeData(ZookeeperTemplate zookeeperTemplate) {
		ZookeeperNodeData data = new ZookeeperNodeData();

		if (!zookeeperTemplate.exists(ROOT_PATH)) {
			zookeeperTemplate.createNode(ROOT_PATH, CreateMode.PERSISTENT);
		}

		if (!zookeeperTemplate.exists(
			ROOT_PATH + "/" + scheduledPluginProperties.getColonyName())) {
			zookeeperTemplate.createNode(
				ROOT_PATH + "/" + scheduledPluginProperties.getColonyName(),
				CreateMode.PERSISTENT);
		}

		String zkPath = null;
		try {
			zkPath = zookeeperTemplate.createNode(
				ROOT_PATH + "/" + scheduledPluginProperties.getColonyName() + COLONY_STRENGTHEN,
				InetAddress.getLocalHost().getHostAddress(),
				CreateMode.EPHEMERAL_SEQUENTIAL);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		data.setZkPath(zkPath);
		data.setZkParentNodePath(ROOT_PATH + "/" + scheduledPluginProperties.getColonyName());

		return data;
	}

	@Bean
	@ConditionalOnProperty(prefix = ScheduledPluginProperties.PREFIX, name = "colony", havingValue = "true")
	@ConditionalOnMissingBean
	@ConditionalOnBean({ZookeeperTemplate.class, ZookeeperNodeData.class})
	public ColonyStrengthen colonyStrengthen() {
		return new ColonyStrengthen();
	}
}
