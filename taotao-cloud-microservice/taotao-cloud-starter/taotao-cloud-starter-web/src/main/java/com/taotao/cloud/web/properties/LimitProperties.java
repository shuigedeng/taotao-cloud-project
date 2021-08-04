package com.taotao.cloud.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 忽略XSS 配置类
 */
@RefreshScope
@ConfigurationProperties(prefix = LimitProperties.PREFIX)
public class LimitProperties {

	public static final String PREFIX = "taotao.cloud.web.limit";

	private Boolean enabled = false;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
