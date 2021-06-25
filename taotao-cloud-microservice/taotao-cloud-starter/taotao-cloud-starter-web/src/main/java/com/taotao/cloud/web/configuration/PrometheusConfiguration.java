package com.taotao.cloud.web.configuration;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;

/**
 * PrometheusConfiguration
 *
 * @author zuihou
 * @date 2020/12/30 2:48 下午
 */
public class PrometheusConfiguration {

	public static class TaoTaoCloudMetrics {

		/**
		 * 计数器可以用于记录只会增加不会减少的指标类型，比如记录应用请求的总量(http_requests_total)， cpu使用时间(process_cpu_seconds_total)等。
		 * 一般而言，Counter类型的metrics指标在命名中 我们使用_total结束。
		 */
		public static Counter requestCounter = Counter.build()
			.name("io_namespace_http_requests_total")
			.labelNames("path", "method", "code")
			.help("Total requests.").register();

		/**
		 * 使用Gauge可以反映应用的当前状态,例如在监控主机时，主机当前空闲的内容大小(node_memory_MemFree)，
		 * 可用内存大小(node_memory_MemAvailable)。或者容器当前的CPU使用率,内存使用率。这里我们使用 Gauge记录当前应用正在处理的Http请求数量。
		 */
		public static Gauge inprogressRequests = Gauge.build()
			.name("io_namespace_http_inprogress_requests").labelNames("path", "method")
			.help("Inprogress requests.").register();

		/**
		 * 主要用于在指定分布范围内(Buckets)记录大小(如http request bytes)或者事件发生的次数。 以请求响应时间requests_latency_seconds为例
		 */
		public static Histogram requestLatencyHistogram = Histogram.build()
			.labelNames("path", "method", "code")
			.name("io_namespace_http_requests_latency_seconds_histogram")
			.help("Request latency in seconds.")
			.register();
		public static Histogram.Timer histogramRequestTimer;

		/**
		 * 和Histogram类似，不同在于Histogram可以通过histogram_quantile函数在服务器端计算分位数，而
		 * Sumamry的分位数则是直接在客户端进行定义。因此对于分位数的计算。 Summary在通过PromQL进行查询时 有更好的性能表现，而Histogram则会消耗更多的资源。相对的对于客户端而言Histogram消耗的资源更少
		 */
		public static Summary requestLatency = Summary.build()
			.name("io_namespace_http_requests_latency_seconds_summary")
			.quantile(0.5, 0.05)
			.quantile(0.9, 0.01)
			.labelNames("path", "method", "code")
			.help("Request latency in seconds.").register();
		public static Summary.Timer requestTimer;
	}
}
