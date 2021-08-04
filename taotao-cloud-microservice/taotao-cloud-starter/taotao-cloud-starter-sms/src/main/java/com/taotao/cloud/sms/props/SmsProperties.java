package com.taotao.cloud.sms.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {

	public static final String PREFIX = "taotao.cloud.sms";

	private boolean enabled = false;

	private String type = "ali";

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
