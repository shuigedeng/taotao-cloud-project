package com.taotao.cloud.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 21:54
 **/
@RefreshScope
@ConfigurationProperties(prefix = ThreadPoolProperties.PREFIX)
public class ThreadPoolProperties {

	public static final String PREFIX = "taotao.cloud.core.threadpool";

	private int threadPoolMaxSiz = 500;
	private int ThreadPoolMinSize = 0;

	public int getThreadPoolMaxSiz() {
		return threadPoolMaxSiz;
	}

	public void setThreadPoolMaxSiz(int threadPoolMaxSiz) {
		this.threadPoolMaxSiz = threadPoolMaxSiz;
	}

	public int getThreadPoolMinSize() {
		return ThreadPoolMinSize;
	}

	public void setThreadPoolMinSize(int threadPoolMinSize) {
		ThreadPoolMinSize = threadPoolMinSize;
	}
}
