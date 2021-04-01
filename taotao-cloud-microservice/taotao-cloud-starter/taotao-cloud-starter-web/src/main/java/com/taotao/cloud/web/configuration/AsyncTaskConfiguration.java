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
package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.async.AsyncTaskProperties;
import com.taotao.cloud.web.async.AsyncThreadPoolTaskExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestContextListener;

/**
 * 默认异步任务配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 09:12
 */
@EnableAsync(proxyTargetClass = true)
public class AsyncTaskConfiguration {

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	public TaskExecutor taskExecutor(AsyncTaskProperties asyncTaskProperties) {
		ThreadPoolTaskExecutor executor = new AsyncThreadPoolTaskExecutor();
		executor.setCorePoolSize(asyncTaskProperties.getCorePoolSize());
		executor.setMaxPoolSize(asyncTaskProperties.getMaxPoolSiz());
		executor.setQueueCapacity(asyncTaskProperties.getQueueCapacity());
		executor.setThreadNamePrefix(asyncTaskProperties.getThreadNamePrefix());

		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
