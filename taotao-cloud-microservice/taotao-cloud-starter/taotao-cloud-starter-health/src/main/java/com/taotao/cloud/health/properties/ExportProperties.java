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
 * ExportProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:05:07
 */
@RefreshScope
@ConfigurationProperties(prefix = ExportProperties.PREFIX)
public class ExportProperties {

	public static final String PREFIX = "taotao.cloud.health.export";

	private boolean enabled = true;

	//上传报表循环间隔时间 秒
	private int exportTimeSpan = 30;

	private String[] elkDestinations;

	private boolean elkEnabled = false;

	private boolean catEnabled = false;

	private String catServerUrl;


	public int getExportTimeSpan() {
		return exportTimeSpan;
	}

	public void setExportTimeSpan(int exportTimeSpan) {
		this.exportTimeSpan = exportTimeSpan;
	}

	public String[] getElkDestinations() {
		return elkDestinations;
	}

	public void setElkDestinations(String[] elkDestinations) {
		this.elkDestinations = elkDestinations;
	}

	public boolean isElkEnabled() {
		return elkEnabled;
	}

	public void setElkEnabled(boolean elkEnabled) {
		this.elkEnabled = elkEnabled;
	}

	public boolean isCatEnabled() {
		return catEnabled;
	}

	public void setCatEnabled(boolean catEnabled) {
		this.catEnabled = catEnabled;
	}

	public String getCatServerUrl() {
		return catServerUrl;
	}

	public void setCatServerUrl(String catServerUrl) {
		this.catServerUrl = catServerUrl;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
