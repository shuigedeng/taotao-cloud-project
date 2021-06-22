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

import com.taotao.cloud.common.base.Callable;
import com.taotao.cloud.common.base.CoreProperties;
import lombok.var;

/**
 * TimeWatchUtil
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/6/22 17:46
 */
public class TimeWatchUtil {

	/**
	 * 打印时间表
	 *
	 * @param isPrint 是否打印
	 * @param msg     消息
	 * @param action0 action0
	 * @author dengtao
	 * @since 2021/6/22 17:53
	 */
	public static void print(boolean isPrint, String msg, Callable.Action0 action0) {
		print(isPrint, msg, () -> {
			action0.invoke();
			return 1;
		});
	}

	/**
	 * 打印时间表
	 *
	 * @param isPrint 是否打印
	 * @param msg     消息
	 * @param action0 action0
	 * @return T 消息对象
	 * @author dengtao
	 * @since 2021/6/22 17:54
	 */
	public static <T> T print(boolean isPrint, String msg, Callable.Func0<T> action0) {
		if (isPrint) {
			var b = System.currentTimeMillis();
			T t = action0.invoke();
			var e = System.currentTimeMillis();
			LogUtil.info(CoreProperties.Project, msg + " 耗时:" + (e - b) + "毫秒");
			return t;
		} else {
			return action0.invoke();
		}
	}
}
