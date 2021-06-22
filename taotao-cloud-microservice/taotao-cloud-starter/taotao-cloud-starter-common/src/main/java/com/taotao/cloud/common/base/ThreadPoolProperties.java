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
package com.taotao.cloud.common.base;

import com.taotao.cloud.common.utils.PropertyUtil;

/**
 * ThreadPoolProperties
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/6/22 17:10
 **/
public class ThreadPoolProperties {

	public static String Prefix = "taotao.cloud.threadpool.";

	public static int getThreadPoolMaxSize() {
		return PropertyUtil.getPropertyCache("taotao.cloud.threadpool.max", 500);
	}

	public static int getThreadPoolMinSize() {
		return PropertyUtil.getPropertyCache("taotao.cloud.threadpool.min", 0);
	}
}
