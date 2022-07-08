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
package com.taotao.cloud.logger.logRecord.thread;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.logRecord.configuration.LogRecordProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志记录线程池
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:38:37
 */
@Component
@EnableConfigurationProperties(value = LogRecordProperties.class)
public class LogRecordThreadPool {

	/**
	 * 线程工厂
	 */
	private static final ThreadFactory THREAD_FACTORY = new CustomizableThreadFactory("log-record-");

	/**
	 * 日志记录池执行人
	 */
	private final ExecutorService LOG_RECORD_POOL_EXECUTOR;

	/**
	 * 日志记录线程池
	 *
	 * @param logRecordProperties 日志记录属性
	 * @since 2022-04-26 14:38:37
	 */
	public LogRecordThreadPool(LogRecordProperties logRecordProperties){
        LogUtil.info("LogRecordThreadPool init poolSize [{}]", logRecordProperties.getPoolSize());
        int poolSize = logRecordProperties.getPoolSize();
        this.LOG_RECORD_POOL_EXECUTOR = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.CallerRunsPolicy());
    }

	/**
	 * 日志记录池执行人
	 *
	 * @return {@link ExecutorService }
	 * @since 2022-04-26 14:38:37
	 */
	public ExecutorService getLogRecordPoolExecutor() {
        return LOG_RECORD_POOL_EXECUTOR;
    }
}
