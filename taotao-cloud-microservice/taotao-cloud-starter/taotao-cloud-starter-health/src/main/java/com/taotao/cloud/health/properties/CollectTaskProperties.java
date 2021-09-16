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
 * CollectTaskProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:03:40
 */
@RefreshScope
@ConfigurationProperties(prefix = CollectTaskProperties.PREFIX)
public class CollectTaskProperties {

	public static final String PREFIX = "taotao.cloud.health.collect";

	private boolean uncatchEnabled = true;

	private boolean xxljobEnabled = true;

	private int xxljobTimeSpan = 20;

	private boolean webServerEnabled = true;

	private int webServerTimeSpan = 20;

	private boolean monitorThreadEnabled = true;

	private int monitorThreadTimeSpan = 20;

	private boolean asyncThreadEnabled = true;

	private int asyncThreadTimeSpan = 20;

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

	private boolean nacosEnabled = true;
	private int nacosTimeSpan = 20;


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

	public boolean isMonitorThreadEnabled() {
		return monitorThreadEnabled;
	}

	public void setMonitorThreadEnabled(boolean monitorThreadEnabled) {
		this.monitorThreadEnabled = monitorThreadEnabled;
	}

	public int getMonitorThreadTimeSpan() {
		return monitorThreadTimeSpan;
	}

	public void setMonitorThreadTimeSpan(int monitorThreadTimeSpan) {
		this.monitorThreadTimeSpan = monitorThreadTimeSpan;
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

	public boolean isWebServerEnabled() {
		return webServerEnabled;
	}

	public void setWebServerEnabled(boolean webServerEnabled) {
		this.webServerEnabled = webServerEnabled;
	}

	public int getWebServerTimeSpan() {
		return webServerTimeSpan;
	}

	public void setWebServerTimeSpan(int webServerTimeSpan) {
		this.webServerTimeSpan = webServerTimeSpan;
	}

	public boolean isNacosEnabled() {
		return nacosEnabled;
	}

	public void setNacosEnabled(boolean nacosEnabled) {
		this.nacosEnabled = nacosEnabled;
	}

	public int getNacosTimeSpan() {
		return nacosTimeSpan;
	}

	public void setNacosTimeSpan(int nacosTimeSpan) {
		this.nacosTimeSpan = nacosTimeSpan;
	}

	public boolean isAsyncThreadEnabled() {
		return asyncThreadEnabled;
	}

	public void setAsyncThreadEnabled(boolean asyncThreadEnabled) {
		this.asyncThreadEnabled = asyncThreadEnabled;
	}

	public int getAsyncThreadTimeSpan() {
		return asyncThreadTimeSpan;
	}

	public void setAsyncThreadTimeSpan(int asyncThreadTimeSpan) {
		this.asyncThreadTimeSpan = asyncThreadTimeSpan;
	}
}
