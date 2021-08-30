package com.taotao.cloud.health.collect;

import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.health.model.FieldReport;
import com.taotao.cloud.health.filter.DoubtApiInterceptor;
import com.taotao.cloud.health.filter.DoubtApiInterceptor.DoubtApiInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.Arrays;
import java.util.Map;

/**
 * 收集使用内存最大的前五个接口及内存使用情况
 *
 * @author Robin.Wang
 * @version 1.0.0
 * @date 2019-10-23
 */
public class DoubtApiCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;

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
		return "可疑API采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.doubtapi.info";
	}

	@Override
	protected Object getData() {

		ApiUsedMemoryTopInfo info = new ApiUsedMemoryTopInfo();
		try {
			Map<String, DoubtApiInterceptor.DoubtApiInfo> map = (Map<String, DoubtApiInterceptor.DoubtApiInfo>) Collector.DEFAULT.value(
				"taotao.cloud.health.doubtapi.info").get();
			if (map != null && map.size() > 0) {
				DoubtApiInfo[] copy = map.values()
					.toArray(new DoubtApiInfo[map.values().size()]);
				Arrays.sort(copy);
				int detailLen = Math.min(copy.length, 5);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < detailLen; i++) {
					DoubtApiInfo o = copy[i];

					long avg;
					if (o.getCount() > 0) {
						avg = o.getTotalIncreMem() / 1024 / 1024 / o.getCount();
					} else {
						avg = o.getTotalIncreMem() / 1024 / 1024;
					}
					sb.append(String.format("url:%s,方法:%s,平均内存增量:%s(M),调用次数:%s\r\n", o.getUri(),
						o.getMethod(),
						avg, o.getCount()));
				}
				info.detail = sb.toString();
			}
		} catch (Exception exp) {
		}
		return info;

	}

	public static class ApiUsedMemoryTopInfo {

		@FieldReport(name = "taotao.cloud.health.collect.doubt.api.detail", desc = "可疑内存增长api分析报告")
		String detail;

		public ApiUsedMemoryTopInfo() {
		}

		public ApiUsedMemoryTopInfo(String detail) {
			this.detail = detail;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}
	}

}
