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
package com.taotao.cloud.web.configuration;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.monitor.Monitor;
import com.taotao.cloud.health.collect.HealthCheckProvider;
import com.taotao.cloud.health.configuration.HealthAutoConfiguration;
import com.taotao.cloud.health.model.Report;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * PrometheusConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:28:17
 */
@AutoConfiguration(after = HealthAutoConfiguration.class)
public class PrometheusAutoConfiguration implements InitializingBean {

	@Autowired(required = false)
	private HealthCheckProvider healthCheckProvider;

	@Value("${spring.application.name}")
	private String applicationName;

	private final Map<String, Gauge> gaugeMap = new ConcurrentHashMap<>();

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
						LogUtil.warn(StarterName.HEALTH_STARTER, "HealthCheck Prometheus error ", e);
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

	private final PrometheusMeterRegistry prometheusMeterRegistry;

	public PrometheusAutoConfiguration(PrometheusMeterRegistry prometheusMeterRegistry) {
		this.prometheusMeterRegistry = prometheusMeterRegistry;
	}

	/**
	 * 用于统计请求总数 计数器可以用于记录只会增加不会减少的指标类型，比如记录应用请求的总量(http_requests_total)，
	 * cpu使用时间(process_cpu_seconds_total)等。 一般而言，Counter类型的metrics指标在命名中 我们使用_total结束。
	 */
	@Bean
	public Counter requestCounter() {
		LogUtil.started(Counter.class, StarterName.WEB_STARTER);

		return Counter.build()
			.name(getName() + "_requests_total")
			.help("请求总数数据")
			.labelNames("service", "method", "code")
			.register(prometheusMeterRegistry.getPrometheusRegistry());
	}

	/**
	 * 使用Gauge可以反映应用的当前状态,例如在监控主机时，主机当前空闲的内容大小(node_memory_MemFree)， 37		 *
	 * 可用内存大小(node_memory_MemAvailable)。或者容器当前的CPU使用率,内存使用率。这里我们使用 Gauge记录当前应用正在处理的Http请求数量。 38
	 */
	@Bean
	public Gauge getInprogressRequests() {
		LogUtil.started(Gauge.class, StarterName.WEB_STARTER);

		return Gauge.build()
			.name(getName() + "http_inprogress_requests")
			.labelNames("path", "method")
			.help("进行中的请求状态数据")
			.register(prometheusMeterRegistry.getPrometheusRegistry());
	}

	/**
	 * 主要用于在指定分布范围内(Buckets)记录大小(如http request bytes)或者事件发生的次数。 以请求响应时间requests_latency_seconds为例
	 */
	@Bean
	public Histogram getRequestLatencyHistogram() {
		LogUtil.started(Histogram.class, StarterName.WEB_STARTER);

		return Histogram.build()
			.name(getName() + "_http_requests_latency_seconds_histogram")
			.labelNames("path", "method", "code")
			.help("以秒为单位的请求延迟直方图数据")
			.register(prometheusMeterRegistry.getPrometheusRegistry());
	}

	public static Histogram.Timer histogramRequestTimer;

	/**
	 * 和Histogram类似，不同在于Histogram可以通过histogram_quantile函数在服务器端计算分位数，而 Sumamry的分位数则是直接在客户端进行定义。因此对于分位数的计算。
	 * Summary在通过PromQL进行查询时 有更好的性能表现，而Histogram则会消耗更多的资源。相对的对于客户端而言Histogram消耗的资源更少 用于统计TP50，TP90
	 */
	@Bean
	public Summary requestLatency() {
		LogUtil.started(Summary.class, StarterName.WEB_STARTER);

		return Summary.build()
			.name(getName() + "_request_latency")
			.quantile(0.5, 0.05)
			.quantile(0.9, 0.01)
			.labelNames("path", "method", "code")
			.help("以秒为单位的请求延迟分数数据")
			.register(prometheusMeterRegistry.getPrometheusRegistry());
	}

	public static Summary.Timer requestTimer;

	private String getName(){
		return applicationName.replaceAll("-", "_");
	}
}
