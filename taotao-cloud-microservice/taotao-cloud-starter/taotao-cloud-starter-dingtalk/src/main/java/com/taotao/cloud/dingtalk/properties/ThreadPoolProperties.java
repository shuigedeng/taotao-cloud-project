/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * DINGTALK线程池参数配置-用于异步处理
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:24:35
 */
@RefreshScope
@ConfigurationProperties(prefix = ThreadPoolProperties.PREFIX)
public class ThreadPoolProperties {

	public static final String PREFIX = "taotao.cloud.dinger.threadpool";

	private static final int DEFAULT_CORE_SIZE = Runtime.getRuntime().availableProcessors() + 1;

	private boolean enabled = true;

	/**
	 * 线程池维护线程的最小数量, 选填
	 */
	private int coreSize = DEFAULT_CORE_SIZE;
	/**
	 * 线程池维护线程的最大数量, 选填
	 */
	private int maxSize = DEFAULT_CORE_SIZE * 2;
	/**
	 * 空闲线程的存活时间, 选填
	 */
	private int keepAliveSeconds = 60;
	/**
	 * 持有等待执行的任务队列, 选填
	 */
	private int queueCapacity = 10;
	/**
	 * 线程名称前缀, 选填
	 */
	private String threadNamePrefix = "taotao-cloud-dinger-threadpool-";

	public int getCoreSize() {
		return coreSize;
	}

	public void setCoreSize(int coreSize) {
		this.coreSize = coreSize;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
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

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
