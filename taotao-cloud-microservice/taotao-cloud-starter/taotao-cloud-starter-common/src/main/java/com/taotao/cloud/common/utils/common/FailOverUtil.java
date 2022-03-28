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
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * FailOverUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:50:38
 */
public class FailOverUtil {

	/**
	 * name
	 */
	private final static String NAME = "补偿工具";

	/**
	 * invoke
	 *
	 * @param consumer consumer
	 * @param c1       c1
	 * @param <T>      T
	 * @return T
	 * @since 2021-09-02 20:50:56
	 */
	@SafeVarargs
	public static <T> T invoke(Consumer<Result<T>> consumer, Callable<T>... c1) {
		Result<T> result = new Result<>();
		try {
			int times = 0;
			for (int i = 0; i < c1.length; i++) {
				Callable<T> tCallable = c1[i];
				try {
					result.response = tCallable.call();
					result.success = true;
					break;
				} catch (Exception e) {
					result.throwable = e;
					if (i > 0) {
						LogUtil.error(NAME.concat("-失败-补偿次数 {}") + " error info {}", i,
							ExceptionUtil.getFullStackTrace(e));
					} else {
						LogUtil.error(
							ExceptionUtil.getFullStackTrace(e));
					}
				}
				times = i + 1;
			}
			if (result.success && times > 0) {
				LogUtil.info(
					PropertyUtil.getProperty("spring.application.name") + " {} 补偿成功, 补偿次数：{}",
					NAME,
					times);
			}
		} finally {
			consumer.accept(result);
		}
		return result.response;
	}

	/**
	 * Result
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:51:11
	 */
	public static class Result<T> {

		private boolean success = false;
		private Throwable throwable;
		private T response;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public Throwable getThrowable() {
			return throwable;
		}

		public void setThrowable(Throwable throwable) {
			this.throwable = throwable;
		}

		public T getResponse() {
			return response;
		}

		public void setResponse(T response) {
			this.response = response;
		}
	}
}
