package com.taotao.cloud.gateway.properties;

import java.util.Objects;
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

	public HttpsProperties() {
	}

	public HttpsProperties(Boolean enabled, Integer port) {
		this.enabled = enabled;
		this.port = port;
	}

	@Override
	public String toString() {
		return "HttpsProperties{" +
			"enabled=" + enabled +
			", port=" + port +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		HttpsProperties that = (HttpsProperties) o;
		return Objects.equals(enabled, that.enabled) && Objects.equals(port,
			that.port);
	}

	@Override
	public int hashCode() {
		return Objects.hash(enabled, port);
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
