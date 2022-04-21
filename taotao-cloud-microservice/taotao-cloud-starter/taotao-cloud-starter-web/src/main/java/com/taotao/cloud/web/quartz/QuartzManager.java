/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.web.quartz;

import static org.quartz.TriggerBuilder.newTrigger;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Date;
import java.util.Objects;
import javax.annotation.Resource;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;

public class QuartzManager {

	private static final String JOB_NAME = "TASK_";

	@Resource(name = "scheduler")
	private Scheduler scheduler;

	/**
	 * 添加job cron
	 */
	public void addJob(QuartzJobModel quartzJobModel) {
		try {
			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(QuartzExecutionJob.class).
				withIdentity(JOB_NAME + quartzJobModel.getId())
				.build();

			//通过触发器名和cron 表达式创建 Trigger
			Trigger cronTrigger = newTrigger()
				.withIdentity(JOB_NAME + quartzJobModel.getId())
				.startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule(quartzJobModel.getCronExpression()))
				.build();

			cronTrigger.getJobDataMap().put(QuartzJobModel.JOB_KEY, quartzJobModel);

			//重置启动时间
			((CronTriggerImpl) cronTrigger).setStartTime(new Date());

			//执行定时任务
			scheduler.scheduleJob(jobDetail, cronTrigger);

			// 暂停任务
			if (quartzJobModel.isPause()) {
				pauseJob(quartzJobModel);
			}

			// 添加日志
			RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
			if (Objects.nonNull(redisRepository)) {
				redisRepository.send(RedisConstant.QUARTZ_JOB_ADD_TOPIC, quartzJobModel);
			}

			LogUtil.info("添加Quartz定时任务成功");
		} catch (Exception e) {
			LogUtil.error("创建定时任务失败", e);
			throw new QuartzExecutionExecution("创建定时任务失败");
		}
	}

	/**
	 * 更新job cron表达式
	 */
	public void updateJobCron(QuartzJobModel quartzJobModel) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJobModel.getId());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			// 如果不存在则创建一个定时任务
			if (trigger == null) {
				addJob(quartzJobModel);
				trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			}

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(
				quartzJobModel.getCronExpression());
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
				.withSchedule(scheduleBuilder).build();

			//重置启动时间
			((CronTriggerImpl) trigger).setStartTime(new Date());
			trigger.getJobDataMap().put(QuartzJobModel.JOB_KEY, quartzJobModel);

			scheduler.rescheduleJob(triggerKey, trigger);

			// 暂停任务
			if (quartzJobModel.isPause()) {
				pauseJob(quartzJobModel);
			}

			RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
			if (Objects.nonNull(redisRepository)) {
				redisRepository.send(RedisConstant.QUARTZ_JOB_UPDATE_CRON_TOPIC, quartzJobModel);
			}
		} catch (Exception e) {
			LogUtil.error("更新定时任务失败", e);
			throw new QuartzExecutionExecution("更新定时任务失败");
		}
	}

	/**
	 * 删除一个job
	 */
	public void deleteJob(QuartzJobModel quartzJobModel) {
		try {
			JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJobModel.getId());
			scheduler.pauseJob(jobKey);
			scheduler.deleteJob(jobKey);

			RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
			if (Objects.nonNull(redisRepository)) {
				redisRepository.send(RedisConstant.QUARTZ_JOB_DELETE_TOPIC, quartzJobModel);
			}
		} catch (Exception e) {
			LogUtil.error("删除定时任务失败", e);
			throw new QuartzExecutionExecution("删除定时任务失败");
		}
	}

	/**
	 * 恢复一个job
	 */
	public void resumeJob(QuartzJobModel quartzJobModel) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJobModel.getId());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 如果不存在则创建一个定时任务
			if (trigger == null) {
				addJob(quartzJobModel);
			}
			JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJobModel.getId());
			scheduler.resumeJob(jobKey);
			quartzJobModel.setPause(!quartzJobModel.isPause());

			RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
			if (Objects.nonNull(redisRepository)) {
				redisRepository.send(RedisConstant.QUARTZ_JOB_RESUME_TOPIC, quartzJobModel);
			}
		} catch (Exception e) {
			LogUtil.error("恢复定时任务失败", e);
			throw new QuartzExecutionExecution("恢复定时任务失败");
		}
	}

	/**
	 * 立即执行job
	 */
	public void runJobNow(QuartzJobModel quartzJobModel) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJobModel.getId());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 如果不存在则创建一个定时任务
			if (trigger == null) {
				addJob(quartzJobModel);
			}
			JobDataMap dataMap = new JobDataMap();
			dataMap.put(QuartzJobModel.JOB_KEY, quartzJobModel);
			JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJobModel.getId());
			scheduler.triggerJob(jobKey, dataMap);

			RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
			if (Objects.nonNull(redisRepository)) {
				redisRepository.send(RedisConstant.QUARTZ_JOB_RUN_NOW_TOPIC, quartzJobModel);
			}
		} catch (Exception e) {
			LogUtil.error("定时任务执行失败", e);
			throw new QuartzExecutionExecution("定时任务执行失败");
		}
	}

	/**
	 * 暂停一个job
	 */
	public void pauseJob(QuartzJobModel quartzJobModel) {
		try {
			JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJobModel.getId());
			scheduler.pauseJob(jobKey);

			RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
			if (Objects.nonNull(redisRepository)) {
				redisRepository.send(RedisConstant.QUARTZ_JOB_PAUSE_TOPIC, quartzJobModel);
			}
		} catch (Exception e) {
			LogUtil.error("定时任务暂停失败", e);
			throw new QuartzExecutionExecution("定时任务暂停失败");
		}
	}
}
