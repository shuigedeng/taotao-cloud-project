package com.taotao.cloud.gateway.properties;

import com.taotao.cloud.common.utils.ContextUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证权限配置
 *
 * @date 2020-10-28
 */
@RefreshScope
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
@AutoConfigureBefore(ApiProperties.class)
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
		ApiProperties apiProperties = ContextUtil.getBean(ApiProperties.class, true);
		if (Objects.nonNull(apiProperties)) {
			String baseUri = apiProperties.getBaseUri();
			ignoreUrl = ignoreUrl.stream().map(url -> baseUri + url)
				.collect(Collectors.toList());
			Collections.addAll(ignoreUrl, ENDPOINTS);
		}
	}

	public SecurityProperties() {
	}

	public SecurityProperties(Boolean enabled, List<String> ignoreUrl) {
		this.enabled = enabled;
		this.ignoreUrl = ignoreUrl;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getIgnoreUrl() {
		return ignoreUrl;
	}

	public void setIgnoreUrl(List<String> ignoreUrl) {
		this.ignoreUrl = ignoreUrl;
	}

	@Override
	public String toString() {
		return "SecurityProperties{" +
			"enabled=" + enabled +
			", ignoreUrl=" + ignoreUrl +
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
		SecurityProperties that = (SecurityProperties) o;
		return Objects.equals(enabled, that.enabled) && Objects.equals(ignoreUrl,
			that.ignoreUrl);
	}

	@Override
	public int hashCode() {
		return Objects.hash(enabled, ignoreUrl);
	}
}
