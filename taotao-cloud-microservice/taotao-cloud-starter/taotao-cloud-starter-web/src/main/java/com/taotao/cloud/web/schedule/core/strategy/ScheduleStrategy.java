package com.taotao.cloud.web.schedule.core.strategy;

import com.taotao.cloud.web.schedule.model.ScheduledJobModel;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * ScheduleStrategy
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:04:05
 */
public interface ScheduleStrategy {

	/**
	 * schedule
	 *
	 * @param threadPoolTaskScheduler threadPoolTaskScheduler
	 * @param scheduledJobModel         scheduledSource
	 * @param runnable                runnable
	 * @return {@link java.util.concurrent.ScheduledFuture&lt;?&gt; }
	 * @since 2022-02-09 17:04:08
	 */
	ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler,
		ScheduledJobModel scheduledJobModel, Runnable runnable);
}
