package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.model.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.lang.annotation.Annotation;

/**
 * @author Huang Zhaoping
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
		if (ContextUtil.getBean(
			ReflectionUtil.tryClassForName("com.xxl.job.core.executor.impl.XxlJobSpringExecutor"),
			false) == null) {
			return null;
		}

		JobInfo data = new JobInfo();
		Class<?> aClass = ReflectionUtil.classForName(
			"com.xxl.job.core.handler.annotation.XxlJob");
		data.count = ContextUtil.getApplicationContext().getBeanNamesForAnnotation(
			(Class<? extends Annotation>) aClass).length;

		return data;
	}

	private static class JobInfo {

		@FieldReport(name = "taotao.cloud.health.collect.xxljob.count", desc = "xxljob任务数量")
		private Integer count;

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}
	}
}
