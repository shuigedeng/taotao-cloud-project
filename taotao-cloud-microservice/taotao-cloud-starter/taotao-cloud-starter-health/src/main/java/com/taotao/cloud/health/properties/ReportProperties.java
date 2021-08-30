package com.taotao.cloud.health.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 12:07
 **/
@RefreshScope
@ConfigurationProperties(prefix = ReportProperties.PREFIX)
public class ReportProperties {

	public static final String PREFIX = "taotao.cloud.health.report";

	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
