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
package com.taotao.cloud.quartz.execution;


import com.google.common.base.Stopwatch;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.quartz.entity.QuartzJob;
import com.taotao.cloud.quartz.entity.QuartzJobLog;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 文摘石英执行工作
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-06 09:03:37
 */
public abstract class AbstractQuartzExecutionJob extends QuartzJobBean {
	@Override
	protected void executeInternal(JobExecutionContext context) {
		QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);

		QuartzJobLog quartzJobLog = new QuartzJobLog();
		quartzJobLog.setJobName(quartzJob.getJobName());
		quartzJobLog.setBeanName(quartzJob.getBeanName());
		quartzJobLog.setMethodName(quartzJob.getMethodName());
		quartzJobLog.setParams(quartzJob.getParams());
		quartzJobLog.setCronExpression(quartzJob.getCronExpression());
		quartzJobLog.setExecutionThread(Thread.currentThread().getName());
		quartzJobLog.setStartTime(LocalDateTime.now());

		Stopwatch sw = Stopwatch.createStarted();
		try {
			LogUtils.info("准备执行任务，任务ID：{}", quartzJob.getId());

			// 执行任务
			doExecute(quartzJob);

			long seconds = sw.stop().elapsed(TimeUnit.SECONDS);
			quartzJobLog.setTime(seconds);
			quartzJobLog.setSuccess(true);
			quartzJobLog.setEndTime(LocalDateTime.now());

			LogUtils.info("任务执行完毕，任务名称：{} 总共耗时：{} 秒", quartzJob.getJobName(), seconds);
		} catch (Exception e) {
			LogUtils.error("任务执行失败，任务名称：{}" + quartzJob.getJobName(), e);
			long seconds = sw.stop().elapsed(TimeUnit.SECONDS);
			quartzJobLog.setTime(seconds);
			quartzJobLog.setSuccess(false);
			quartzJobLog.setExceptionDetail(LogUtils.getStackTrace(e));
			quartzJobLog.setEndTime(LocalDateTime.now());

			//todo 更新数据状态
			// quartzJobModel.setPause(false);
			// quartzJobService.updateIsPause(quartzJobModel);
		} finally {
			//todo 添加日志
			// quartzLogService.save(log);
		}
		// 获取spring bean
		// QuartzLogService quartzLogService = ContextUtil.getBean(QuartzLogService.class, true);
		// QuartzJobService quartzJobService = ContextUtil.getBean(QuartzJobService.class, true);

		LogUtils.info(quartzJobLog.toString());
	}

	/**
	 * 执行spring bean方法
	 */
	protected void doExecute(QuartzJob scheduleJob) throws Exception {
		String methodName = scheduleJob.getMethodName();
		String params = scheduleJob.getParams();

		Object target = ContextUtils.getBean(scheduleJob.getBeanName(), true);
		if (Objects.isNull(target)) {
			throw new RuntimeException("未找到bean");
		}

		Method method;

		if (StringUtils.isNotBlank(params)) {
			method = target.getClass().getDeclaredMethod(methodName, String.class);
		} else {
			method = target.getClass().getDeclaredMethod(methodName);
		}

		ReflectionUtils.makeAccessible(method);
		if (StringUtils.isNotBlank(params)) {
			method.invoke(target, params);
		} else {
			method.invoke(target);
		}
	}
}
