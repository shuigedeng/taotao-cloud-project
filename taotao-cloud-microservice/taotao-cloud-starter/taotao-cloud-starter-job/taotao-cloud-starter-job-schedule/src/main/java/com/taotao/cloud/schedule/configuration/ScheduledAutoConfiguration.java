package com.taotao.cloud.schedule.configuration;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.schedule.properties.ScheduledProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(value = {ScheduledProperties.class})
public class ScheduledAutoConfiguration implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(20);
		taskScheduler.setRemoveOnCancelPolicy(true);
		taskScheduler.setThreadNamePrefix("taotao-cloud-scheduled-task");
		taskScheduler.setErrorHandler(LogUtils::error);
		taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
		taskScheduler.setAwaitTerminationSeconds(50);
		taskScheduler.initialize();

		taskRegistrar.setTaskScheduler(taskScheduler);
	}

}
