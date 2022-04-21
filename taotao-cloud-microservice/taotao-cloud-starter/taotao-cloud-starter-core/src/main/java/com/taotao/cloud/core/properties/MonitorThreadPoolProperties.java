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
package com.taotao.cloud.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * ThreadPoolProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:42:36
 */
@RefreshScope
@ConfigurationProperties(prefix = MonitorThreadPoolProperties.PREFIX)
public class MonitorThreadPoolProperties {

	public static final String PREFIX = "taotao.cloud.core.monitor.threadpool";

	/**
	 * 监控核心线程数，默认：10
	 */
	private int corePoolSize = 10;

	/**
	 * 最大线程数，默认：50
	 */
	private int maximumPoolSize = 50;

	/**
	 * 线程存活时间 单位秒，默认：60
	 */
	private long keepAliveTime = 60;

	/**
	 * 线程池前缀
	 */
	private String threadNamePrefix = "taotao-cloud-monitor-executor";

	private boolean checkHealth = true;

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public String getThreadNamePrefix() {
		return threadNamePrefix;
	}

	public void setThreadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix;
	}

	public boolean isCheckHealth() {
		return checkHealth;
	}

	public void setCheckHealth(boolean checkHealth) {
		this.checkHealth = checkHealth;
	}
}
