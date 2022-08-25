package com.taotao.cloud.monitor.kuding.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class ClearBodyInterceptor implements HandlerInterceptor {

	private CurrentRequetBodyResolver currentRequetBodyResolver;

	private final Log logger = LogFactory.getLog(getClass());

	public ClearBodyInterceptor(CurrentRequetBodyResolver currentRequetBodyResolver) {
		this.currentRequetBodyResolver = currentRequetBodyResolver;
	}

	public ClearBodyInterceptor() {
	}

	/**
	 * @return the currentRequetBodyResolver
	 */
	public CurrentRequetBodyResolver getCurrentRequetBodyResolver() {
		return currentRequetBodyResolver;
	}

	/**
	 * @param currentRequetBodyResolver the currentRequetBodyResolver to set
	 */
	public void setCurrentRequetBodyResolver(CurrentRequetBodyResolver currentRequetBodyResolver) {
		this.currentRequetBodyResolver = currentRequetBodyResolver;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.trace("清除请求体数据");
		currentRequetBodyResolver.remove();
	}

}
