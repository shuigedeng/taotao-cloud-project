package com.taotao.cloud.monitor.kuding.properties.enums;

public enum ProjectEnviroment {

	DEVELOP("dev"), TEST("test"), PREVIEW("pre"), RELEASE("release"), ROLLBACK("roll_back");

	private final String name;

	private ProjectEnviroment(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
