package com.taotao.cloud.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(value = TaoTaoCloudRequestProperties.BASE_REQUEST_PREFIX)
public class TaoTaoCloudRequestProperties {

	public static final String BASE_REQUEST_PREFIX = "taotao.cloud.request";

	/**
	 * 是否开启日志链路追踪
	 */
	private Boolean trace = true;

	/**
	 * 是否启用获取IP地址
	 */
	private Boolean ip = true;

	/**
	 * 是否启用增强模式
	 */
	private Boolean enhance = true;
}
