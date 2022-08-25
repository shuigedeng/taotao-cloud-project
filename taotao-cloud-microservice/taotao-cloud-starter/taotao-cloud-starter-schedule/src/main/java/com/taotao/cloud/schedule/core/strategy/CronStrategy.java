package com.taotao.cloud.schedule.core.strategy;

import com.taotao.cloud.schedule.model.ScheduledJobModel;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 * CronStrategy
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:16:15
 */
public class CronStrategy implements ScheduleStrategy {

	@Override
	public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler,
		ScheduledJobModel scheduledJobModel, Runnable runnable) {
		return threadPoolTaskScheduler.schedule(runnable,
			new CronTrigger(scheduledJobModel.getCron()));
	}
}
