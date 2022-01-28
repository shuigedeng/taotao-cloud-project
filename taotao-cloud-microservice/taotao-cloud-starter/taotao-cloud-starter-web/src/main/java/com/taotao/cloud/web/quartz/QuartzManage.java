/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.web.quartz;

import static org.quartz.TriggerBuilder.newTrigger;

import com.taotao.cloud.common.utils.LogUtil;
import java.util.Date;
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

public class QuartzManage {

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
			if (quartzJobModel.getPause()) {
				pauseJob(quartzJobModel);
			}
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
			if (quartzJobModel.getPause()) {
				pauseJob(quartzJobModel);
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
		} catch (Exception e) {
			LogUtil.error("定时任务暂停失败", e);
			throw new QuartzExecutionExecution("定时任务暂停失败");
		}
	}
}
