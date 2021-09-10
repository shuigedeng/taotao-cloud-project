package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.http.HttpClientManager;
import com.taotao.cloud.health.model.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

/**
 * HTTP连接池性能采集
 *
 * @author Huang Zhaoping
 */
public class HttpPoolCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;
	private HttpClientManager httpClientManager;

	public HttpPoolCollectTask(HttpClientManager httpClientManager,
		CollectTaskProperties properties) {
		this.httpClientManager = httpClientManager;
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
		return "Http连接池性能采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.httpPool.info";
	}

	@Override
	protected Object getData() {
		ConcurrentHashMap<String, DefaultHttpClient> pool = httpClientManager.getPool();
		if (pool == null && pool.isEmpty()) {
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

		public HttpPoolInfo() {
		}

		public HttpPoolInfo(Integer availableCount, Integer pendingCount, Integer leasedCount,
			String poolDetail) {
			this.availableCount = availableCount;
			this.pendingCount = pendingCount;
			this.leasedCount = leasedCount;
			this.poolDetail = poolDetail;
		}

		public Integer getAvailableCount() {
			return availableCount;
		}

		public void setAvailableCount(Integer availableCount) {
			this.availableCount = availableCount;
		}

		public Integer getPendingCount() {
			return pendingCount;
		}

		public void setPendingCount(Integer pendingCount) {
			this.pendingCount = pendingCount;
		}

		public Integer getLeasedCount() {
			return leasedCount;
		}

		public void setLeasedCount(Integer leasedCount) {
			this.leasedCount = leasedCount;
		}

		public String getPoolDetail() {
			return poolDetail;
		}

		public void setPoolDetail(String poolDetail) {
			this.poolDetail = poolDetail;
		}
	}
}
