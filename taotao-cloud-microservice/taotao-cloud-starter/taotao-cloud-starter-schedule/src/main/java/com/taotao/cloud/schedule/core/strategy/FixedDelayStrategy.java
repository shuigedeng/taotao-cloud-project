package com.taotao.cloud.schedule.core.strategy;

import com.taotao.cloud.schedule.model.ScheduledJobModel;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.FixedDelayTask;

/**
 * FixedDelayStrategy
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:16:15
 */
public class FixedDelayStrategy implements ScheduleStrategy {

	@Override
	public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler,
		ScheduledJobModel scheduledJobModel, Runnable runnable) {
		Long fixedDelay =
			scheduledJobModel.getFixedDelayString() == null ? scheduledJobModel.getFixedDelay()
				: Long.valueOf(scheduledJobModel.getFixedDelayString());
		Long initialDelay =
			scheduledJobModel.getInitialDelayString() == null ? scheduledJobModel.getInitialDelay()
				: Long.valueOf(scheduledJobModel.getInitialDelayString());
		if (initialDelay == null) {
			initialDelay = 0L;
		}
		FixedDelayTask fixedDelayTask = new FixedDelayTask(runnable, fixedDelay, initialDelay);
		Date startTime = new Date(System.currentTimeMillis() + fixedDelayTask.getInitialDelay());
		return threadPoolTaskScheduler.scheduleWithFixedDelay(runnable, startTime,
			fixedDelayTask.getInterval());
	}
}
