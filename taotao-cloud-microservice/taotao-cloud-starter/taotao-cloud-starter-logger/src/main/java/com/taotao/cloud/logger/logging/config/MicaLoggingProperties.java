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
package com.taotao.cloud.logger.logging.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.ClassUtils;

/**
 * logging 配置
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:42:44
 */
@RefreshScope
@ConfigurationProperties(MicaLoggingProperties.PREFIX)
public class MicaLoggingProperties {

	public static final String PREFIX = "mica.logging";

	private Console console = new Console();
	private Files files = new Files();
	private Logstash logstash = new Logstash();
	private Loki loki = new Loki();

	public static class Console {

		/**
		 * 是否启动完成后关闭控制台日志，适用于，正式环境
		 */
		private boolean closeAfterStart = false;

		public boolean isCloseAfterStart() {
			return closeAfterStart;
		}

		public void setCloseAfterStart(boolean closeAfterStart) {
			this.closeAfterStart = closeAfterStart;
		}
	}

	public static class Files {

		public static final String PREFIX = MicaLoggingProperties.PREFIX + ".files";
		/**
		 * 是否开启文件日志
		 */
		private boolean enabled = true;
		/**
		 * 使用 json 格式化
		 */
		private boolean useJsonFormat = false;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isUseJsonFormat() {
			return useJsonFormat;
		}

		public void setUseJsonFormat(boolean useJsonFormat) {
			this.useJsonFormat = useJsonFormat;
		}
	}

	public static class Logstash {

		public static final String PREFIX = MicaLoggingProperties.PREFIX + ".logstash";
		/**
		 * 是否开启 logstash 日志收集
		 */
		private boolean enabled = false;
		/**
		 * 目标地址，默认： localhost:5000，示例： host1.domain.com,host2.domain.com:5560
		 */
		private String destinations = "localhost:5000";
		/**
		 * 自定义扩展字段
		 */
		private Map<String, Object> customFieldMap = new HashMap<>();
		/**
		 * logstash 队列大小
		 */
		private int ringBufferSize = 512;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public String getDestinations() {
			return destinations;
		}

		public void setDestinations(String destinations) {
			this.destinations = destinations;
		}

		public Map<String, Object> getCustomFieldMap() {
			return customFieldMap;
		}

		public void setCustomFieldMap(Map<String, Object> customFieldMap) {
			this.customFieldMap = customFieldMap;
		}

		public int getRingBufferSize() {
			return ringBufferSize;
		}

		public void setRingBufferSize(int ringBufferSize) {
			this.ringBufferSize = ringBufferSize;
		}
	}

	public static class Loki {

		public static final String PREFIX = MicaLoggingProperties.PREFIX + ".loki";
		/**
		 * 是否开启 loki 日志收集
		 */
		private boolean enabled = false;
		/**
		 * 编码方式，支持 Json、ProtoBuf，默认： Json
		 */
		private LokiEncoder encoder = LokiEncoder.Json;
		/**
		 * http sender，支持 java11、OKHttp、ApacheHttp，默认: 从项目依赖中查找，顺序 java11 -> okHttp -> ApacheHttp
		 */
		private HttpSender httpSender;
		/**
		 * 通用配置
		 */
		private int batchMaxItems = 1000;
		private int batchMaxBytes = 4 * 1024 * 1024;
		private long batchTimeoutMs = 60000;
		private long sendQueueMaxBytes = 41943040;
		/**
		 * 使用堆外内存
		 */
		private boolean useDirectBuffers = true;
		private boolean drainOnStop = true;
		/**
		 * 开启 metrics
		 */
		private boolean metricsEnabled = false;
		private boolean verbose = false;
		/**
		 * http 配置，默认: http://localhost:3100/loki/api/v1/push
		 */
		private String httpUrl = "http://localhost:3100/loki/api/v1/push";
		private long httpConnectionTimeoutMs = 30000;
		private long httpRequestTimeoutMs = 5000;
		private String httpAuthUsername;
		private String httpAuthPassword;
		private String httpTenantId;
		/**
		 * format 标签，默认：
		 * appName=${appName},profile=${profile},host=${HOSTNAME},level=%level,traceId=%X{traceId:-NAN},requestId=%X{requestId:-}
		 */
		private String formatLabelPattern = "appName=${appName},profile=${profile},host=${HOSTNAME},level=%level,traceId=%X{traceId:-NAN},requestId=%X{requestId:-NAN}";
		/**
		 * format 标签扩展
		 */
		private String formatLabelPatternExtend;
		/**
		 * format 标签分隔符，默认:，
		 */
		private String formatLabelPairSeparator = ",";
		/**
		 * format 标签 key、value 分隔符，默认: =
		 */
		private String formatLabelKeyValueSeparator = "=";
		private boolean formatLabelNoPex = true;
		/**
		 * 消息体格式，默认为: l=%level c=%logger{20} t=%thread | %msg %ex
		 */
		private String formatMessagePattern;
		private boolean formatStaticLabels = false;
		private boolean formatSortByTime = false;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public LokiEncoder getEncoder() {
			return encoder;
		}

		public void setEncoder(LokiEncoder encoder) {
			this.encoder = encoder;
		}

		public HttpSender getHttpSender() {
			return httpSender;
		}

		public void setHttpSender(
			HttpSender httpSender) {
			this.httpSender = httpSender;
		}

		public int getBatchMaxItems() {
			return batchMaxItems;
		}

		public void setBatchMaxItems(int batchMaxItems) {
			this.batchMaxItems = batchMaxItems;
		}

		public int getBatchMaxBytes() {
			return batchMaxBytes;
		}

		public void setBatchMaxBytes(int batchMaxBytes) {
			this.batchMaxBytes = batchMaxBytes;
		}

		public long getBatchTimeoutMs() {
			return batchTimeoutMs;
		}

		public void setBatchTimeoutMs(long batchTimeoutMs) {
			this.batchTimeoutMs = batchTimeoutMs;
		}

		public long getSendQueueMaxBytes() {
			return sendQueueMaxBytes;
		}

		public void setSendQueueMaxBytes(long sendQueueMaxBytes) {
			this.sendQueueMaxBytes = sendQueueMaxBytes;
		}

		public boolean isUseDirectBuffers() {
			return useDirectBuffers;
		}

		public void setUseDirectBuffers(boolean useDirectBuffers) {
			this.useDirectBuffers = useDirectBuffers;
		}

		public boolean isDrainOnStop() {
			return drainOnStop;
		}

		public void setDrainOnStop(boolean drainOnStop) {
			this.drainOnStop = drainOnStop;
		}

		public boolean isMetricsEnabled() {
			return metricsEnabled;
		}

		public void setMetricsEnabled(boolean metricsEnabled) {
			this.metricsEnabled = metricsEnabled;
		}

		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}

		public String getHttpUrl() {
			return httpUrl;
		}

		public void setHttpUrl(String httpUrl) {
			this.httpUrl = httpUrl;
		}

		public long getHttpConnectionTimeoutMs() {
			return httpConnectionTimeoutMs;
		}

		public void setHttpConnectionTimeoutMs(long httpConnectionTimeoutMs) {
			this.httpConnectionTimeoutMs = httpConnectionTimeoutMs;
		}

		public long getHttpRequestTimeoutMs() {
			return httpRequestTimeoutMs;
		}

		public void setHttpRequestTimeoutMs(long httpRequestTimeoutMs) {
			this.httpRequestTimeoutMs = httpRequestTimeoutMs;
		}

		public String getHttpAuthUsername() {
			return httpAuthUsername;
		}

		public void setHttpAuthUsername(String httpAuthUsername) {
			this.httpAuthUsername = httpAuthUsername;
		}

		public String getHttpAuthPassword() {
			return httpAuthPassword;
		}

		public void setHttpAuthPassword(String httpAuthPassword) {
			this.httpAuthPassword = httpAuthPassword;
		}

		public String getHttpTenantId() {
			return httpTenantId;
		}

		public void setHttpTenantId(String httpTenantId) {
			this.httpTenantId = httpTenantId;
		}

		public String getFormatLabelPattern() {
			return formatLabelPattern;
		}

		public void setFormatLabelPattern(String formatLabelPattern) {
			this.formatLabelPattern = formatLabelPattern;
		}

		public String getFormatLabelPatternExtend() {
			return formatLabelPatternExtend;
		}

		public void setFormatLabelPatternExtend(String formatLabelPatternExtend) {
			this.formatLabelPatternExtend = formatLabelPatternExtend;
		}

		public String getFormatLabelPairSeparator() {
			return formatLabelPairSeparator;
		}

		public void setFormatLabelPairSeparator(String formatLabelPairSeparator) {
			this.formatLabelPairSeparator = formatLabelPairSeparator;
		}

		public String getFormatLabelKeyValueSeparator() {
			return formatLabelKeyValueSeparator;
		}

		public void setFormatLabelKeyValueSeparator(String formatLabelKeyValueSeparator) {
			this.formatLabelKeyValueSeparator = formatLabelKeyValueSeparator;
		}

		public boolean isFormatLabelNoPex() {
			return formatLabelNoPex;
		}

		public void setFormatLabelNoPex(boolean formatLabelNoPex) {
			this.formatLabelNoPex = formatLabelNoPex;
		}

		public String getFormatMessagePattern() {
			return formatMessagePattern;
		}

		public void setFormatMessagePattern(String formatMessagePattern) {
			this.formatMessagePattern = formatMessagePattern;
		}

		public boolean isFormatStaticLabels() {
			return formatStaticLabels;
		}

		public void setFormatStaticLabels(boolean formatStaticLabels) {
			this.formatStaticLabels = formatStaticLabels;
		}

		public boolean isFormatSortByTime() {
			return formatSortByTime;
		}

		public void setFormatSortByTime(boolean formatSortByTime) {
			this.formatSortByTime = formatSortByTime;
		}
	}

	/**
	 * 编码方式
	 */
	public enum LokiEncoder {
		/**
		 * Encoder
		 */
		Json,
		ProtoBuf
	}

	/**
	 * http Sender
	 */
	public enum HttpSender {
		/**
		 * http 方式
		 */
		JAVA11("java.net.http.HttpClient"),
		OKHttp("okhttp3.OkHttpClient"),
		ApacheHttp("org.apache.http.impl.client.HttpClients");

		/**
		 * sender 判定类
		 */
		private final String senderClass;

		HttpSender(String senderClass) {
			this.senderClass = senderClass;
		}

		public String getSenderClass() {
			return senderClass;
		}

		public boolean isAvailable() {
			return ClassUtils.isPresent(senderClass, null);
		}
	}

	public Console getConsole() {
		return console;
	}

	public void setConsole(Console console) {
		this.console = console;
	}

	public Files getFiles() {
		return files;
	}

	public void setFiles(Files files) {
		this.files = files;
	}

	public Logstash getLogstash() {
		return logstash;
	}

	public void setLogstash(Logstash logstash) {
		this.logstash = logstash;
	}

	public Loki getLoki() {
		return loki;
	}

	public void setLoki(Loki loki) {
		this.loki = loki;
	}
}
