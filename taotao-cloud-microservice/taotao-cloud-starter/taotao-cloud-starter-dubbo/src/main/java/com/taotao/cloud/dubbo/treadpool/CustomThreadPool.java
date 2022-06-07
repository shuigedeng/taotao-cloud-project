package com.taotao.cloud.dubbo.treadpool;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.threadpool.ThreadPool;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class CustomThreadPool implements ThreadPool {
	@Override
	public Executor getExecutor(URL url) {
		LogUtil.info("CustomThreadPool getExecutor activate ------------------------------");
		LogUtil.info(url.toFullString());

		AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor executor = new AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(10000);
		executor.setKeepAliveSeconds(300);
		executor.setThreadNamePrefix("taotao-cloud-dubbo-executor");

		executor.setTaskDecorator(new AsyncAutoConfiguration.AsyncTaskDecorator());

		/*
		 rejection-policy：当pool已经达到max size的时候，如何处理新任务
		 CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		 */
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;

	}
}
