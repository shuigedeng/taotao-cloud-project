package com.taotao.cloud.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
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
}
