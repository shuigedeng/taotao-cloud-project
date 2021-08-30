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
package com.taotao.cloud.core.utils;

import static com.taotao.cloud.core.properties.CoreProperties.SpringApplicationName;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.ExceptionUtil;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * 错误补偿工具类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:23
 */
public class FailOverUtil {

	private final static String name = "补偿工具";

	/**
	 * 错误补偿工具类
	 *
	 * @param consumer consumer
	 * @param c1       c1
	 * @author shuigedeng
	 * @since 2021/6/22 17:41
	 */
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
						LogUtil.error(FailOverUtil.class, StarterName.CLOUD_STARTER,
							name.concat("-失败-补偿次数 {}") + " error info {}", i,
							ExceptionUtil.getFullStackTrace(e));
					} else {
						LogUtil.error(FailOverUtil.class, StarterName.CLOUD_STARTER,
							ExceptionUtil.getFullStackTrace(e));
					}
				}
				times = i + 1;
			}
			if (result.success && times > 0) {
				LogUtil.info(FailOverUtil.class, StarterName.CLOUD_STARTER,
					PropertyUtil.getProperty(SpringApplicationName) + " {} 补偿成功, 补偿次数：{}", name,
					times);
			}
		} finally {
			consumer.accept(result);
		}
		return result.response;
	}

	/**
	 * 反馈结果
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2021/6/22 17:42
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
