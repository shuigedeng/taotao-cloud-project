/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.executor;

import com.taotao.cloud.sms.enums.RejectPolicy;
import com.taotao.cloud.sms.properties.SmsAsyncProperties;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.lang.Nullable;

/**
 * 抽象发送异步处理线程池
 *
 * @author shuigedeng
 */
public abstract class AbstractSendAsyncThreadPoolExecutor implements SendAsyncThreadPoolExecutor {

	protected final SmsAsyncProperties config;

	public AbstractSendAsyncThreadPoolExecutor(SmsAsyncProperties config) {
		this.config = config;
	}

	/**
	 * 根据拒绝策略构造拒绝处理程序
	 *
	 * @param type 拒绝策略
	 * @return 拒绝处理程序
	 */
	protected static RejectedExecutionHandler buildRejectedExecutionHandler(
		@Nullable RejectPolicy type) {
		if (type == null) {
			return new ThreadPoolExecutor.AbortPolicy();
		}
		switch (type) {
			case Caller:
				return new ThreadPoolExecutor.CallerRunsPolicy();
			case Discard:
				return new ThreadPoolExecutor.DiscardPolicy();
			case DiscardOldest:
				return new ThreadPoolExecutor.DiscardOldestPolicy();
			default:
				return new ThreadPoolExecutor.AbortPolicy();
		}
	}

	/**
	 * 提交异步任务
	 *
	 * @param command 待执行任务
	 */
	@Override
	public final void submit(Runnable command) {
		submit0(command);
	}

	/**
	 * 提交异步任务
	 *
	 * @param command 待执行任务
	 */
	protected abstract void submit0(Runnable command);

	/**
	 * 默认线程工厂
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:48:39
	 */
	public static class DefaultThreadFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		public DefaultThreadFactory() {
			this.group = Thread.currentThread().getThreadGroup();

			this.namePrefix =
				"taotao-cloud-sms-async-executor-pool-" + poolNumber.getAndIncrement();
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r,
				namePrefix + "-thread-" + threadNumber.getAndIncrement(),
				0);

			// 一定要设置未非守护线程
			t.setDaemon(false);

			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}

			return t;
		}

		public ThreadGroup getGroup() {
			return group;
		}

		public AtomicInteger getThreadNumber() {
			return threadNumber;
		}

		public String getNamePrefix() {
			return namePrefix;
		}
	}
}
