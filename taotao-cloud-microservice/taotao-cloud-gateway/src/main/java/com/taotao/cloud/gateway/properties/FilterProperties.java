package com.taotao.cloud.gateway.properties;

import java.util.Objects;
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

	public FilterProperties() {
	}

	public FilterProperties(Boolean trace, Boolean log, Boolean gray, Boolean blacklist,
		Boolean sign) {
		this.trace = trace;
		this.log = log;
		this.gray = gray;
		this.blacklist = blacklist;
		this.sign = sign;
	}

	@Override
	public String
	toString() {
		return "FilterProperties{" +
			"trace=" + trace +
			", log=" + log +
			", gray=" + gray +
			", blacklist=" + blacklist +
			", sign=" + sign +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FilterProperties that = (FilterProperties) o;
		return Objects.equals(trace, that.trace) && Objects.equals(log, that.log)
			&& Objects.equals(gray, that.gray) && Objects.equals(blacklist,
			that.blacklist) && Objects.equals(sign, that.sign);
	}

	@Override
	public int hashCode() {
		return Objects.hash(trace, log, gray, blacklist, sign);
	}

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
