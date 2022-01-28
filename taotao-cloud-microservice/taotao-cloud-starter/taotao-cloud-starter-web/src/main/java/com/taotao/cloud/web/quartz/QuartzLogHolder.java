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
package com.taotao.cloud.web.quartz;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 租户context holder
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:33:03
 */
public class QuartzLogHolder {

	private QuartzLogHolder() {
	}

	/**
	 * CONTEXT
	 */
	private static final ThreadLocal<QuartzLogModel> QUARTZ_LOG_CONTEXT = new TransmittableThreadLocal<>();

	/**
	 * setTenant
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 19:33:31
	 */
	public static void setLog(QuartzLogModel log) {
		QUARTZ_LOG_CONTEXT.set(log);
	}

	/**
	 * getTenant
	 *
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 19:33:34
	 */
	public static QuartzLogModel getLog() {
		return QUARTZ_LOG_CONTEXT.get();
	}

	/**
	 * clear
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 19:33:39
	 */
	public static void clear() {
		QUARTZ_LOG_CONTEXT.remove();
	}
}
