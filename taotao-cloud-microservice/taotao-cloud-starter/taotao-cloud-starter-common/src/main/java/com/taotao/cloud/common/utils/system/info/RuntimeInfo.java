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
package com.taotao.cloud.common.utils.system.info;

import java.io.Serializable;

/**
 * 运行时信息，包括内存总大小、已用大小、可用大小等
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:26:16
 */
public class RuntimeInfo implements Serializable {

	private final transient Runtime currentRuntime = Runtime.getRuntime();

	/**
	 * 获得运行时对象
	 *
	 * @since 2021-09-02 19:26:20
	 */
	public final Runtime getRuntime() {
		return currentRuntime;
	}

	/**
	 * 获得JVM最大可用内存
	 *
	 * @since 2021-09-02 19:26:20
	 */
	public final long getMaxMemory() {
		return currentRuntime.maxMemory();
	}

	/**
	 * 获得JVM已分配内存
	 *
	 * @since 2021-09-02 19:26:20
	 */
	public final long getTotalMemory() {
		return currentRuntime.totalMemory();
	}

	/**
	 * 获得JVM已分配内存中的剩余空间
	 *
	 * @since 2021-09-02 19:26:20
	 */
	public final long getFreeMemory() {
		return currentRuntime.freeMemory();
	}

	/**
	 * 获得JVM最大可用内存
	 *
	 * @since 2021-09-02 19:26:20
	 */
	public final long getUsableMemory() {
		return currentRuntime.maxMemory() - currentRuntime.totalMemory()
			+ currentRuntime.freeMemory();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("Runtime:         ").append(getRuntime())
			.append("\nMax Memory:      ").append(getMaxMemory())
			.append("\nTotal Memory:    ").append(getTotalMemory())
			.append("\nFree Memory:     ").append(getFreeMemory())
			.append("\nUsable Memory:   ").append(getUsableMemory());

		return builder.toString();
	}

}
