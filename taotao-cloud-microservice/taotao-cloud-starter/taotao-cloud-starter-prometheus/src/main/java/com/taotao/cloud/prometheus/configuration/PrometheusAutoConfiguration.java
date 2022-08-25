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
package com.taotao.cloud.prometheus.configuration;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.monitor.Monitor;
import com.taotao.cloud.monitor.collect.HealthCheckProvider;
import com.taotao.cloud.monitor.configuration.HealthAutoConfiguration;
import com.taotao.cloud.monitor.model.Report;
import com.taotao.cloud.prometheus.interceptor.PrometheusMetricsInterceptor;
import com.taotao.cloud.prometheus.properties.PrometheusProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Gauge;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * PrometheusConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:28:17
 */
@EnableConfigurationProperties({PrometheusProperties.class})
@ConditionalOnProperty(prefix = PrometheusProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfiguration(after = HealthAutoConfiguration.class)
public class PrometheusAutoConfiguration implements WebMvcConfigurer, InitializingBean {

	@Autowired(required = false)
	private HealthCheckProvider healthCheckProvider;
	@Autowired
	private PrometheusMeterRegistry prometheusMeterRegistry;
	@Value("${spring.application.name}")
	private String applicationName;

	private final Map<String, Gauge> gaugeMap = new ConcurrentHashMap<>();

	@Bean
	MeterRegistryCustomizer<MeterRegistry> appMetricsCommonTags() {
		return registry -> registry.config().commonTags("application", applicationName);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(PrometheusAutoConfiguration.class, StarterName.PROMETHEUS_STARTER);
		if (Objects.nonNull(healthCheckProvider)) {
			Monitor monitorThreadPool = healthCheckProvider.getMonitor();
			ThreadPoolExecutor monitorThreadPoolExecutor = monitorThreadPool.getMonitorThreadPoolExecutor();
			monitorThreadPoolExecutor.submit(() -> {
				while (!monitorThreadPool.monitorIsShutdown()) {
					try {
						Report report = healthCheckProvider.getReport(false);
						report.eachReport((field, reportItem) -> {
							Object value = reportItem.getValue();
							String desc = reportItem.getDesc();
							if (!StrUtil.isBlankIfStr(value) && value instanceof Number number) {
								String[] labelNames = field.split("\\.");
								field = field.replaceAll("\\.", "_");

								Gauge gauge = gaugeMap.get(field);
								if (Objects.isNull(gauge)) {
									gauge = Gauge.build()
										.name(field)
										.labelNames(labelNames)
										.help(desc)
										.register(prometheusMeterRegistry.getPrometheusRegistry());
									gaugeMap.put(field, gauge);
								}

								gauge.labels(labelNames).set(number.doubleValue());
							}
							return null;
						});
					} catch (Exception e) {
						LogUtil.warn(StarterName.MONITOR_STARTER, "HealthCheck Prometheus error ", e);
					}

					try {
						Thread.sleep(5 * 1000L);
					} catch (Exception e) {
						LogUtil.error(e);
					}
				}
			});
		}
	}

	@Bean
	public PrometheusMetricsInterceptor prometheusMetricsInterceptor() {
		return new PrometheusMetricsInterceptor();
	}

	//拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(prometheusMetricsInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/actuator/**");
	}
}
