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
package com.taotao.cloud.dubbo.treadpool;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.threadpool.support.fixed.FixedThreadPool;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 看着池
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-08 10:45:52
 */
public class WatchingPool extends FixedThreadPool implements Runnable {

	/**
	 * 线程池预警值【可以根据实际情况动态调整大小】
	 */
	private static final double ALARM_PERCENT = 0.75f;

	private final Map<URL, ThreadPoolExecutor> theadPoolMap = new ConcurrentHashMap<>();

	public WatchingPool() {
		// 创建一个定时任务，每3秒执行一次【可以根据实际情况动态调整参数】
		new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			@Override
			public Thread newThread(@NotNull Runnable r) {
				return new Thread(Thread.currentThread().getThreadGroup(), r,
					"taotao-cloud-dubbo-watchingPool");
			}
		}).scheduleWithFixedDelay(this, 1, 3, TimeUnit.SECONDS);
	}

	@Override
	public Executor getExecutor(URL url) {
		// 重写父类getExecutor, 如果executor是ThreadPoolExecutor，则放入theadPoolMap中
		Executor executor = super.getExecutor(url);
		if (executor instanceof ThreadPoolExecutor) {
			theadPoolMap.put(url, (ThreadPoolExecutor) executor);
		}

		return executor;
	}

	@Override
	public void run() {
		for (Map.Entry<URL, ThreadPoolExecutor> entry : theadPoolMap.entrySet()) {
			URL url = entry.getKey();
			ThreadPoolExecutor threadPoolExecutor = entry.getValue();
			// 获取正在活动的线程数
			int activeCount = threadPoolExecutor.getActiveCount();
			// 获取总的线程数  （继承的FixedThreadPool ， 所以这里获取核心的线程数就是总的线程数）
			int corePoolSize = threadPoolExecutor.getCorePoolSize();

			double percent = activeCount / (corePoolSize * 1.0);

			LogUtil.info("线程池状态：{}/{},: {}%", activeCount, corePoolSize, percent * 100);
			if (percent > ALARM_PERCENT) {
				LogUtil.error("超出警戒线 : host:{}, 当前使用量 {}%, URL:{}", url.getHost(), percent * 100, url);
			}
		}
	}
}
