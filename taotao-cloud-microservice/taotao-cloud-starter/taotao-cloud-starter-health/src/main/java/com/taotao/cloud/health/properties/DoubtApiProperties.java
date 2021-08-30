package com.taotao.cloud.health.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 12:07
 **/
@RefreshScope
@ConfigurationProperties(prefix = DoubtApiProperties.PREFIX)
public class DoubtApiProperties {

	public static final String PREFIX = "taotao.cloud.health.doubtapi";

	private boolean enabled = false;

	//增长内存统计阈值，默认3M
	private  int threshold = 3 * 1024 * 1024;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
}
