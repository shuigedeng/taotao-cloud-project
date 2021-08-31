package com.taotao.cloud.health.properties;

import com.taotao.cloud.common.utils.ContextUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 12:07
 **/
@RefreshScope
@ConfigurationProperties(prefix = CheckProperties.PREFIX)
public class CheckProperties {

	public static final String PREFIX = "taotao.cloud.health.check";

	private boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
