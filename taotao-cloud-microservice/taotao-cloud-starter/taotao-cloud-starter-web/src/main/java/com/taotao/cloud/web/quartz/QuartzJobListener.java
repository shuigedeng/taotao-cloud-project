package com.taotao.cloud.web.quartz;

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.DateUtil;
import com.taotao.cloud.common.utils.LogUtil;
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

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobKey = context.getJobDetail().getKey().toString();
		LogUtil.info("CustomJobListener 定时任务:{}-开始执行", jobKey);

		QuartzJobModel quartzJobModel = (QuartzJobModel) context.getMergedJobDataMap().get(
			QuartzJobModel.JOB_KEY);

		QuartzLogModel log = new QuartzLogModel();
		log.setJobName(quartzJobModel.getJobName());
		log.setBaenName(quartzJobModel.getBeanName());
		log.setMethodName(quartzJobModel.getMethodName());
		log.setParams(quartzJobModel.getParams());
		log.setCronExpression(quartzJobModel.getCronExpression());
		log.setStartTime(LocalDateTime.now());

		QuartzLogHolder.setLog(log);
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		String jobKey = context.getJobDetail().getKey().toString();
		LogUtil.info("CustomJobListener 定时任务:{}-执行结束", jobKey);

		QuartzJobModel quartzJobModel = (QuartzJobModel) context.getMergedJobDataMap().get(
			QuartzJobModel.JOB_KEY);

		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);

		QuartzLogModel log = QuartzLogHolder.getLog();
		if (Objects.isNull(jobException)) {
			long times = DateUtil.getTimestamp() - Timestamp.valueOf(log.getStartTime()).getTime();
			log.setTime(times);
			log.setSuccess(true);
			LogUtil.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJobModel.getJobName(), times);
		} else {
			LogUtil.error("任务执行失败，任务名称：{}" + quartzJobModel.getJobName(), jobException);
			long times = DateUtil.getTimestamp() - Timestamp.valueOf(log.getStartTime()).getTime();
			log.setTime(times);
			// 任务状态 0：成功 1：失败
			log.setSuccess(false);
			log.setExceptionDetail(LogUtil.getStackTrace(jobException));
			quartzJobModel.setPause(false);

			//更新状态
			if (Objects.nonNull(redisRepository)) {
				redisRepository.send("", "");
			}
			//quartzJobService.updateIsPause(quartzJobModel);
		}

		if (Objects.nonNull(redisRepository)) {
			redisRepository.send("", "");
		}
		//quartzLogService.save(log);
		QuartzLogHolder.clear();
	}
}
