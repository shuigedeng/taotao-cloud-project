package com.taotao.cloud.health.properties;

import com.taotao.cloud.common.utils.ContextUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = HealthProperties.PREFIX)
public class HealthProperties {

	public static final String PREFIX = "taotao.cloud.health";

	/**
	 * 是否开启健康检查
	 */
	private boolean enabled = true;

	/**
	 * 健康检查时间间隔 秒
	 */
	private int healthTimeSpan = 10;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getHealthTimeSpan() {
		return healthTimeSpan;
	}

	public void setHealthTimeSpan(int healthTimeSpan) {
		this.healthTimeSpan = healthTimeSpan;
	}
}
