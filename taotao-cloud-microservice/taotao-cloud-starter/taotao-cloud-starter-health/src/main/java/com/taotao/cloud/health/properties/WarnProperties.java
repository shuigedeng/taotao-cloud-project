package com.taotao.cloud.health.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = WarnProperties.PREFIX)
public class WarnProperties {

	public static final String PREFIX = "taotao.cloud.health.warn";

	/**
	 * 报警是否开启
	 */
	private boolean enabled = true;

	/**
	 * 报警消息缓存数量
	 */
	private int cacheCount = 3;

	/**
	 * 报警消息循环间隔时间 秒
	 */
	private int timeSpan = 10;

	/**
	 * 报警重复过滤时间间隔 分钟
	 */
	private int duplicateTimeSpan = 2;

	private boolean dingDingWarnEnabled = true;
	private boolean smsWarnEnabled = true;
	private boolean emailWarnEnabled = true;

	/**
	 * 钉钉报警过滤ip
	 */
	private String dingdingFilterIP;

	/**
	 * 飞书报警过滤ip
	 */
	private String flybookFilterIP;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getCacheCount() {
		return cacheCount;
	}

	public void setCacheCount(int cacheCount) {
		this.cacheCount = cacheCount;
	}

	public int getTimeSpan() {
		return timeSpan;
	}

	public void setTimeSpan(int timeSpan) {
		this.timeSpan = timeSpan;
	}

	public int getDuplicateTimeSpan() {
		return duplicateTimeSpan;
	}

	public void setDuplicateTimeSpan(int duplicateTimeSpan) {
		this.duplicateTimeSpan = duplicateTimeSpan;
	}

	public String getDingdingFilterIP() {
		return dingdingFilterIP;
	}

	public void setDingdingFilterIP(String dingdingFilterIP) {
		this.dingdingFilterIP = dingdingFilterIP;
	}

	public String getFlybookFilterIP() {
		return flybookFilterIP;
	}

	public void setFlybookFilterIP(String flybookFilterIP) {
		this.flybookFilterIP = flybookFilterIP;
	}

	public boolean isDingDingWarnEnabled() {
		return dingDingWarnEnabled;
	}

	public void setDingDingWarnEnabled(boolean dingDingWarnEnabled) {
		this.dingDingWarnEnabled = dingDingWarnEnabled;
	}

	public boolean isSmsWarnEnabled() {
		return smsWarnEnabled;
	}

	public void setSmsWarnEnabled(boolean smsWarnEnabled) {
		this.smsWarnEnabled = smsWarnEnabled;
	}

	public boolean isEmailWarnEnabled() {
		return emailWarnEnabled;
	}

	public void setEmailWarnEnabled(boolean emailWarnEnabled) {
		this.emailWarnEnabled = emailWarnEnabled;
	}
}
