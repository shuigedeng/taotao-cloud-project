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
package com.taotao.cloud.canal.properties;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * CanalProperties 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:32:05
 */
@RefreshScope
@ConfigurationProperties(prefix = CanalProperties.PREFIX)
public class CanalProperties {

	public static final String PREFIX = "taotao.cloud.canal";

	private boolean enabled = false;

	// 配置信息
	private Map<String, Instance> instances = new LinkedHashMap<>();

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, Instance> getInstances() {
		return instances;
	}

	public void setInstances(Map<String, Instance> instances) {
		this.instances = instances;
	}

	/**
	 * canal 配置类
	 */
	public static class Instance {

		/**
		 * 是否是集群模式
		 */
		private boolean clusterEnabled;

		/**
		 * zookeeper 地址
		 */
		private Set<String> zookeeperAddress = new LinkedHashSet<>();

		/**
		 * canal 服务器地址，默认是本地的环回地址
		 */
		private String host = "127.1.1.1";

		/**
		 * canal 服务设置的端口，默认 11111
		 */
		private int port = 11111;

		/**
		 * 集群 设置的用户名
		 */
		private String userName = "";

		/**
		 * 集群 设置的密码
		 */
		private String password = "";

		/**
		 * 批量从 canal 服务器获取数据的最多数目
		 */
		private int batchSize = 1000;

		/**
		 * 是否有过滤规则
		 */
		private String filter;

		/**
		 * 当错误发生时，重试次数
		 */
		private int retryCount = 5;

		/**
		 * 信息捕获心跳时间
		 */
		private long acquireInterval = 1000;

		public Instance() {
		}

		public boolean getClusterEnabled() {
			return clusterEnabled;
		}

		public void setClusterEnabled(boolean clusterEnabled) {
			this.clusterEnabled = clusterEnabled;
		}

		public Set<String> getZookeeperAddress() {
			return zookeeperAddress;
		}

		public void setZookeeperAddress(Set<String> zookeeperAddress) {
			this.zookeeperAddress = zookeeperAddress;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public int getBatchSize() {
			return batchSize;
		}

		public void setBatchSize(int batchSize) {
			this.batchSize = batchSize;
		}

		public String getFilter() {
			return filter;
		}

		public void setFilter(String filter) {
			this.filter = filter;
		}

		public int getRetryCount() {
			return retryCount;
		}

		public void setRetryCount(int retryCount) {
			this.retryCount = retryCount;
		}

		public long getAcquireInterval() {
			return acquireInterval;
		}

		public void setAcquireInterval(long acquireInterval) {
			this.acquireInterval = acquireInterval;
		}
	}

}
