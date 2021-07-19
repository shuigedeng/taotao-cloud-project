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

import com.taotao.cloud.web.configuration.PrometheusConfiguration;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * PrometheusInterceptor
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/06/25 16:50
 */
public class PrometheusMetricsInterceptor implements HandlerInterceptor {

	@Resource
	private Counter requestCounter;

	@Resource
	private Summary requestLatency;

	@Resource
	private Gauge inprogressRequests;

	@Resource
	private Histogram requestLatencyHistogram;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		String method = request.getMethod();
		int status = response.getStatus();

		inprogressRequests.labels(requestURI, method).inc();

		PrometheusConfiguration.histogramRequestTimer = requestLatencyHistogram
			.labels(requestURI, method, String.valueOf(status)).startTimer();

		PrometheusConfiguration.requestTimer = requestLatency
			.labels(requestURI, method, String.valueOf(status)).startTimer();
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
		Object handler, Exception ex) throws Exception {
		String requestURI = request.getRequestURI();
		String method = request.getMethod();
		int status = response.getStatus();

		requestCounter.labels(requestURI, method, String.valueOf(status)).inc();

		inprogressRequests.labels(requestURI, method).dec();

		if (Objects.nonNull(PrometheusConfiguration.histogramRequestTimer)) {
			PrometheusConfiguration.histogramRequestTimer.observeDuration();
		}

		if (Objects.nonNull(PrometheusConfiguration.requestTimer)) {
			PrometheusConfiguration.requestTimer.observeDuration();
		}
	}
}
