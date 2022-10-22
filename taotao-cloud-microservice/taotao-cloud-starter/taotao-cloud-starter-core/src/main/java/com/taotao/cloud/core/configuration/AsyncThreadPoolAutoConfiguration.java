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

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolFactory;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.decorator.ContextDecorator;
import com.taotao.cloud.core.properties.AsyncProperties;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 异步任务配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@AutoConfiguration
public class AsyncThreadPoolAutoConfiguration implements InitializingBean {

	@Autowired
	private AsyncProperties asyncProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(AsyncThreadPoolAutoConfiguration.class, StarterName.CORE_STARTER);
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
		executor.setWaitForTasksToCompleteOnShutdown(
			asyncProperties.isWaitForTasksToCompleteOnShutdown());
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
		 // AbortPolicy: 直接抛出java.util.concurrent.RejectedExecutionException异常
		// CallerRunsPolicy: 主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度
		// DiscardOldestPolicy: 抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行
		// DiscardPolicy: 抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行
		 */
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		executor.initialize();

		return executor;
	}

	@Bean("prometheusThreadPoolTaskScheduler")
	public ThreadPoolTaskScheduler prometheusThreadPoolTaskScheduler() {
		ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
		executor.setPoolSize(5);
		executor.setThreadGroupName("taotao-cloud-prometheus-task-scheduler");
		executor.setAwaitTerminationSeconds(60);
		executor.setWaitForTasksToCompleteOnShutdown(true);

		return executor;
	}
}
