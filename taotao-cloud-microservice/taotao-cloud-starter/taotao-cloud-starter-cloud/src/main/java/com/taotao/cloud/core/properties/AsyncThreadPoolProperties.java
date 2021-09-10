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
package com.taotao.cloud.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * AsyncProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:44:31
 */
@RefreshScope
@ConfigurationProperties(prefix = AsyncThreadPoolProperties.PREFIX)
public class AsyncThreadPoolProperties {

	public static final String PREFIX = "taotao.cloud.core.threadpool.async";

	private boolean enabled = true;

	/**
	 * 异步核心线程数，默认：10
	 */
	private int corePoolSize = 10;

	/**
	 * 异步最大线程数，默认：50
	 */
	private int maxPoolSiz = 50;

	/**
	 * 队列容量，默认：10000
	 */
	private int queueCapacity = 10000;

	/**
	 * 线程存活时间，默认：300
	 */
	private int keepAliveSeconds = 300;

	/**
	 * 线程池前缀
	 */
	private String threadNamePrefix = "taotao-cloud-async-executor";

	public AsyncThreadPoolProperties() {
	}

	public AsyncThreadPoolProperties(boolean enabled, int corePoolSize, int maxPoolSiz,
		int queueCapacity, int keepAliveSeconds, String threadNamePrefix) {
		this.enabled = enabled;
		this.corePoolSize = corePoolSize;
		this.maxPoolSiz = maxPoolSiz;
		this.queueCapacity = queueCapacity;
		this.keepAliveSeconds = keepAliveSeconds;
		this.threadNamePrefix = threadNamePrefix;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSiz() {
		return maxPoolSiz;
	}

	public void setMaxPoolSiz(int maxPoolSiz) {
		this.maxPoolSiz = maxPoolSiz;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public String getThreadNamePrefix() {
		return threadNamePrefix;
	}

	public void setThreadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}
}
