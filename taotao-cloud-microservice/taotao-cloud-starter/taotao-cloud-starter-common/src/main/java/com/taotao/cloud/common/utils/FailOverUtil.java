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
package com.taotao.cloud.common.utils;

import static com.taotao.cloud.common.base.CoreProperties.SpringApplicationName;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import lombok.Getter;

/**
 * 错误补偿工具类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:23
 */
public class FailOverUtil {

	private final static String name = "补偿工具";

	/**
	 * 错误补偿工具类
	 *
	 * @param consumer
	 * @param c1
	 * @return T
	 * @author dengtao
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
						LogUtil
							.error(name.concat("-失败-补偿次数" + i) + " error info: {0}", e,
								ExceptionUtil.getFullStackTrace(e));
					} else {
						LogUtil.error(ExceptionUtil.getFullStackTrace(e));
					}
				}
				times = i + 1;
			}
			if (result.success && times > 0) {
				LogUtil.info(
					PropertyUtil.getProperty(SpringApplicationName) + " " + name
						+ " 补偿成功, 补偿次数：{0}", times);
			}
		} finally {
			consumer.accept(result);
		}
		return result.response;
	}

	/**
	 * 反馈结果
	 *
	 * @author dengtao
	 * @version 1.0.0
	 * @since 2021/6/22 17:42
	 */
	@Getter
	public static class Result<T> {

		private boolean success = false;
		private Throwable throwable;
		private T response;
	}
}
