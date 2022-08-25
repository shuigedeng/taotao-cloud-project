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
package com.taotao.cloud.monitor.collect;

import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.monitor.annotation.FieldReport;
import com.taotao.cloud.monitor.properties.CollectTaskProperties;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * XxlJobCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:18:16
 */
public class XxlJobCollectTask extends AbstractCollectTask {

	private static final String XXL_JOB_SPRING_EXECUTOR_CLASS = "com.xxl.job.core.executor.impl.XxlJobSpringExecutor";
	private static final String XXL_JOB_CLASS = "com.xxl.job.core.handler.annotation.XxlJob";

	private static final String TASK_NAME = "taotao.cloud.health.collect.xxljob";

	private final CollectTaskProperties properties;

	public XxlJobCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getXxljobTimeSpan();
	}

	@Override
	public boolean getEnabled() {
		return properties.isXxljobEnabled();
	}

	@Override
	public String getDesc() {
		return this.getClass().getName();
	}

	@Override
	public String getName() {
		return TASK_NAME;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected CollectInfo getData() {
		try {
			Class<?> xxlJobSpringExecutorClass = ReflectionUtil.tryClassForName(XXL_JOB_SPRING_EXECUTOR_CLASS);
			Object xxlJobSpringExecutor = ContextUtil.getBean(xxlJobSpringExecutorClass, false);
			if (Objects.isNull(xxlJobSpringExecutor)) {
				return null;
			}

			JobInfo data = new JobInfo();
			Class<?> xxlJobClass = ReflectionUtil.classForName(XXL_JOB_CLASS);
			data.count = ContextUtil.getApplicationContext()
				.getBeanNamesForAnnotation((Class<? extends Annotation>) xxlJobClass).length;

			Object jobThreadRepository = ReflectionUtil.tryGetStaticFieldValue(xxlJobSpringExecutorClass,
				"jobThreadRepository", null);
			if (Objects.isNull(jobThreadRepository)) {
				data.jobThreadRepository = (Integer) ReflectionUtil.callMethod(jobThreadRepository,
					"size", null);
			}

			Object jobHandlerRepository = ReflectionUtil.tryGetStaticFieldValue(xxlJobSpringExecutorClass,
				"jobHandlerRepository", null);
			if (Objects.isNull(jobThreadRepository)) {
				data.jobHandlerRepository = (Integer) ReflectionUtil.callMethod(jobHandlerRepository,
					"size", null);
			}

			Object adminBizList = ReflectionUtil.tryGetStaticFieldValue(xxlJobSpringExecutorClass,
				"adminBizList", null);
			if (Objects.nonNull(adminBizList)) {
				data.adminBizList = (Integer) ReflectionUtil.callMethod(adminBizList,
					"size", null);
			}

			data.logRetentionDays = ReflectionUtil.tryGetValue(xxlJobSpringExecutorClass, "logRetentionDays");

			return data;
		} catch (Exception e) {

		}
		return null;
	}

	private static class JobInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".count", desc = "xxljob任务数量")
		private Integer count = 0;

		@FieldReport(name = TASK_NAME + ".job.thread.repository", desc = "xxljob thread repository数量")
		private Integer jobThreadRepository = 0;

		@FieldReport(name = TASK_NAME + ".job.handler.repository", desc = "xxljob handler repository 数量")
		private Integer jobHandlerRepository = 0;

		@FieldReport(name = TASK_NAME + ".admin.biz.list", desc = "xxljob admin biz 数量")
		private Integer adminBizList = 0;

		@FieldReport(name = TASK_NAME + ".log.retention.days", desc = "xxljob日志保留天数")
		private Integer logRetentionDays = 0;
	}
}
