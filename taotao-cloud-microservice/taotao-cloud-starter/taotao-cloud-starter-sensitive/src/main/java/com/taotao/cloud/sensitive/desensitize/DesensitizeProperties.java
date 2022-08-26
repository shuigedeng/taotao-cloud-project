package com.taotao.cloud.sensitive.desensitize;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 脱敏设置
 */
@RefreshScope
@ConfigurationProperties(prefix = DesensitizeProperties.PREFIX)
public class DesensitizeProperties {

	public static final String PREFIX = "taotao.cloud.sensitive";

	private Boolean enabled = true;

	/**
	 * 脱敏级别： 0：不做脱敏处理 1：管理端用户手机号等信息脱敏 2：商家端信息脱敏（为2时，表示管理端，商家端同时脱敏）
	 */
	private Integer sensitiveLevel = 0;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getSensitiveLevel() {
		if (sensitiveLevel == null) {
			return 0;
		}
		return sensitiveLevel;
	}

	public void setSensitiveLevel(Integer sensitiveLevel) {
		this.sensitiveLevel = sensitiveLevel;
	}

}
