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
import com.taotao.cloud.common.base.ThreadPool;
import java.util.Collection;
import lombok.var;

/**
 * 提供线程池操作类 默认使用自定义的全局bsf线程池
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/6/2 16:49
 **/
public class ThreadUtils {

	/**
	 * 使用系统线程池并行for循环
	 *
	 * @param taskName      任务名称
	 * @param parallelCount 并行数量
	 * @param taskList      任务列表
	 * @param action        action
	 * @author dengtao
	 * @since 2021/6/22 17:55
	 */
	public static <T> void parallelFor(String taskName, int parallelCount, Collection<T> taskList,
		final Callable.Action1<T> action) {
		if (parallelCount < 2) {
			for (var t : taskList) {
				action.invoke(t);
			}
		} else {
			ThreadPool.System.parallelFor2(taskName, parallelCount, taskList, action);
		}
	}
}
