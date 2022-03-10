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
package com.taotao.cloud.common.utils.common;

import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.List;
import java.util.function.Consumer;

/**
 * RetryUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 14:58:57
 */
public class RetryUtil {

	private RetryUtil() {
	}

	/**
	 * name
	 */
	private final static String name = "重试工具";

	/**
	 * 重试调度方法
	 *
	 * @param dataSupplier     返回数据方法执行体
	 * @param exceptionCaught  出错异常处理(包括第一次执行和重试错误)
	 * @param retryCount       重试次数
	 * @param sleepTime        重试间隔睡眠时间(注意：阻塞当前线程)
	 * @param expectExceptions 期待异常(抛出符合相应异常时候重试),空或者空容器默认进行重试
	 * @return R
	 * @author shuigedeng
	 * @since 2021-09-02 14:59:11
	 */
	public static <R> R invoke(Supplier<R> dataSupplier, Consumer<Throwable> exceptionCaught,
		int retryCount, long sleepTime, List<Class<? extends Throwable>> expectExceptions) {
		Throwable ex;

		try {
			// 产生数据
			return dataSupplier == null ? null : dataSupplier.get();
		} catch (Throwable throwable) {
			// 捕获异常
			catchException(exceptionCaught, throwable);
			ex = throwable;
		}

		if (expectExceptions != null && !expectExceptions.isEmpty()) {
			// 校验异常是否匹配期待异常
			Class<? extends Throwable> exClass = ex.getClass();
			boolean match = expectExceptions.stream().anyMatch(clazz -> clazz == exClass);
			if (!match) {
				return null;
			}
		}

		// 匹配期待异常或者允许任何异常重试
		for (int i = 0; i < retryCount; i++) {
			try {
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
				return dataSupplier.get();
			} catch (InterruptedException e) {
				LogUtil.error("thread interrupted !! break retry,cause:" + e.getMessage());
				// 恢复中断信号
				Thread.currentThread().interrupt();
				// 线程中断直接退出重试
				break;
			} catch (Throwable throwable) {
				catchException(exceptionCaught, throwable);
			}
		}

		return null;
	}

	/**
	 * catchException
	 *
	 * @param exceptionCaught exceptionCaught
	 * @param throwable       throwable
	 * @author shuigedeng
	 * @since 2021-09-02 17:04:42
	 */
	private static void catchException(Consumer<Throwable> exceptionCaught, Throwable throwable) {
		try {
			if (exceptionCaught != null) {
				exceptionCaught.accept(throwable);
			}
		} catch (Throwable e) {
			LogUtil.error(e,
				"retry exception caught throw error:" + ExceptionUtil.getFullStackTrace(e));
		}
	}

	/**
	 * Supplier
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 14:59:53
	 */
	@FunctionalInterface
	public interface Supplier<T> {

		T get() throws Exception;
	}
}
