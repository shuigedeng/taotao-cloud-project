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
package com.taotao.cloud.core.configuration;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolFactory;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.decorator.ContextDecorator;
import com.taotao.cloud.core.properties.AsyncProperties;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 异步任务配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@AutoConfiguration
public class AsyncThreadPoolAutoConfiguration implements InitializingBean{

	@Autowired
	private AsyncProperties asyncProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(AsyncThreadPoolAutoConfiguration.class, StarterName.CORE_STARTER);
	}

	@Bean("asyncThreadPoolTaskExecutor")
	public AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor() {
		AsyncThreadPoolTaskExecutor executor = new AsyncThreadPoolTaskExecutor();

		// 线程池名的前缀
		executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());
		// 核心线程数
		executor.setCorePoolSize(asyncProperties.getCorePoolSize());
		// 最大线程数
		executor.setMaxPoolSize(asyncProperties.getMaxPoolSiz());
		// 允许线程的空闲时间
		executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());
		// 任务队列容量（阻塞队列）
		executor.setQueueCapacity(asyncProperties.getQueueCapacity());
		// 是否允许核心线程超时
		executor.setAllowCoreThreadTimeOut(asyncProperties.isAllowCoreThreadTimeOut());
		// 应用关闭时-是否等待未完成任务继续执行，再继续销毁其他的Bean
		executor.setWaitForTasksToCompleteOnShutdown(asyncProperties.isWaitForTasksToCompleteOnShutdown());
		// 应用关闭时-继续等待时间（单位：秒）
		executor.setAwaitTerminationSeconds(asyncProperties.getAwaitTerminationSeconds());
		// ThreadFactory
		executor.setThreadFactory(new AsyncThreadPoolFactory(asyncProperties, executor));
		// 异步线程上下文装饰器
		executor.setTaskDecorator(new ContextDecorator(asyncProperties));
		/*
		 线程池拒绝策略
		 rejection-policy：当pool已经达到max size的时候，如何处理新任务
		 CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		 */
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		executor.initialize();

		return executor;
	}
}
