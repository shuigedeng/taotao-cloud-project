package com.taotao.cloud.security.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证权限配置
 *
 * @date 2020-10-28
 */
@RefreshScope
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {

	public static final String PREFIX = "taotao.cloud.security";

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
		"/health/**",
		"/css/**",
		"/js/**",
		"/k8s/**",
		"/k8s",
		"/images/**",
		"/doc/**",
		"/swagger-ui.html",
		"/favicon.ico",
		"/actuator/**",
		"/index",
		"/index.html",
		"/doc.html",
	};

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

	public List<String> getIgnoreUrl() {
		return ignoreUrl;
	}

	public void setIgnoreUrl(List<String> ignoreUrl) {
		this.ignoreUrl = ignoreUrl;
	}

}
