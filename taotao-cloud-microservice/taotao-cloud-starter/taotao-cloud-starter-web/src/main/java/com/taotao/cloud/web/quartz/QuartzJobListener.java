package com.taotao.cloud.web.quartz;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

/**
 * CustomJobListener
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-17 09:06:31
 */
public class QuartzJobListener extends JobListenerSupport {

	public static final QuartzLogModel LOG = new QuartzLogModel();

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

		QuartzJobModel quartzJobModel = (QuartzJobModel) context.getMergedJobDataMap().get(
			QuartzJobModel.JOB_KEY);

		LOG.setJobName(quartzJobModel.getJobName());
		LOG.setBaenName(quartzJobModel.getBeanName());
		LOG.setMethodName(quartzJobModel.getMethodName());
		LOG.setParams(quartzJobModel.getParams());
		LOG.setCronExpression(quartzJobModel.getCronExpression());
		LOG.setStartTime(LocalDateTime.now());
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

		QuartzJobModel quartzJobModel = (QuartzJobModel) context.getMergedJobDataMap().get(
			QuartzJobModel.JOB_KEY);

		RedisRepository redisRepository = ContextUtils.getBean(RedisRepository.class, true);

		if (Objects.isNull(jobException)) {
			long times = DateUtils.getTimestamp() - Timestamp.valueOf(LOG.getStartTime()).getTime();
			LOG.setTime(times);
			LOG.setSuccess(true);
			LogUtils.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJobModel.getJobName(), times);
		} else {
			LogUtils.error("任务执行失败，任务名称：{}" + quartzJobModel.getJobName(), jobException);
			long times = DateUtils.getTimestamp() - Timestamp.valueOf(LOG.getStartTime()).getTime();
			LOG.setTime(times);
			// 任务状态 0：成功 1：失败
			LOG.setSuccess(false);
			LOG.setExceptionDetail(LogUtils.getStackTrace(jobException));
			quartzJobModel.setPause(false);

			// 发送数据到redis  sys模块更新状态
			if (Objects.nonNull(redisRepository)) {
				redisRepository.send(RedisConstant.QUARTZ_JOB_UPDATE_TOPIC, quartzJobModel);
			}
			//quartzJobService.updateIsPause(quartzJobModel);
		}

		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.QUARTZ_JOB_LOG_ADD_TOPIC, LOG);
		}

		//quartzLogService.save(log);
	}
}
