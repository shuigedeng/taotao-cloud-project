package com.taotao.cloud.opentracing.zipkin.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义 Sleuth Web 配置
 */
@ConfigurationProperties("spring.sleuth.web.servlet")
public class CustomSleuthWebProperties {

	private String ignoreHeaders;

	private String ignoreParameters;

	public String getIgnoreHeaders() {
		return ignoreHeaders;
	}

	public void setIgnoreHeaders(String ignoreHeaders) {
		this.ignoreHeaders = ignoreHeaders;
	}

	public String getIgnoreParameters() {
		return ignoreParameters;
	}

	public void setIgnoreParameters(String ignoreParameters) {
		this.ignoreParameters = ignoreParameters;
	}
}
