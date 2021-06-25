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
package com.taotao.cloud.web.interceptor;

import com.taotao.cloud.web.configuration.PrometheusConfiguration.TaoTaoCloudMetrics;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * PrometheusInterceptor
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/06/25 16:50
 */
public class PrometheusMetricsInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		String method = request.getMethod();
		int status = response.getStatus();

		TaoTaoCloudMetrics.inprogressRequests.labels(requestURI, method).inc();

		TaoTaoCloudMetrics.histogramRequestTimer = TaoTaoCloudMetrics.requestLatencyHistogram
			.labels(requestURI, method, String.valueOf(status)).startTimer();

		TaoTaoCloudMetrics.requestTimer = TaoTaoCloudMetrics.requestLatency
			.labels(requestURI, method, String.valueOf(status)).startTimer();
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
		Object handler, Exception ex) throws Exception {
		String requestURI = request.getRequestURI();
		String method = request.getMethod();
		int status = response.getStatus();

		TaoTaoCloudMetrics.requestCounter.labels(requestURI, method, String.valueOf(status)).inc();

		TaoTaoCloudMetrics.inprogressRequests.labels(requestURI, method).dec();

		TaoTaoCloudMetrics.histogramRequestTimer.observeDuration();

		TaoTaoCloudMetrics.requestTimer.observeDuration();

		super.afterCompletion(request, response, handler, ex);
	}
}
