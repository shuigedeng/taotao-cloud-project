/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * TraceContextHolder
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:33:52
 */
public class TraceContextHolder {

	private TraceContextHolder() {
	}

	/**
	 * TRACE_CONTEXT
	 */
	private static final ThreadLocal<String> TRACE_CONTEXT = new TransmittableThreadLocal<>();

	/**
	 * setTraceId
	 *
	 * @param traceId traceId
	 * @since 2021-09-02 19:33:58
	 */
	public static void setTraceId(String traceId) {
		TRACE_CONTEXT.set(traceId);
	}

	/**
	 * getTraceId
	 *
	 * @return {@link String }
	 * @since 2021-09-02 19:34:00
	 */
	public static String getTraceId() {
		return TRACE_CONTEXT.get();
	}

	/**
	 * clear
	 *
	 * @since 2021-09-02 19:34:06
	 */
	public static void clear() {
		TRACE_CONTEXT.remove();
	}
}
