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

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 可疑API采集
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:34:39
 */
public class DoubtApiCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.doubtApi";

	private final CollectTaskProperties properties;

	public DoubtApiCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getDoubtApiTimeSpan();
	}

	@Override
	public boolean getEnabled() {
		return properties.isDoubtApiEnabled();
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
	protected CollectInfo getData() {
		try {
			DoubtApiInfo info = new DoubtApiInfo();
			Collector collector = Collector.getCollector();
			if (Objects.nonNull(collector)) {
				Object doubtApiInfo = collector.value("taotao.cloud.health.doubtapi.info").get();

				//Map<String, DoubtApiInterceptor.DoubtApiInfo> map = (Map<String, DoubtApiInterceptor.DoubtApiInfo>) doubtApiInfo;
				//if (map != null && map.size() > 0) {
				//	DoubtApiInterceptor.DoubtApiInfo[] copy = map.values().toArray(new DoubtApiInterceptor.DoubtApiInfo[map.values().size()]);
				//	Arrays.sort(copy);
				//
				//	int detailLen = Math.min(copy.length, 5);
				//	StringBuilder sb = new StringBuilder();
				//
				//	for (int i = 0; i < detailLen; i++) {
				//		DoubtApiInterceptor.DoubtApiInfo o = copy[i];
				//
				//		long avg;
				//		if (o.getCount() > 0) {
				//			avg = o.getTotalIncreMem() / 1024 / 1024 / o.getCount();
				//		} else {
				//			avg = o.getTotalIncreMem() / 1024 / 1024;
				//		}
				//		sb.append(String.format("url:%s,方法:%s,平均内存增量:%s(M),调用次数:%s\r\n",
				//			o.getUri(),
				//			o.getMethod(),
				//			avg, o.getCount()));
				//	}
				//	info.detail = sb.toString();
				//}
				return info;
			}
		} catch (Exception exp) {
			LogUtil.error(exp);
		}
		return null;
	}

	public static class DoubtApiInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".detail", desc = "可疑内存增长api分析报告")
		String detail = "";
	}

}
