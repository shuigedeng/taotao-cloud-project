package com.taotao.cloud.job.quartz.listener;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.quartz.entity.QuartzJob;
import com.taotao.cloud.job.quartz.entity.QuartzJobLog;
import com.taotao.cloud.job.quartz.enums.QuartzJobCode;
import com.taotao.cloud.job.quartz.event.QuartzEvent;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * CustomJobListener
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-17 09:06:31
 */
public class QuartzJobListener extends JobListenerSupport {

	public static final ThreadLocal<QuartzJobLog> QUARTZ_JOB_LOG_THREAD_LOCAL = new ThreadLocal<>();

	@Override
	public String getName() {
		return getClass().getName();
	}

	/**
	 * Scheduler 在 JobDetail 将要被执行时调用这个方法
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobKey = context.getJobDetail().getKey().toString();
		LogUtils.info("CustomJobListener 定时任务:{}-开始执行", jobKey);

		QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);

		QuartzJobLog log = new QuartzJobLog();
		log.setJobName(quartzJob.getJobName());
		log.setBeanName(quartzJob.getBeanName());
		log.setMethodName(quartzJob.getMethodName());
		log.setParams(quartzJob.getParams());
		log.setCronExpression(quartzJob.getCronExpression());
		log.setStartTime(LocalDateTime.now());

		QUARTZ_JOB_LOG_THREAD_LOCAL.set(log);
	}

	/**
	 * Scheduler 在 JobDetail 即将被执行，但又被 TriggerListener否决了时调用这个方法
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		LogUtils.info("CustomJobListener 定时任务被否决执行");
		super.jobExecutionVetoed(context);
	}

	/**
	 * Scheduler 在 JobDetail 被执行之后调用这个方法
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		String jobKey = context.getJobDetail().getKey().toString();
		LogUtils.info("CustomJobListener 定时任务:{}-执行结束", jobKey);

		QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(
			QuartzJob.JOB_KEY);

		QuartzJobLog quartzJobLog = QUARTZ_JOB_LOG_THREAD_LOCAL.get();

		if (Objects.isNull(jobException)) {
			long times = DateUtils.getTimestamp() - Timestamp.valueOf(quartzJobLog.getStartTime()).getTime();
			quartzJobLog.setTime(times);
			quartzJobLog.setSuccess(true);
			LogUtils.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJob.getJobName(), times);
		} else {
			LogUtils.error("任务执行失败，任务名称：{}" + quartzJob.getJobName(), jobException);
			long times = DateUtils.getTimestamp() - Timestamp.valueOf(quartzJobLog.getStartTime()).getTime();
			quartzJobLog.setTime(times);
			// 任务状态 0：成功 1：失败
			quartzJobLog.setSuccess(false);
			quartzJobLog.setExceptionDetail(LogUtils.getStackTrace(jobException));

			// todo 需要修改
			quartzJob.setState(QuartzJobCode.RUNNING);
		}

		QUARTZ_JOB_LOG_THREAD_LOCAL.remove();

		ContextUtils.publishEvent(new QuartzEvent(quartzJobLog));
	}
}
