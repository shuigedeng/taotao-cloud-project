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
 * AsyncProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:44:31
 */
@RefreshScope
@ConfigurationProperties(prefix = AsyncProperties.PREFIX)
public class AsyncProperties {

	public static final String PREFIX = "taotao.cloud.core.async";

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
	 * 是否允许核心线程超时
	 * <p>
	 * 默认：false
	 */
	private boolean allowCoreThreadTimeOut = false;

	/**
	 * 应用关闭时-是否等待未完成任务继续执行，再继续销毁其他的Bean
	 * <p>
	 * 默认：true
	 */
	private boolean waitForTasksToCompleteOnShutdown = true;

	/**
	 * 依赖 {@linkplain #waitForTasksToCompleteOnShutdown} 为true
	 * <p>
	 * 应用关闭时-继续等待时间（单位：秒）
	 * <p>
	 * 如果超过这个时间还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
	 * <p>
	 * 默认：5
	 */
	private Integer awaitTerminationSeconds = 5;

	/**
	 * 线程池前缀
	 */
	private String threadNamePrefix = "taotao-cloud-async-executor";

	private boolean checkHealth = true;

	/**
	 * ServletAsyncContext阻塞超时时长 setAttribute 时所使用的固定变量名
	 */
	public static final String SERVLET_ASYNC_CONTEXT_TIMEOUT_MILLIS = "servletAsyncContextTimeoutMillis";

	/**
	 * 是否开启 ServletAsyncContext
	 * <p>
	 * 用于阻塞父线程 Servlet 的关闭（调用 destroy() 方法），导致子线程获取的上下文为空
	 * </p>
	 * 默认：false
	 */
	private boolean enableServletAsyncContext = false;

	/**
	 * ServletAsyncContext阻塞超时时长（单位：毫秒），异步上下文最长生命周期（最大阻塞父线程多久）
	 * <p>
	 * 单个方法的阻塞超时时间需要更长时，可以使用
	 * {@code
	 * ServletUtils.getRequest().setAttribute(AsyncProperties.SERVLET_ASYNC_CONTEXT_TIMEOUT_MILLIS,
	 * 2000)}，为单个异步方法设置单独的超时时长。
	 * </p>
	 * 默认：600
	 */
	private Long servletAsyncContextTimeoutMillis = 600L;

	/**
	 * 是否开启异步
	 */
	private boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public boolean isEnableServletAsyncContext() {
		return enableServletAsyncContext;
	}

	public void setEnableServletAsyncContext(boolean enableServletAsyncContext) {
		this.enableServletAsyncContext = enableServletAsyncContext;
	}

	public Long getServletAsyncContextTimeoutMillis() {
		return servletAsyncContextTimeoutMillis;
	}

	public void setServletAsyncContextTimeoutMillis(Long servletAsyncContextTimeoutMillis) {
		this.servletAsyncContextTimeoutMillis = servletAsyncContextTimeoutMillis;
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

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}


	public boolean isCheckHealth() {
		return checkHealth;
	}

	public void setCheckHealth(boolean checkHealth) {
		this.checkHealth = checkHealth;
	}


	public boolean isAllowCoreThreadTimeOut() {
		return allowCoreThreadTimeOut;
	}

	public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
	}

	public boolean isWaitForTasksToCompleteOnShutdown() {
		return waitForTasksToCompleteOnShutdown;
	}

	public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
	}

	public Integer getAwaitTerminationSeconds() {
		return awaitTerminationSeconds;
	}

	public void setAwaitTerminationSeconds(Integer awaitTerminationSeconds) {
		this.awaitTerminationSeconds = awaitTerminationSeconds;
	}
}
