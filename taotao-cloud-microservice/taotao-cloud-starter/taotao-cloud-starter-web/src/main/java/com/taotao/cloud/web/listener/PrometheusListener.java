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
package com.taotao.cloud.web.listener;

import com.taotao.cloud.web.configuration.PrometheusConfiguration.TaoTaoCloudMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import javax.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * PrometheusListener
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/06/25 17:02
 */
public class PrometheusListener implements ApplicationListener<ContextRefreshedEvent> {

	@Resource
	PrometheusMeterRegistry meterRegistry;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		new TaoTaoCloudMetrics();

		CollectorRegistry prometheusRegistry = meterRegistry.getPrometheusRegistry();

		prometheusRegistry.register(TaoTaoCloudMetrics.requestCounter);
		prometheusRegistry.register(TaoTaoCloudMetrics.inprogressRequests);
		prometheusRegistry.register(TaoTaoCloudMetrics.requestLatencyHistogram);
		prometheusRegistry.register(TaoTaoCloudMetrics.requestLatency);
	}

}
