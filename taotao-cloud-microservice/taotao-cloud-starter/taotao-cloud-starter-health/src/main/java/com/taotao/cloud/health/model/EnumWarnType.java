package com.taotao.cloud.health.model;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 20:16
 **/
public enum EnumWarnType {
	//报警级别
	ERROR(2, "错误"),
	WARN(1, "告警"),
	INFO(0, "通知");

	private int level;
	private String description;

	public String getDescription() {
		return description;
	}

	public int getLevel() {
		return level;
	}

	EnumWarnType(int level, String description) {
		this.description = description;
		this.level = level;
	}
}
