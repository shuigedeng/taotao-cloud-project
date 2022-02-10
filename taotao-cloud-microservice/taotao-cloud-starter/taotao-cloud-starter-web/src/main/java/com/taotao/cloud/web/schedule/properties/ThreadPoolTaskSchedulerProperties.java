package com.taotao.cloud.web.schedule.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ThreadPoolTaskSchedulerProperties
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 16:50:13
 */
@ConfigurationProperties(prefix = ThreadPoolTaskSchedulerProperties.PREFIX)
public class ThreadPoolTaskSchedulerProperties {

	public static final String PREFIX = "taotao.cloud.web.scheduled.thread-pool";
	//线程池大小
	private Integer poolSize = 10;
	//线程名前缀
	private String threadNamePrefix = "taotao-cloud-scheduled-thread-";
	//设置是否关闭时等待执行中的任务执行完成
	private Boolean waitForTasksToCompleteOnShutdown = false;
	//设置此执行器被关闭时等待的最长时间，用于在其余容器继续关闭之前等待剩余任务执行完成
	//需要将waitForTasksToCompleteOnShutdown设置为true，此配置才起作用
	private Integer awaitTerminationSeconds = 0;

	public Integer getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(Integer poolSize) {
		this.poolSize = poolSize;
	}

	public String getThreadNamePrefix() {
		return threadNamePrefix;
	}

	public void setThreadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix;
	}

	public Boolean getWaitForTasksToCompleteOnShutdown() {
		return waitForTasksToCompleteOnShutdown;
	}

	public void setWaitForTasksToCompleteOnShutdown(Boolean waitForTasksToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
	}

	public Integer getAwaitTerminationSeconds() {
		return awaitTerminationSeconds;
	}

	public void setAwaitTerminationSeconds(Integer awaitTerminationSeconds) {
		this.awaitTerminationSeconds = awaitTerminationSeconds;
	}
}
