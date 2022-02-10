package com.taotao.cloud.web.schedule.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ScheduledProperties
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 16:51:07
 */
@ConfigurationProperties(prefix = ScheduledProperties.PREFIX)
public class ScheduledProperties {

	public static final String PREFIX = "taotao.cloud.web.scheduled";

	//是否开启
	private Boolean enabled = true;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
