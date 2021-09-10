package com.taotao.cloud.health.properties;

import com.taotao.cloud.common.utils.ContextUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 12:07
 **/
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
