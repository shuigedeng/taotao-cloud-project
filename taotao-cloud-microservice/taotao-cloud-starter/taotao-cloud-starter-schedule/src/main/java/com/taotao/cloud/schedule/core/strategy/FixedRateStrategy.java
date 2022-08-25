package com.taotao.cloud.schedule.core.strategy;

import com.taotao.cloud.schedule.model.ScheduledJobModel;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.FixedRateTask;

/**
 * FixedRateStrategy
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:16:15
 */
public class FixedRateStrategy implements ScheduleStrategy {

	@Override
	public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler,
		ScheduledJobModel scheduledJobModel, Runnable runnable) {
		Long fixedRate =
			scheduledJobModel.getFixedRateString() == null ? scheduledJobModel.getFixedRate()
				: Long.valueOf(scheduledJobModel.getFixedRateString());
		Long initialDelay =
			scheduledJobModel.getInitialDelayString() == null ? scheduledJobModel.getInitialDelay()
				: Long.valueOf(scheduledJobModel.getInitialDelayString());
		if (initialDelay == null) {
			initialDelay = 0L;
		}
		FixedRateTask fixedRateTask = new FixedRateTask(runnable, fixedRate, initialDelay);
		Date startTime = new Date(System.currentTimeMillis() + fixedRateTask.getInitialDelay());
		return threadPoolTaskScheduler.scheduleAtFixedRate(runnable, startTime,
			fixedRateTask.getInterval());
	}
}
