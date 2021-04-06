package com.taotao.cloud.common.utils;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

/**
 * 链路追踪工具类
 */
public class TraceUtil {

	/**
	 * 从header和参数中获取traceId 从前端传入数据
	 *
	 * @param request 　HttpServletRequest
	 * @return traceId
	 */
	public static String getTraceId(HttpServletRequest request) {
		String traceId = request.getParameter(CommonConstant.TAOTAO_CLOUD_TRACE_ID);
		if (StrUtil.isBlank(traceId)) {
			traceId = request.getHeader(CommonConstant.TAOTAO_CLOUD_TRACE_HEADER);
		}
		return traceId;
	}

	public static String getTraceId() {
		return MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID);
	}

	/**
	 * 传递traceId至MDC
	 *
	 * @param traceId 　跟踪ID
	 */
	public static void mdcTraceId(String traceId) {
		if (StrUtil.isNotBlank(traceId)) {
			MDC.put(CommonConstant.TAOTAO_CLOUD_TRACE_ID, traceId);
		}
	}
}
