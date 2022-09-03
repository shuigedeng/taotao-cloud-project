package com.taotao.cloud.quartz.other;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.Map;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartzJobServiceImpl implements QuartzJobService {

	@Autowired
	private Scheduler scheduler;

	@Override
	public void addJob(String clazzName, String jobName, String groupName, String cronExp,
		Map<String, Object> param) {
		try {
			// 启动调度器，默认初始化的时候已经启动
			// scheduler.start();
			// 构建job信息
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(clazzName);
			JobDetail jobDetail = JobBuilder.newJob(jobClass)
				.withIdentity(jobName, groupName)
				.build();
			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(jobName, groupName)
				.withSchedule(scheduleBuilder)
				.build();
			// 获得JobDataMap，写入数据
			if (param != null) {
				trigger.getJobDataMap().putAll(param);
			}
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (ObjectAlreadyExistsException e) {
			LogUtils.warn("{}组下的{}任务已存在，创建失败", groupName, jobName);
		} catch (Exception e) {
			LogUtils.error("创建任务失败", e);
		}
	}

	@Override
	public void pauseJob(String jobName, String groupName) {
		try {
			scheduler.pauseJob(JobKey.jobKey(jobName, groupName));
		} catch (SchedulerException e) {
			LogUtils.error("暂停任务失败", e);
		}
	}

	@Override
	public void resumeJob(String jobName, String groupName) {
		try {
			scheduler.resumeJob(JobKey.jobKey(jobName, groupName));
		} catch (SchedulerException e) {
			LogUtils.error("恢复任务失败", e);
		}
	}

	@Override
	public void runOnce(String jobName, String groupName) {
		try {
			scheduler.triggerJob(JobKey.jobKey(jobName, groupName));
		} catch (SchedulerException e) {
			LogUtils.error("立即运行一次定时任务失败", e);
		}
	}

	@Override
	public void updateJob(String jobName, String groupName, String cronExp,
		Map<String, Object> param) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (cronExp != null) {
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder()
					.withIdentity(triggerKey)
					.withSchedule(scheduleBuilder)
					.build();
			}
			// 修改map
			if (param != null) {
				trigger.getJobDataMap().putAll(param);
			}
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (Exception e) {
			LogUtils.error("更新任务失败", e);
		}
	}

	@Override
	public void deleteJob(String jobName, String groupName) {
		try {
			// 暂停、移除、删除
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, groupName));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, groupName));
			scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
		} catch (Exception e) {
			LogUtils.error("删除任务失败", e);
		}
	}

	@Override
	public void startAllJobs() {
		try {
			scheduler.start();
		} catch (Exception e) {
			LogUtils.error("开启所有的任务失败", e);
		}
	}

	@Override
	public void pauseAllJobs() {
		try {
			scheduler.pauseAll();
		} catch (Exception e) {
			LogUtils.error("暂停所有任务失败", e);
		}
	}

	@Override
	public void resumeAllJobs() {
		try {
			scheduler.resumeAll();
		} catch (Exception e) {
			LogUtils.error("恢复所有任务失败", e);
		}
	}

	@Override
	public void shutdownAllJobs() {
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
}
