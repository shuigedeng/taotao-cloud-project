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

	private Boolean enabled  = true;

	/**
	 * 是否是演示站点
	 */
	private Boolean isDemoSite = false;

	/**
	 * 测试模式 验证码短信为6个1
	 */
	private Boolean isTestModel = false;

	/**
	 * 脱敏级别： 0：不做脱敏处理 1：管理端用户手机号等信息脱敏 2：商家端信息脱敏（为2时，表示管理端，商家端同时脱敏）
	 */
	private Integer sensitiveLevel = 0;


	public Boolean getDemoSite() {
		if (isDemoSite == null) {
			return false;
		}
		return isDemoSite;
	}

	public Boolean getTestModel() {
		if (isTestModel == null) {
			return false;
		}
		return isTestModel;
	}

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

	public void setDemoSite(Boolean demoSite) {
		isDemoSite = demoSite;
	}

	public void setTestModel(Boolean testModel) {
		isTestModel = testModel;
	}

	public void setSensitiveLevel(Integer sensitiveLevel) {
		this.sensitiveLevel = sensitiveLevel;
	}

}
