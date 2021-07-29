package com.taotao.cloud.gateway.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证权限配置
 *
 * @date 2020-10-28
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {

	public static final String PREFIX = "taotao.cloud.gateway.security";

	public static final String[] ENDPOINTS = {
		"/actuator/**",
		"/v3/**",
		"/*/v3/**",
		"/fallback",
		"/favicon.ico",
		"/swagger-resources/**",
		"/webjars/**",
		"/druid/**",
		"/*/*.html",
		"/*/*.css",
		"/*/*.js",
		"/*.js",
		"/*.css",
		"/*.html",
		"/*/favicon.ico",
		"/*/api-docs",
		"/css/**",
		"/js/**",
		"/images/**"
	};

	/**
	 * 是否启用网关鉴权模式
	 */
	private Boolean enabled = true;

	/**
	 * 忽略URL，List列表形式
	 */
	private List<String> ignoreUrl = new ArrayList<>();

	/**
	 * 首次加载合并ENDPOINTS
	 */
	@PostConstruct
	public void initIgnoreUrl() {
		Collections.addAll(ignoreUrl, ENDPOINTS);
	}
}
