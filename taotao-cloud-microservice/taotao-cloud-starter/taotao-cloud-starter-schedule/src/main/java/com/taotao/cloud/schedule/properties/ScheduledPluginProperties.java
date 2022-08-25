package com.taotao.cloud.schedule.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ScheduledPluginProperties
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 16:50:31
 */
@ConfigurationProperties(prefix = ScheduledPluginProperties.PREFIX)
public class ScheduledPluginProperties {

	public static final String PREFIX = "taotao.cloud.scheduled.plugin";

	//开启执行标志
	private Boolean executionFlag = false;
	//开启定时任务调度日志，日志文件是存在本地磁盘上的
	private Boolean executionLog = false;
	//开启基于zookeeper的集群模式
	private Boolean colony = false;
	//日志存放位置，不设置默认位置为程序同级目录下
	private String logPath = "";
	//zookeeper集群模式的定时任务服务名，相同名称的定时任务名称服务会被统一管理
	private String colonyName = "colony";

	public Boolean getExecutionFlag() {
		return executionFlag;
	}

	public void setExecutionFlag(Boolean executionFlag) {
		this.executionFlag = executionFlag;
	}

	public Boolean getExecutionLog() {
		return executionLog;
	}

	public void setExecutionLog(Boolean executionLog) {
		this.executionLog = executionLog;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public Boolean getColony() {
		return colony;
	}

	public void setColony(Boolean colony) {
		this.colony = colony;
	}

	public String getColonyName() {
		return colonyName;
	}

	public void setColonyName(String colonyName) {
		this.colonyName = colonyName;
	}
}
