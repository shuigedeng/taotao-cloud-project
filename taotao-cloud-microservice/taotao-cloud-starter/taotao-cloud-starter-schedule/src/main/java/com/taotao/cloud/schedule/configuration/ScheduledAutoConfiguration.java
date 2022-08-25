package com.taotao.cloud.schedule.configuration;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.schedule.core.ScheduledApplicationRunner;
import com.taotao.cloud.schedule.core.ScheduledConfig;
import com.taotao.cloud.schedule.core.ScheduledManager;
import com.taotao.cloud.schedule.core.ScheduledPostProcessor;
import com.taotao.cloud.schedule.core.interceptor.ColonyStrengthen;
import com.taotao.cloud.schedule.core.interceptor.ExecutionFlagStrengthen;
import com.taotao.cloud.schedule.core.interceptor.LogStrengthen;
import com.taotao.cloud.schedule.model.ZookeeperNodeData;
import com.taotao.cloud.schedule.properties.ScheduledPluginProperties;
import com.taotao.cloud.schedule.properties.ScheduledProperties;
import com.taotao.cloud.schedule.properties.ThreadPoolTaskSchedulerProperties;
import com.taotao.cloud.zookeeper.template.ZookeeperTemplate;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * ScheduledAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:07:00
 */
@AutoConfiguration
@EnableScheduling
@ConditionalOnProperty(prefix = ScheduledProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(value = {ThreadPoolTaskSchedulerProperties.class, ScheduledPluginProperties.class, ScheduledProperties.class})
public class ScheduledAutoConfiguration implements SchedulingConfigurer {

	public static String ROOT_PATH = "/taotao-cloud-scheduled";
	public static String COLONY_STRENGTHEN = "/ColonyStrengthen";

	@Autowired
	private ThreadPoolTaskSchedulerProperties threadPoolTaskSchedulerProperties;
	@Autowired
	private ScheduledPluginProperties scheduledPluginProperties;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(threadPoolTaskSchedulerProperties.getPoolSize());
		taskScheduler.setRemoveOnCancelPolicy(true);
		taskScheduler.setThreadNamePrefix(threadPoolTaskSchedulerProperties.getThreadNamePrefix());
		taskScheduler.setErrorHandler(LogUtil::error);
		taskScheduler.setWaitForTasksToCompleteOnShutdown(
			threadPoolTaskSchedulerProperties.getWaitForTasksToCompleteOnShutdown());
		taskScheduler.setAwaitTerminationSeconds(
			threadPoolTaskSchedulerProperties.getAwaitTerminationSeconds());
		taskScheduler.initialize();
		taskRegistrar.setTaskScheduler(taskScheduler);
	}

	@Bean("scheduledThreadPoolTaskScheduler")
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
	@DependsOn("scheduledConfig")
	public ScheduledPostProcessor superScheduledPostProcessor() {
		return new ScheduledPostProcessor();
	}

	@Bean
	@DependsOn("scheduledThreadPoolTaskScheduler")
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
