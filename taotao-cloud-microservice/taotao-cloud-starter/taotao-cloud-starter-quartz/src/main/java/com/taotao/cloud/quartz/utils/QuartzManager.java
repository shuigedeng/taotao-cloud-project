/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.quartz.utils;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.quartz.entity.QuartzJob;
import com.taotao.cloud.quartz.enums.ScheduleConcurrentEnum;
import com.taotao.cloud.quartz.enums.ScheduleStatusEnum;
import com.taotao.cloud.quartz.event.QuartzEvent;
import com.taotao.cloud.quartz.exception.QuartzExecutionException;
import com.taotao.cloud.quartz.execution.ScheduleConcurrentExecution;
import com.taotao.cloud.quartz.execution.ScheduleDisallowConcurrentExecution;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 石英经理
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-06 09:21:01
 */
public class QuartzManager {

	/**
	 * 作业名
	 */
	private static final String JOB_NAME = "TASK_";
	/**
	 * 触发器名字
	 */
	private static final String TRIGGER_NAME = "TRIGGER_";

	/**
	 * 调度器
	 */
	@Resource(name = "scheduler")
	private Scheduler scheduler;

	/**
	 * 添加job cron
	 *
	 * @param quartzJob 石英工作
	 * @since 2022-09-06 09:21:01
	 */
	public void addJob(QuartzJob quartzJob) {
		try {
			JobKey jobKey = getJobKey(quartzJob);
			// 构建job信息
			JobDetail jobDetail = JobBuilder
				.newJob(getJobClass(quartzJob))
				.withIdentity(jobKey)
				.withDescription(quartzJob.getRemark())
				.storeDurably()
				.build();

			// 表达式调度构建器
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule(quartzJob.getCronExpression())
				.withMisfireHandlingInstructionDoNothing();
			TriggerKey triggerKey = getTriggerKey(quartzJob);

			//通过触发器名和cron 表达式创建 Trigger
			Trigger cronTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity(triggerKey)
				.withSchedule(cronScheduleBuilder)
				.build();

			cronTrigger.getJobDataMap().put(QuartzJob.JOB_KEY, quartzJob);

			//重置启动时间
			((CronTriggerImpl) cronTrigger).setStartTime(new Date());

			//执行定时任务
			scheduler.scheduleJob(jobDetail, cronTrigger);

			// 判断是否存在
			if (scheduler.checkExists(jobKey)) {
				// 防止创建时存在数据问题，先移除，然后再执行创建操作
				deleteJob(quartzJob);
			}

			// 判断任务是否过期
			if (CronUtils.getNextExecution(quartzJob.getCronExpression()) != null) {
				// 执行调度任务
				scheduler.scheduleJob(jobDetail, cronTrigger);
			}

			// 暂停任务
			if (quartzJob.getState().equals(ScheduleStatusEnum.PAUSE.getValue())) {
				pauseJob(quartzJob);
			}

			// 添加日志
			// RedisRepository redisRepository = ContextUtils.getBean(RedisRepository.class, true);
			// if (Objects.nonNull(redisRepository)) {
			// 	redisRepository.send(RedisConstant.QUARTZ_JOB_ADD_TOPIC, quartzJobModel);
			// }

			ContextUtils.publishEvent(new QuartzEvent(quartzJob));

			LogUtils.info("添加Quartz定时任务成功");
		} catch (Exception e) {
			LogUtils.error("创建定时任务失败", e);
			throw new QuartzExecutionException("创建定时任务失败");
		}
	}


	/**
	 * 更新定时任务
	 *
	 * @param quartzJob 石英工作
	 * @since 2022-09-06 09:21:01
	 */
	public void updateJob(QuartzJob quartzJob) {
		try {
			// 防止创建时存在数据问题，先移除，然后再执行创建操作
			if (checkExists(quartzJob)) {
				deleteJob(quartzJob);
			}
		} catch (SchedulerException e) {
			throw new RuntimeException("更新定时任务失败", e);
		}

		addJob(quartzJob);
	}

	/**
	 * checkExists
	 *
	 * @param quartzJob quartzJob
	 * @return boolean
	 * @since 2022-09-06 09:21:01
	 */
	public boolean checkExists(QuartzJob quartzJob) throws SchedulerException {
		return scheduler.checkExists(getJobKey(quartzJob));
	}


	/**
	 * 删除一个job
	 *
	 * @param quartzJob 石英工作
	 * @since 2022-09-06 09:21:01
	 */
	public void deleteJob(QuartzJob quartzJob) {
		try {
			scheduler.pauseJob(getJobKey(quartzJob));
			scheduler.deleteJob(getJobKey(quartzJob));

			// RedisRepository redisRepository = ContextUtils.getBean(RedisRepository.class, true);
			// if (Objects.nonNull(redisRepository)) {
			// 	redisRepository.send(RedisConstant.QUARTZ_JOB_DELETE_TOPIC, quartzJobModel);
			// }
			ContextUtils.publishEvent(new QuartzEvent(quartzJob));
		} catch (Exception e) {
			LogUtils.error("删除定时任务失败", e);
			throw new QuartzExecutionException("删除定时任务失败");
		}
	}

	/**
	 * 恢复一个job
	 *
	 * @param quartzJob 石英工作
	 * @since 2022-09-06 09:21:01
	 */
	public void resumeJob(QuartzJob quartzJob) {
		try {
			scheduler.resumeJob(getJobKey(quartzJob));

			// RedisRepository redisRepository = ContextUtils.getBean(RedisRepository.class, true);
			// if (Objects.nonNull(redisRepository)) {
			// 	redisRepository.send(RedisConstant.QUARTZ_JOB_RESUME_TOPIC, quartzJobModel);
			// }
			ContextUtils.publishEvent(new QuartzEvent(quartzJob));
		} catch (Exception e) {
			LogUtils.error("恢复定时任务失败", e);
			throw new QuartzExecutionException("恢复定时任务失败");
		}
	}

	/**
	 * 立即执行job
	 *
	 * @param quartzJob 石英工作
	 * @since 2022-09-06 09:21:02
	 */
	public void runJobNow(QuartzJob quartzJob) {
		try {
			JobDataMap dataMap = new JobDataMap();
			dataMap.put(QuartzJob.JOB_KEY, quartzJob);

			if (checkExists(quartzJob)) {
				scheduler.triggerJob(getJobKey(quartzJob), dataMap);
			}

			// RedisRepository redisRepository = ContextUtils.getBean(RedisRepository.class, true);
			// if (Objects.nonNull(redisRepository)) {
			// 	redisRepository.send(RedisConstant.QUARTZ_JOB_RUN_NOW_TOPIC, quartzJobModel);
			// }
			ContextUtils.publishEvent(new QuartzEvent(quartzJob));
		} catch (Exception e) {
			LogUtils.error("定时任务执行失败", e);
			throw new QuartzExecutionException("定时任务执行失败");
		}
	}

	/**
	 * 暂停一个job
	 *
	 * @param quartzJob 石英工作
	 * @since 2022-09-06 09:21:02
	 */
	public void pauseJob(QuartzJob quartzJob) {
		try {
			scheduler.pauseJob(getJobKey(quartzJob));

			// RedisRepository redisRepository = ContextUtils.getBean(RedisRepository.class, true);
			// if (Objects.nonNull(redisRepository)) {
			// 	redisRepository.send(RedisConstant.QUARTZ_JOB_PAUSE_TOPIC, quartzJobModel);
			// }
			ContextUtils.publishEvent(new QuartzEvent(quartzJob));
		} catch (Exception e) {
			LogUtils.error("定时任务暂停失败", e);
			throw new QuartzExecutionException("定时任务暂停失败");
		}
	}

	/**
	 * 获取任务对象
	 */
	public Class<? extends Job> getJobClass(String classname) {
		Class<?> clazz;
		try {
			clazz = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			throw new QuartzExecutionException("找不到该定时任务类名");
		}
		if (Job.class.isAssignableFrom(clazz)) {
			//noinspection unchecked
			return (Class<Job>) clazz;
		}
		throw new QuartzExecutionException("该类不是定时任务类");
	}

	public static Class<? extends Job> getJobClass(QuartzJob quartzJob) {
		if (quartzJob.getConcurrent().equals(ScheduleConcurrentEnum.NO.getValue())) {
			return ScheduleDisallowConcurrentExecution.class;
		} else {
			return ScheduleConcurrentExecution.class;
		}
	}

	/**
	 * 获取触发器key
	 *
	 * @param quartzJob 石英工作
	 * @return {@link TriggerKey }
	 * @since 2022-09-06 09:21:02
	 */
	public static TriggerKey getTriggerKey(QuartzJob quartzJob) {
		return TriggerKey.triggerKey(TRIGGER_NAME + quartzJob.getId(), quartzJob.getGroupName());
	}

	/**
	 * 获取jobKey
	 *
	 * @param quartzJob 石英工作
	 * @return {@link JobKey }
	 * @since 2022-09-06 09:21:02
	 */
	public static JobKey getJobKey(QuartzJob quartzJob) {
		return JobKey.jobKey(JOB_NAME + quartzJob.getId(), quartzJob.getGroupName());
	}

	/**
	 * 开始所有工作
	 *
	 * @since 2022-09-06 09:21:02
	 */
	public void startAllJobs() {
		try {
			scheduler.start();
		} catch (Exception e) {
			LogUtils.error("开启所有的任务失败", e);
		}
	}

	/**
	 * 暂停所有
	 *
	 * @since 2022-09-06 09:21:02
	 */
	public void pauseAll() {
		try {
			scheduler.pauseAll();
		} catch (Exception e) {
			LogUtils.error("暂停所有任务失败", e);
		}
	}

	/**
	 * 恢复所有
	 *
	 * @since 2022-09-06 09:21:02
	 */
	public void resumeAll() {
		try {
			scheduler.resumeAll();
		} catch (Exception e) {
			LogUtils.error("恢复所有任务失败", e);
		}
	}

	/**
	 * 关闭所有
	 *
	 * @since 2022-09-06 09:21:02
	 */
	public void shutdownAll() {
		try {
			if (!scheduler.isShutdown()) {
				// 需谨慎操作关闭scheduler容器
				// scheduler生命周期结束，无法再 start() 启动scheduler
				scheduler.shutdown(true);
			}
		} catch (Exception e) {
			LogUtils.error("关闭所有的任务失败", e);
		}
	}

	/**
	 * 获取定时任务列表
	 */
	public List<Trigger> findTriggers() {
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			return jobKeys.stream().map(this::getTriggersOfJob)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		} catch (SchedulerException e) {
			LogUtils.error(e);
			throw new QuartzExecutionException(e.getMessage());
		}
	}

	private List<? extends Trigger> getTriggersOfJob(JobKey jobKey) {
		try {
			return scheduler.getTriggersOfJob(jobKey);
		} catch (SchedulerException e) {
			LogUtils.error(e);
			throw new QuartzExecutionException(e);
		}
	}

	public void deleteTrigger(String triggerName) {
		try {
			this.scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName));
		} catch (SchedulerException e) {
			LogUtils.error(e);
		}
	}

	public void clear() {
		try {
			scheduler.clear();
		} catch (SchedulerException e) {
			LogUtils.error(e);
		}
	}
}
