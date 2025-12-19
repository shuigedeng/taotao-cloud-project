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

package com.taotao.cloud.job.worker.common.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

import lombok.Getter;

/**
 * ExecutorManager
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class ExecutorManager {

	@Getter
	private static ScheduledExecutorService heartbeatExecutor = null;
	@Getter
	private static ScheduledExecutorService healthReportExecutor = null;
	@Getter
	private static ScheduledExecutorService lightweightTaskStatusCheckExecutor = null;
	@Getter
	private static ExecutorService lightweightTaskExecutorService = null;

	public static void initExecutorManager() {

		final int availableProcessors = Runtime.getRuntime().availableProcessors();

		ThreadFactory heartbeatThreadFactory =
			new ThreadFactoryBuilder().setNameFormat("TtcJob-worker-heartbeat-%d").build();
		heartbeatExecutor = new ScheduledThreadPoolExecutor(3, heartbeatThreadFactory);

		ThreadFactory healthReportThreadFactory =
			new ThreadFactoryBuilder().setNameFormat("TtcJob-worker-healthReport-%d").build();
		healthReportExecutor = new ScheduledThreadPoolExecutor(3, healthReportThreadFactory);

		ThreadFactory lightTaskReportFactory =
			new ThreadFactoryBuilder()
				.setNameFormat("TtcJob-worker-light-task-status-check-%d")
				.build();
		lightweightTaskStatusCheckExecutor =
			new ScheduledThreadPoolExecutor(availableProcessors * 10, lightTaskReportFactory);

		ThreadFactory lightTaskExecuteFactory =
			new ThreadFactoryBuilder()
				.setNameFormat("TtcJob-worker-light-task-execute-%d")
				.build();
		lightweightTaskExecutorService =
			new ThreadPoolExecutor(
				availableProcessors * 10,
				availableProcessors * 10,
				120L,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(( 1024 * 2 ), true),
				lightTaskExecuteFactory,
				new ThreadPoolExecutor.AbortPolicy());
	}

	public static void shutdown() {
		heartbeatExecutor.shutdownNow();
		lightweightTaskExecutorService.shutdown();
		lightweightTaskStatusCheckExecutor.shutdown();
	}
}
