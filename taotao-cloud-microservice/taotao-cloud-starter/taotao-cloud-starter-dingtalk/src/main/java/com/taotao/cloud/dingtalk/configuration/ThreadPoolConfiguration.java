/*
 * Copyright ©2015-2021 Jaemon. All Rights Reserved.
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
package com.taotao.cloud.dingtalk.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.dingtalk.constant.DingerConstant;
import com.taotao.cloud.dingtalk.properties.ThreadPoolProperties;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * DINGTALK线程池配置类
 *
 */
@Configuration
@ConditionalOnProperty(prefix = ThreadPoolProperties.PREFIX, name = "enabled", havingValue = "true")
//@ConditionalOnBean(DingerRobot.class)
@ConditionalOnMissingBean(name = DingerConstant.DINGER_EXECUTOR)
public class ThreadPoolConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ThreadPoolConfiguration.class, StarterName.DINGTALK_STARTER);
	}

	@Bean(name = DingerConstant.DINGER_EXECUTOR)
	public Executor dingTalkExecutor(ThreadPoolProperties threadPoolProperties) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心线程数
		executor.setCorePoolSize(threadPoolProperties.getCoreSize());
		// 最大线程数
		executor.setMaxPoolSize(threadPoolProperties.getMaxSize());
		// 线程最大空闲时间
		executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
		// 队列大小
		executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
		// 指定用于新创建的线程名称的前缀
		executor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());

		// 使用自定义拒绝策略, 直接抛出异常
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		// 等待任务完成时再关闭线程池--表明等待所有线程执行完
		executor.setWaitForTasksToCompleteOnShutdown(true);

		// 初始化
		executor.initialize();
		return executor;
	}

}
