package com.taotao.cloud.health.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@RefreshScope
@ConfigurationProperties(prefix = CollectTaskProperties.PREFIX)
public class CollectTaskProperties {

	public static final String PREFIX = "taotao.cloud.health.collect";

	private boolean uncatchEnabled = true;

	private boolean xxljobEnabled = true;

	private int xxljobTimeSpan = 20;

	private boolean tomcatEnabled = true;

	private int tomcatTimeSpan = 20;

	private boolean threadEnabled = true;

	private int threadTimeSpan = 20;

	private boolean threadPollEnabled = true;

	private int threadPollTimeSpan = 20;

	private boolean networkEnabled = true;

	private int networkTimeSpan = 10;

	private boolean mybatisEnabled = true;

	private int mybatisTimeSpan = 20;

	private boolean memeryEnabled = true;

	private int memeryTimeSpan = 10;

	private boolean logStatisticEnabled = true;

	private int logStatisticTimeSpan = 20;

	private boolean jedisEnabled = true;

	private int jedisTimeSpan = 20;

	private boolean ioEnabled = true;
	private int ioTimeSpan = 20;

	private boolean httpPoolEnabled = true;
	private int httpPoolTimeSpan = 20;

	private boolean elkEnabled = true;
	private int elkTimeSpan = 20;

	private boolean doubtApiEnabled = true;
	private int doubtApiTimeSpan = 20;

	private boolean datasourceEnabled = true;
	private int datasourcTimeSpan = 20;

	private boolean cpuEnabled = true;
	private int cpuTimeSpan = 20;


	public boolean isThreadPollEnabled() {
		return threadPollEnabled;
	}

	public void setThreadPollEnabled(boolean threadPollEnabled) {
		this.threadPollEnabled = threadPollEnabled;
	}

	public int getThreadPollTimeSpan() {
		return threadPollTimeSpan;
	}

	public void setThreadPollTimeSpan(int threadPollTimeSpan) {
		this.threadPollTimeSpan = threadPollTimeSpan;
	}

	public boolean isCpuEnabled() {
		return cpuEnabled;
	}

	public void setCpuEnabled(boolean cpuEnabled) {
		this.cpuEnabled = cpuEnabled;
	}

	public int getCpuTimeSpan() {
		return cpuTimeSpan;
	}

	public void setCpuTimeSpan(int cpuTimeSpan) {
		this.cpuTimeSpan = cpuTimeSpan;
	}

	public boolean isDatasourceEnabled() {
		return datasourceEnabled;
	}

	public void setDatasourceEnabled(boolean datasourceEnabled) {
		this.datasourceEnabled = datasourceEnabled;
	}

	public int getDatasourcTimeSpan() {
		return datasourcTimeSpan;
	}

	public void setDatasourcTimeSpan(int datasourcTimeSpan) {
		this.datasourcTimeSpan = datasourcTimeSpan;
	}

	public boolean isDoubtApiEnabled() {
		return doubtApiEnabled;
	}

	public void setDoubtApiEnabled(boolean doubtApiEnabled) {
		this.doubtApiEnabled = doubtApiEnabled;
	}

	public int getDoubtApiTimeSpan() {
		return doubtApiTimeSpan;
	}

	public void setDoubtApiTimeSpan(int doubtApiTimeSpan) {
		this.doubtApiTimeSpan = doubtApiTimeSpan;
	}

	public boolean isElkEnabled() {
		return elkEnabled;
	}

	public void setElkEnabled(boolean elkEnabled) {
		this.elkEnabled = elkEnabled;
	}

	public int getElkTimeSpan() {
		return elkTimeSpan;
	}

	public void setElkTimeSpan(int elkTimeSpan) {
		this.elkTimeSpan = elkTimeSpan;
	}

	public boolean isHttpPoolEnabled() {
		return httpPoolEnabled;
	}

	public void setHttpPoolEnabled(boolean httpPoolEnabled) {
		this.httpPoolEnabled = httpPoolEnabled;
	}

	public int getHttpPoolTimeSpan() {
		return httpPoolTimeSpan;
	}

	public void setHttpPoolTimeSpan(int httpPoolTimeSpan) {
		this.httpPoolTimeSpan = httpPoolTimeSpan;
	}

	public boolean isIoEnabled() {
		return ioEnabled;
	}

	public void setIoEnabled(boolean ioEnabled) {
		this.ioEnabled = ioEnabled;
	}


	public int getIoTimeSpan() {
		return ioTimeSpan;
	}

	public void setIoTimeSpan(int ioTimeSpan) {
		this.ioTimeSpan = ioTimeSpan;
	}

	public boolean isJedisEnabled() {
		return jedisEnabled;
	}

	public void setJedisEnabled(boolean jedisEnabled) {
		this.jedisEnabled = jedisEnabled;
	}

	public int getJedisTimeSpan() {
		return jedisTimeSpan;
	}

	public void setJedisTimeSpan(int jedisTimeSpan) {
		this.jedisTimeSpan = jedisTimeSpan;
	}

	public boolean isLogStatisticEnabled() {
		return logStatisticEnabled;
	}

	public void setLogStatisticEnabled(boolean logStatisticEnabled) {
		this.logStatisticEnabled = logStatisticEnabled;
	}

	public int getLogStatisticTimeSpan() {
		return logStatisticTimeSpan;
	}

	public void setLogStatisticTimeSpan(int logStatisticTimeSpan) {
		this.logStatisticTimeSpan = logStatisticTimeSpan;
	}

	public boolean isMemeryEnabled() {
		return memeryEnabled;
	}

	public void setMemeryEnabled(boolean memeryEnabled) {
		this.memeryEnabled = memeryEnabled;
	}

	public int getMemeryTimeSpan() {
		return memeryTimeSpan;
	}

	public void setMemeryTimeSpan(int memeryTimeSpan) {
		this.memeryTimeSpan = memeryTimeSpan;
	}

	public boolean isMybatisEnabled() {
		return mybatisEnabled;
	}

	public void setMybatisEnabled(boolean mybatisEnabled) {
		this.mybatisEnabled = mybatisEnabled;
	}

	public int getMybatisTimeSpan() {
		return mybatisTimeSpan;
	}

	public void setMybatisTimeSpan(int mybatisTimeSpan) {
		this.mybatisTimeSpan = mybatisTimeSpan;
	}

	public boolean isNetworkEnabled() {
		return networkEnabled;
	}

	public void setNetworkEnabled(boolean networkEnabled) {
		this.networkEnabled = networkEnabled;
	}

	public int getNetworkTimeSpan() {
		return networkTimeSpan;
	}

	public void setNetworkTimeSpan(int networkTimeSpan) {
		this.networkTimeSpan = networkTimeSpan;
	}

	public boolean isThreadEnabled() {
		return threadEnabled;
	}

	public void setThreadEnabled(boolean threadEnabled) {
		this.threadEnabled = threadEnabled;
	}

	public int getThreadTimeSpan() {
		return threadTimeSpan;
	}

	public void setThreadTimeSpan(int threadTimeSpan) {
		this.threadTimeSpan = threadTimeSpan;
	}

	public boolean isUncatchEnabled() {
		return uncatchEnabled;
	}

	public void setUncatchEnabled(boolean uncatchEnabled) {
		this.uncatchEnabled = uncatchEnabled;
	}

	public boolean isXxljobEnabled() {
		return xxljobEnabled;
	}

	public void setXxljobEnabled(boolean xxljobEnabled) {
		this.xxljobEnabled = xxljobEnabled;
	}

	public int getXxljobTimeSpan() {
		return xxljobTimeSpan;
	}

	public void setXxljobTimeSpan(int xxljobTimeSpan) {
		this.xxljobTimeSpan = xxljobTimeSpan;
	}

	public boolean isTomcatEnabled() {
		return tomcatEnabled;
	}

	public void setTomcatEnabled(boolean tomcatEnabled) {
		this.tomcatEnabled = tomcatEnabled;
	}

	public int getTomcatTimeSpan() {
		return tomcatTimeSpan;
	}

	public void setTomcatTimeSpan(int tomcatTimeSpan) {
		this.tomcatTimeSpan = tomcatTimeSpan;
	}
}
