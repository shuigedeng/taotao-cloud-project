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
package com.taotao.cloud.common.utils.servlet;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

/**
 * 链路追踪工具类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 14:39:33
 */
public class TraceUtil {

	private TraceUtil() {
	}

	/**
	 * 从header和参数中获取traceId 从前端传入数据
	 *
	 * @param request 　request
	 * @return {@link String } 跟踪ID
	 * @since 2021-09-02 14:39:44
	 */
	public static String getTraceId(HttpServletRequest request) {
		String traceId = request.getParameter(CommonConstant.TAOTAO_CLOUD_TRACE_ID);
		if (StrUtil.isBlank(traceId)) {
			//final Enumeration<String> headerNames = request.getHeaderNames();
			//while (headerNames.hasMoreElements()) {
			//	System.out.println(headerNames.nextElement());
			//}
			traceId = request.getHeader(CommonConstant.TAOTAO_CLOUD_TRACE_HEADER);
		}
		return traceId;
	}

	/**
	 * 获取traceId
	 *
	 * @return {@link String } 跟踪ID
	 * @since 2021-09-02 14:40:16
	 */
	public static String getTraceId() {
		return MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID);
	}

	/**
	 * 传递traceId至MDC
	 *
	 * @param traceId 　跟踪ID
	 * @since 2021-09-02 14:48:47
	 */
	public static void mdcTraceId(String traceId) {
		if (StrUtil.isNotBlank(traceId)) {
			MDC.put(CommonConstant.TAOTAO_CLOUD_TRACE_ID, traceId);
		}
	}

	/**
	 * 传递tenantId至MDC
	 *
	 * @param tenantId 　租户id
	 * @since 2021-09-02 14:48:59
	 */
	public static void mdcTenantId(String tenantId) {
		if (StrUtil.isNotBlank(tenantId)) {
			MDC.put(CommonConstant.TAOTAO_CLOUD_TENANT_ID, tenantId);
		}
	}

	/**
	 * 传递version至MDC
	 *
	 * @param version 　租户id
	 * @since 2021-09-02 14:49:14
	 */
	public static void mdcVersion(String version) {
		if (StrUtil.isNotBlank(version)) {
			MDC.put(CommonConstant.TAOTAO_CLOUD_REQUEST_VERSION, version);
		}
	}

	/**
	 * 从header和参数中获取Zipkin traceId 从前端传入数据
	 *
	 * @param request 　HttpServletRequest
	 * @return {@link String } 跟踪ID
	 * @since 2021-09-02 14:49:23
	 */
	public static String getZipkinTraceId(HttpServletRequest request) {
		String zipkinTraceId = request.getParameter(CommonConstant.ZIPKIN_TRACE_ID);
		if (StrUtil.isBlank(zipkinTraceId)) {
			//final Enumeration<String> headerNames = request.getHeaderNames();
			//while (headerNames.hasMoreElements()) {
			//	System.out.println(headerNames.nextElement());
			//}
			zipkinTraceId = request.getHeader(CommonConstant.ZIPKIN_TRACE_ID);
		}
		return zipkinTraceId;
	}

	/**
	 * 从header和参数中获取Zipkin SpanId 从前端传入数据
	 *
	 * @param request 　HttpServletRequest
	 * @return {@link String } 跟踪ID
	 * @since 2021-09-02 14:49:43
	 */
	public static String getZipkinSpanId(HttpServletRequest request) {
		String zipkinSpanId = request.getParameter(CommonConstant.ZIPKIN_SPANE_ID);
		if (StrUtil.isBlank(zipkinSpanId)) {
			//final Enumeration<String> headerNames = request.getHeaderNames();
			//while (headerNames.hasMoreElements()) {
			//	System.out.println(headerNames.nextElement());
			//}
			zipkinSpanId = request.getHeader(CommonConstant.ZIPKIN_SPANE_ID);
		}
		return zipkinSpanId;
	}

	/**
	 * 传递zipkinTraceId至MDC
	 *
	 * @param request 　request
	 * @since 2021-09-02 14:50:07
	 */
	public static void mdcZipkinTraceId(HttpServletRequest request) {
		String zipkinTraceId = getZipkinTraceId(request);
		if (StrUtil.isNotBlank(zipkinTraceId)) {
			MDC.put(CommonConstant.ZIPKIN_TRACE_ID, zipkinTraceId);
		}
	}

	/**
	 * 传递zipkinSpanId至MDC
	 *
	 * @param request 　request
	 * @since 2021-09-02 14:50:16
	 */
	public static void mdcZipkinSpanId(HttpServletRequest request) {
		String zipkinSpanId = getZipkinSpanId(request);
		if (StrUtil.isNotBlank(zipkinSpanId)) {
			MDC.put(CommonConstant.ZIPKIN_SPANE_ID, zipkinSpanId);
		}
	}
}
