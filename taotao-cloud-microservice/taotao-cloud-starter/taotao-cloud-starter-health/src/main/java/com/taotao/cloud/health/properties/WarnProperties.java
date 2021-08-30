package com.taotao.cloud.health.properties;

import com.taotao.cloud.common.utils.ContextUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = WarnProperties.PREFIX)
public class WarnProperties {

	public static final String PREFIX = "taotao.cloud.health.warn";

	public static WarnProperties Default() {
		return ContextUtil.getApplicationContext().getBean(WarnProperties.class);
	}

	/**
	 * 报警是否开启
	 */
	private boolean enabled = false;

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

	/**
	 * 钉钉报警系统token
	 */
	private String dingdingSystemAccessToken;

	/**
	 * 钉钉报警项目token
	 */
	private String dingdingProjectAccessToken;

	/**
	 * 钉钉报警过滤ip
	 */
	private String dingdingFilterIP;

	/**
	 * 飞书报警系统token
	 */
	private String flybookSystemAccessToken;

	/**
	 * 飞书报警项目token
	 */
	private String flybookProjectAccessToken;

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

	public String getDingdingSystemAccessToken() {
		return dingdingSystemAccessToken;
	}

	public void setDingdingSystemAccessToken(String dingdingSystemAccessToken) {
		this.dingdingSystemAccessToken = dingdingSystemAccessToken;
	}

	public String getDingdingProjectAccessToken() {
		return dingdingProjectAccessToken;
	}

	public void setDingdingProjectAccessToken(String dingdingProjectAccessToken) {
		this.dingdingProjectAccessToken = dingdingProjectAccessToken;
	}

	public String getDingdingFilterIP() {
		return dingdingFilterIP;
	}

	public void setDingdingFilterIP(String dingdingFilterIP) {
		this.dingdingFilterIP = dingdingFilterIP;
	}

	public String getFlybookSystemAccessToken() {
		return flybookSystemAccessToken;
	}

	public void setFlybookSystemAccessToken(String flybookSystemAccessToken) {
		this.flybookSystemAccessToken = flybookSystemAccessToken;
	}

	public String getFlybookProjectAccessToken() {
		return flybookProjectAccessToken;
	}

	public void setFlybookProjectAccessToken(String flybookProjectAccessToken) {
		this.flybookProjectAccessToken = flybookProjectAccessToken;
	}

	public String getFlybookFilterIP() {
		return flybookFilterIP;
	}

	public void setFlybookFilterIP(String flybookFilterIP) {
		this.flybookFilterIP = flybookFilterIP;
	}
}
