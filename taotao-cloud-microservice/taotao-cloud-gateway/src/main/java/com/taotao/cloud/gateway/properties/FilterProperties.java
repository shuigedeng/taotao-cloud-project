package com.taotao.cloud.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = FilterProperties.PREFIX)
public class FilterProperties {

	public static final String PREFIX = "taotao.cloud.gateway.filter";

	/**
	 * 是否开启日志链路追踪
	 */
	private Boolean trace = true;

	/**
	 * 是否开启日志打印
	 */
	private Boolean log = true;

	/**
	 * 是否启用灰度发布
	 */
	private Boolean gray = true;

	/**
	 * 是否启用黑名单
	 */
	private Boolean blacklist = true;

	/**
	 * 是否启用黑名单
	 */
	private Boolean sign = true;

	public Boolean getTrace() {
		return trace;
	}

	public void setTrace(Boolean trace) {
		this.trace = trace;
	}

	public Boolean getLog() {
		return log;
	}

	public void setLog(Boolean log) {
		this.log = log;
	}

	public Boolean getGray() {
		return gray;
	}

	public void setGray(Boolean gray) {
		this.gray = gray;
	}

	public Boolean getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(Boolean blacklist) {
		this.blacklist = blacklist;
	}

	public Boolean getSign() {
		return sign;
	}

	public void setSign(Boolean sign) {
		this.sign = sign;
	}
}
