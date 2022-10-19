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
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.monitor.Monitor;
import com.taotao.cloud.monitor.collect.HealthCheckProvider;
import com.taotao.cloud.monitor.configuration.HealthAutoConfiguration;
import com.taotao.cloud.monitor.model.Report;
import com.taotao.cloud.prometheus.configuration.PrometheusAutoConfiguration.PrometheusCollector;
import com.taotao.cloud.prometheus.interceptor.PrometheusMetricsInterceptor;
import com.taotao.cloud.prometheus.properties.PrometheusProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
@Import(PrometheusCollector.class)
public class PrometheusAutoConfiguration implements WebMvcConfigurer, InitializingBean,
	DisposableBean {

	@Autowired(required = false)
	private HealthCheckProvider healthCheckProvider;
	@Autowired
	private PrometheusMeterRegistry prometheusMeterRegistry;
	@Value("${spring.application.name}")
	private String applicationName;
	@Autowired
	private PrometheusCollector prometheusCollector;

	private final Map<String, Gauge> gaugeMap = new ConcurrentHashMap<>();
	private ThreadPoolExecutor monitorThreadPoolExecutor;

	@Bean
	MeterRegistryCustomizer<MeterRegistry> appMetricsCommonTags() {
		return registry -> registry.config().commonTags("application", applicationName);
	}

	@Override
	public void destroy() throws Exception {
		if (null != monitorThreadPoolExecutor) {
			Monitor.shutdownThreadlPool(monitorThreadPoolExecutor);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(PrometheusAutoConfiguration.class, StarterName.PROMETHEUS_STARTER);
		if (Objects.nonNull(healthCheckProvider)) {
			Monitor monitorThreadPool = healthCheckProvider.getMonitor();
			monitorThreadPoolExecutor = monitorThreadPool.getMonitorThreadPoolExecutor();
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
						LogUtils.warn(StarterName.MONITOR_STARTER, "HealthCheck Prometheus error ",
							e);
					}

					try {
						Thread.sleep(5 * 1000L);
					} catch (Exception e) {
						LogUtils.error(e);
					}
				}
			});
		}
	}

	//拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new PrometheusMetricsInterceptor(prometheusCollector))
			.addPathPatterns("/**")
			.excludePathPatterns("/actuator/**");
	}


	public static class PrometheusCollector implements InitializingBean {

		@Autowired
		private PrometheusMeterRegistry prometheusMeterRegistry;
		@Value("${spring.application.name}")
		private String applicationName;

		/**
		 * requestCounter
		 */
		public Counter requestCounter;
		/**
		 * inprogressRequests
		 */
		public Gauge inprogressRequests;
		/**
		 * requestLatencyHistogram
		 */
		public Histogram requestLatencyHistogram;
		/**
		 * requestLatency
		 */
		public Summary requestLatency;

		/**
		 * 服务启动时创建自定义指标
		 */
		private void init() {
			/**
			 * 用于统计请求总数 计数器可以用于记录只会增加不会减少的指标类型，比如记录应用请求的总量(http_requests_total)，
			 * cpu使用时间(process_cpu_seconds_total)等。 一般而言，Counter类型的metrics指标在命名中 我们使用_total结束。
			 */
			this.requestCounter = Counter.build()
				.name(getName() + "_requests_total")
				.help("请求总数数据")
				.labelNames("service", "method", "code")
				.register(prometheusMeterRegistry.getPrometheusRegistry());

			/**
			 * 使用Gauge可以反映应用的当前状态,例如在监控主机时，主机当前空闲的内容大小(node_memory_MemFree)， 37		 *
			 * 可用内存大小(node_memory_MemAvailable)。或者容器当前的CPU使用率,内存使用率。这里我们使用 Gauge记录当前应用正在处理的Http请求数量。 38
			 */
			this.inprogressRequests = Gauge.build()
				.name(getName() + "http_inprogress_requests")
				.labelNames("path", "method")
				.help("进行中的请求状态数据")
				.register(prometheusMeterRegistry.getPrometheusRegistry());

			/**
			 * 主要用于在指定分布范围内(Buckets)记录大小(如http request bytes)或者事件发生的次数。 以请求响应时间requests_latency_seconds为例
			 */
			this.requestLatencyHistogram = Histogram.build()
				.name(getName() + "_http_requests_latency_seconds_histogram")
				.labelNames("path", "method", "code")
				.help("以秒为单位的请求延迟直方图数据")
				.register(prometheusMeterRegistry.getPrometheusRegistry());

			/**
			 * 和Histogram类似，不同在于Histogram可以通过histogram_quantile函数在服务器端计算分位数，而 Sumamry的分位数则是直接在客户端进行定义。因此对于分位数的计算。
			 * Summary在通过PromQL进行查询时 有更好的性能表现，而Histogram则会消耗更多的资源。相对的对于客户端而言Histogram消耗的资源更少 用于统计TP50，TP90
			 */
			this.requestLatency = Summary.build()
				.name(getName() + "_request_latency")
				.quantile(0.5, 0.05)
				.quantile(0.9, 0.01)
				.labelNames("path", "method", "code")
				.help("以秒为单位的请求延迟分数数据")
				.register(prometheusMeterRegistry.getPrometheusRegistry());
		}

		private String getName() {
			return applicationName.replaceAll("-", "_");
		}

		@Override
		public void afterPropertiesSet() throws Exception {
			init();
		}
	}
}
