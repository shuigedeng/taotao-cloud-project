/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.annotation.Annotation;

/**
 * XxlJobCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:18:16
 */
public class XxlJobCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;

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
		return "定时任务性能采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.xxljob.info";
	}

	@Override
	protected Object getData() {
		try {
			if (ContextUtil.getBean(
				ReflectionUtil.tryClassForName(
					"com.xxl.job.core.executor.impl.XxlJobSpringExecutor"),
				false) == null) {
				return null;
			}

			JobInfo data = new JobInfo();
			Class<?> aClass = ReflectionUtil.classForName(
				"com.xxl.job.core.handler.annotation.XxlJob");
			data.count = ContextUtil.getApplicationContext().getBeanNamesForAnnotation(
				(Class<? extends Annotation>) aClass).length;

			return data;
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}

	private static class JobInfo {

		@FieldReport(name = "taotao.cloud.health.collect.xxljob.count", desc = "xxljob任务数量")
		private Integer count;
	}
}
