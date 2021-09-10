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
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.http.HttpClientManager;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

/**
 * HTTP连接池性能采集
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:44:20
 */
public class HttpPoolCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;

	public HttpPoolCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getHttpPoolTimeSpan();
	}

	@Override
	public boolean getEnabled() {
		return properties.isHttpPoolEnabled();
	}

	@Override
	public String getDesc() {
		return "HttpPoolCollectTask";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.httppool";
	}

	@Override
	protected Object getData() {
		try {
			HttpClientManager httpClientManager = ContextUtil.getBean(HttpClientManager.class,
				false);

			ConcurrentHashMap<String, DefaultHttpClient> pool = httpClientManager.getPool();
			if (pool == null || pool.isEmpty()) {
				return null;
			}

			HttpPoolInfo data = new HttpPoolInfo();
			StringBuilder detail = new StringBuilder();
			pool.forEach((id, client) -> {
				PoolingHttpClientConnectionManager manager = ReflectionUtil.getFieldValue(client,
					"manager");
				PoolStats stats = manager.getTotalStats();
				data.availableCount += stats.getAvailable();
				data.pendingCount += stats.getPending();
				data.leasedCount += stats.getLeased();

				detail.append(String.format("[Client连接池:%s]\r\n", id));
				detail.append(String.format("路由数:%s\r\n", manager.getRoutes()));
				detail.append(String.format("路由连接数:%s\r\n", manager.getDefaultMaxPerRoute()));
				detail.append(String.format("最大的连接数:%s\r\n", manager.getMaxTotal()));
				detail.append(String.format("可用的连接数:%s\r\n", stats.getAvailable()));
				detail.append(String.format("等待的连接数:%s\r\n", stats.getPending()));
				detail.append(String.format("使用中的连接数:%s\r\n", stats.getLeased()));
			});
			data.poolDetail = detail.toString();
			return data;
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}

	private static class HttpPoolInfo {

		@FieldReport(name = "taotao.cloud.health.collect.httpPool.available", desc = "HttpPool可用的连接数")
		private Integer availableCount = 0;
		@FieldReport(name = "taotao.cloud.health.collect.httpPool.pending", desc = "HttpPool等待的连接数")
		private Integer pendingCount = 0;
		@FieldReport(name = "taotao.cloud.health.collect.httpPool.leased", desc = "HttpPool使用中的连接数")
		private Integer leasedCount = 0;
		@FieldReport(name = "taotao.cloud.health.collect.httpPool.detail", desc = "HttpPool详情")
		private String poolDetail;
	}
}
