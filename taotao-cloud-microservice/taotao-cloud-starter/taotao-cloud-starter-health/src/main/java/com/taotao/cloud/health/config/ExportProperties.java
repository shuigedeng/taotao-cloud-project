package com.taotao.cloud.health.config;

import com.taotao.cloud.common.utils.ContextUtil;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 12:07
 **/
public class ExportProperties {

	public static ExportProperties Default() {
		return ContextUtil.getApplicationContext().getBean(ExportProperties.class);
	}

	//上传报表循环间隔时间 秒
	@Value("${bsf.health.export.timespan:30}")
	private int bsfHealthExportTimeSpan;

	@Value("${bsf.health.export.elk.destinations:${bsf.elk.destinations:}}")
	private String[] bsfElkDestinations;

	@Value("${bsf.health.export.elk.enabled:false}")
	private boolean bsfElkEnabled;

	@Value("${bsf.health.export.cat.enabled:false}")
	private boolean bsfCatEnabled;

	@Value("${bsf.health.export.cat.server.url:${cat.server.url:}}")
	private String bsfCatServerUrl;

	public ExportProperties() {
	}

	public ExportProperties(int bsfHealthExportTimeSpan, String[] bsfElkDestinations,
		boolean bsfElkEnabled, boolean bsfCatEnabled, String bsfCatServerUrl) {
		this.bsfHealthExportTimeSpan = bsfHealthExportTimeSpan;
		this.bsfElkDestinations = bsfElkDestinations;
		this.bsfElkEnabled = bsfElkEnabled;
		this.bsfCatEnabled = bsfCatEnabled;
		this.bsfCatServerUrl = bsfCatServerUrl;
	}

	public int getBsfHealthExportTimeSpan() {
		return bsfHealthExportTimeSpan;
	}

	public void setBsfHealthExportTimeSpan(int bsfHealthExportTimeSpan) {
		this.bsfHealthExportTimeSpan = bsfHealthExportTimeSpan;
	}

	public String[] getBsfElkDestinations() {
		return bsfElkDestinations;
	}

	public void setBsfElkDestinations(String[] bsfElkDestinations) {
		this.bsfElkDestinations = bsfElkDestinations;
	}

	public boolean isBsfElkEnabled() {
		return bsfElkEnabled;
	}

	public void setBsfElkEnabled(boolean bsfElkEnabled) {
		this.bsfElkEnabled = bsfElkEnabled;
	}

	public boolean isBsfCatEnabled() {
		return bsfCatEnabled;
	}

	public void setBsfCatEnabled(boolean bsfCatEnabled) {
		this.bsfCatEnabled = bsfCatEnabled;
	}

	public String getBsfCatServerUrl() {
		return bsfCatServerUrl;
	}

	public void setBsfCatServerUrl(String bsfCatServerUrl) {
		this.bsfCatServerUrl = bsfCatServerUrl;
	}
}
