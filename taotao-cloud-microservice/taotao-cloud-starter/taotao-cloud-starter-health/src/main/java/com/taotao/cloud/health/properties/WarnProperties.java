/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.health.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * WarnProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:06:03
 */
@RefreshScope
@ConfigurationProperties(prefix = WarnProperties.PREFIX)
public class WarnProperties {

	public static final String PREFIX = "taotao.cloud.health.warn";

	/**
	 * 报警消息缓存数量
	 */
	private int cacheCount = 3;

	/**
	 * 报警消息循环间隔时间 秒
	 */
	private int timeSpan = 30;

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

	public boolean getSmsWarnEnabled() {
		return smsWarnEnabled;
	}

	public void setSmsWarnEnabled(boolean smsWarnEnabled) {
		this.smsWarnEnabled = smsWarnEnabled;
	}

	public boolean getEmailWarnEnabled() {
		return emailWarnEnabled;
	}

	public void setEmailWarnEnabled(boolean emailWarnEnabled) {
		this.emailWarnEnabled = emailWarnEnabled;
	}
}
