package com.taotao.cloud.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = HttpsProperties.PREFIX)
public class HttpsProperties {

	public static final String PREFIX = "taotao.cloud.gateway.https";

	/**
	 * 是否启用https
	 */
	private Boolean enabled = false;

	private Integer port = 9443;

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
