package com.taotao.cloud.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = HttpsProperties.PREFIX)
public class HttpsProperties {

	public static final String PREFIX = "taotao.cloud.gateway.https";

	/**
	 * 是否启用https
	 */
	private Boolean enabled = true;

	private Integer port = 9443;
}
