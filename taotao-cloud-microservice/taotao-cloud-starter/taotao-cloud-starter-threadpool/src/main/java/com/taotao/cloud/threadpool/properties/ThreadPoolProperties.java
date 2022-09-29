package com.taotao.cloud.threadpool.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 线程池配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:43:19
 */
@RefreshScope
@ConfigurationProperties(prefix = ThreadPoolProperties.PREFIX)
public class ThreadPoolProperties {

	public static final String PREFIX = "taotao.cloud.threadpool";

	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}

