package com.taotao.cloud.common.utils.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.util.StringUtils;

/**
 * ThreadFactoryCreator
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:28:58
 */
public final class ThreadFactoryCreator {

	public static ThreadFactory create(String threadName) {
		if (!StringUtils.hasText(threadName)) {
			throw new IllegalArgumentException("argument [threadName] must not be blank");
		}
		return new NamedWithIdThreadFactory(threadName);
	}

	private static final class NamedWithIdThreadFactory implements ThreadFactory {

		private final AtomicInteger threadId = new AtomicInteger(1);

		private final String namePrefix;

		private NamedWithIdThreadFactory(String namePrefix) {
			this.namePrefix = namePrefix;
		}

		@Override
		public Thread newThread(Runnable command) {
			Thread thread = new Thread(command);
			thread.setName(this.namePrefix + "-" + this.threadId.getAndAdd(1));
			return thread;
		}
	}

}
