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
package com.taotao.cloud.prometheus.interceptor;

import com.taotao.cloud.prometheus.configuration.PrometheusAutoConfiguration.PrometheusCollector;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * PrometheusMetricsInterceptor
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:02:41
 */
public class PrometheusMetricsInterceptor implements HandlerInterceptor {

	private final PrometheusCollector prometheusCollector;

	public PrometheusMetricsInterceptor(PrometheusCollector prometheusCollector) {
		this.prometheusCollector = prometheusCollector;
	}

	private Histogram.Timer histogramRequestTimer;
	private Summary.Timer requestTimer;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		String method = request.getMethod();
		int status = response.getStatus();

		prometheusCollector.inprogressRequests.labels(requestURI, method).inc();

		this.histogramRequestTimer = prometheusCollector.requestLatencyHistogram
			.labels(requestURI, method, String.valueOf(status)).startTimer();

		this.requestTimer = prometheusCollector.requestLatency
			.labels(requestURI, method, String.valueOf(status)).startTimer();
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
								@NotNull Object handler, Exception ex) throws Exception {
		String requestURI = request.getRequestURI();
		String method = request.getMethod();
		int status = response.getStatus();

		prometheusCollector.requestCounter.labels(requestURI, method, String.valueOf(status)).inc();
		prometheusCollector.inprogressRequests.labels(requestURI, method).dec();

		if (Objects.nonNull(histogramRequestTimer)) {
			histogramRequestTimer.observeDuration();
		}

		if (Objects.nonNull(requestTimer)) {
			requestTimer.observeDuration();
		}
	}



}
