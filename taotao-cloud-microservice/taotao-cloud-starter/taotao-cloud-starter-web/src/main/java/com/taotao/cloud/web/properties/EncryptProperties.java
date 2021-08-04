package com.taotao.cloud.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 *  EncryptProperties
 */
@RefreshScope
@ConfigurationProperties(prefix = EncryptProperties.PREFIX)
public class EncryptProperties {

	public static final String PREFIX = "taotao.cloud.web.encrypt";

	private Boolean enabled = true;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
