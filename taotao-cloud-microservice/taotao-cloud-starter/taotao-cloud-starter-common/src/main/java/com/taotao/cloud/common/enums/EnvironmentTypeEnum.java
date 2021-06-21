package com.taotao.cloud.common.enums;

/**
 * @author: chejiangyi
 * @version: 2019-05-27 13:44
 **/
public enum EnvironmentTypeEnum {
	//开发环境
	DEV("开发"),
	TEST("测试"),
	//预生产
	PRE("预生产"),
	//生产环境
	PRD("生产"),
	DOCKER("docker");

	private String name;

	EnvironmentTypeEnum(String name) {
		this.name = name;
	}
}
