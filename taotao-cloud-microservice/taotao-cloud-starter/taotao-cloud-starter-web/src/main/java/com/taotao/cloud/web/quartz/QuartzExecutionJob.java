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
package com.taotao.cloud.web.quartz;

import static com.taotao.cloud.web.configuration.QuartzAutoConfiguration.EXECUTOR;

import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.Objects;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 参考人人开源，https://gitee.com/renrenio/renren-security
 */
@Async
public class QuartzExecutionJob extends QuartzJobBean {



	@Override
	protected void executeInternal(JobExecutionContext context) {
		//QuartzJobModel quartzJobModel = (QuartzJobModel) context.getMergedJobDataMap().get(
		//	QuartzJobModel.JOB_KEY);
		//// 获取spring bean
		//QuartzLogService quartzLogService = ContextUtil.getBean(QuartzLogService.class, true);
		//QuartzJobService quartzJobService = ContextUtil.getBean(QuartzJobService.class, true);
		//
		//QuartzLogModel log = new QuartzLogModel();
		//log.setJobName(quartzJobModel.getJobName());
		//log.setBaenName(quartzJobModel.getBeanName());
		//log.setMethodName(quartzJobModel.getMethodName());
		//log.setParams(quartzJobModel.getParams());
		//long startTime = System.currentTimeMillis();
		//log.setCronExpression(quartzJobModel.getCronExpression());
		//
		//try {
		//	// 执行任务
		//	LogUtil.info("任务准备执行，任务名称：{}", quartzJobModel.getJobName());
		//	QuartzRunnable task = new QuartzRunnable(
		//		quartzJobModel.getBeanName(),
		//		quartzJobModel.getMethodName(),
		//		quartzJobModel.getParams());
		//
		//	Future<?> future = EXECUTOR.submit(task);
		//	future.get();
		//	long times = System.currentTimeMillis() - startTime;
		//	log.setTime(times);
		//
		//	// 任务状态
		//	log.setSuccess(true);
		//	LogUtil.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJobModel.getJobName(), times);
		//} catch (Exception e) {
		//	LogUtil.error("任务执行失败，任务名称：{}" + quartzJobModel.getJobName(), e);
		//	long times = System.currentTimeMillis() - startTime;
		//	log.setTime(times);
		//	// 任务状态 0：成功 1：失败
		//	log.setSuccess(false);
		//	log.setExceptionDetail(LogUtil.getStackTrace(e));
		//	quartzJobModel.setPause(false);
		//	//更新状态
		//	quartzJobService.updateIsPause(quartzJobModel);
		//} finally {
		//	quartzLogService.save(log);
		//}

		QuartzJobModel quartzJobModel = (QuartzJobModel) context.getMergedJobDataMap().get(
			QuartzJobModel.JOB_KEY);

		QuartzRunnable task = null;
		// 执行任务
		try {
			LogUtil.info("任务准备执行，任务名称：{}", quartzJobModel.getJobName());
			task = new QuartzRunnable(
				quartzJobModel.getBeanName(),
				quartzJobModel.getMethodName(),
				quartzJobModel.getParams());

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			LogUtil.error(e);
		}

		if (Objects.nonNull(task)) {
			EXECUTOR.submit(task);
		}
	}
}
