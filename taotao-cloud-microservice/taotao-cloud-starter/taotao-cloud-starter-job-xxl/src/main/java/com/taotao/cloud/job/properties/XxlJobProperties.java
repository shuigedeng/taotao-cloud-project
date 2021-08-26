package com.taotao.cloud.job.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * xxl-job配置
 *
 * @author lishangbu
 * @date 2020/9/14
 */
@RefreshScope
@ConfigurationProperties(prefix = XxlJobProperties.BASE_XXL_JOB_PREFIX)
public class XxlJobProperties {

	public static final String BASE_XXL_JOB_PREFIX = "taotao.cloud.xxl.job";
	public static final String ENABLED = "enabled";
	public static final String TRUE = "true";

	/**
	 * job开关,默认为false，非必填
	 */
	private boolean enabled = false;

	@NestedConfigurationProperty
	private XxlAdminProperties admin = new XxlAdminProperties();

	@NestedConfigurationProperty
	private XxlExecutorProperties executor = new XxlExecutorProperties();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public XxlAdminProperties getAdmin() {
		return admin;
	}

	public void setAdmin(XxlAdminProperties admin) {
		this.admin = admin;
	}

	public XxlExecutorProperties getExecutor() {
		return executor;
	}

	public void setExecutor(XxlExecutorProperties executor) {
		this.executor = executor;
	}
}
