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
package com.taotao.cloud.web.properties;

import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 异步任务Properties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/7/24 08:22
 */
@RefreshScope
@ConfigurationProperties(prefix = AsyncTaskProperties.PREFIX)
public class AsyncTaskProperties {

	public static final String ENABLED = "enabled";
	public static final String TRUE = "true";

	public static final String PREFIX = "taotao.cloud.web.async.task";

	/**
	 * 线程池维护线程的最小数量
	 */
	private int corePoolSize = 10;

	/**
	 * 线程池维护线程的最大数量
	 */
	private int maxPoolSiz = 200;

	/**
	 * 队列最大长度
	 */
	private int queueCapacity = 300;

	/**
	 * 线程池前缀
	 */
	private String threadNamePrefix = "taotao-cloud-executor-";

	public AsyncTaskProperties() {
	}

	public AsyncTaskProperties(int corePoolSize, int maxPoolSiz, int queueCapacity,
		String threadNamePrefix) {
		this.corePoolSize = corePoolSize;
		this.maxPoolSiz = maxPoolSiz;
		this.queueCapacity = queueCapacity;
		this.threadNamePrefix = threadNamePrefix;
	}

	@Override
	public String toString() {
		return "AsyncTaskProperties{" +
			"corePoolSize=" + corePoolSize +
			", maxPoolSiz=" + maxPoolSiz +
			", queueCapacity=" + queueCapacity +
			", threadNamePrefix='" + threadNamePrefix + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AsyncTaskProperties that = (AsyncTaskProperties) o;
		return corePoolSize == that.corePoolSize && maxPoolSiz == that.maxPoolSiz
			&& queueCapacity == that.queueCapacity && Objects.equals(threadNamePrefix,
			that.threadNamePrefix);
	}

	@Override
	public int hashCode() {
		return Objects.hash(corePoolSize, maxPoolSiz, queueCapacity, threadNamePrefix);
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
}
