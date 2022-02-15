package com.taotao.cloud.web.schedule.core;

import com.taotao.cloud.web.schedule.ScheduledException;
import com.taotao.cloud.web.schedule.core.strategy.CronStrategy;
import com.taotao.cloud.web.schedule.core.strategy.FixedDelayStrategy;
import com.taotao.cloud.web.schedule.core.strategy.FixedRateStrategy;
import com.taotao.cloud.web.schedule.core.strategy.ScheduleStrategy;
import com.taotao.cloud.web.schedule.enums.ScheduledType;
import com.taotao.cloud.web.schedule.model.ScheduledJobModel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * ScheduledFutureFactory
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 16:56:32
 */
public class ScheduledFutureFactory {

	private static final Map<ScheduledType, ScheduleStrategy> STRATEGY_CACHE = new HashMap<>(3);

	/**
	 * 策略启动定时任务
	 *
	 * @param threadPoolTaskScheduler 定时任务线程池
	 * @param scheduledJobModel       定时任务源信息
	 * @param runnable                执行逻辑
	 */
	public static ScheduledFuture<?> create(ThreadPoolTaskScheduler threadPoolTaskScheduler,
		ScheduledJobModel scheduledJobModel, Runnable runnable) {
		ScheduledType type = scheduledJobModel.getType();
		ScheduleStrategy scheduleStrategy = scheduleStrategy(type);
		return scheduleStrategy.schedule(threadPoolTaskScheduler, scheduledJobModel, runnable);
	}

	/**
	 * 静态工厂
	 *
	 * @param type 定时任务的类型
	 */
	private static ScheduleStrategy scheduleStrategy(ScheduledType type) {
		ScheduleStrategy scheduleStrategy = STRATEGY_CACHE.get(type);
		if (scheduleStrategy != null) {
			return scheduleStrategy;
		}
		switch (type) {
			case CRON:
				scheduleStrategy = new CronStrategy();
				break;
			case FIXED_DELAY:
				scheduleStrategy = new FixedDelayStrategy();
				break;
			case FIXED_RATE:
				scheduleStrategy = new FixedRateStrategy();
				break;
			default:
				throw new ScheduledException("创建定时任务，执行失败，无法确定创建定时任务的类型");
		}
		STRATEGY_CACHE.put(type, scheduleStrategy);
		return scheduleStrategy;
	}
}
