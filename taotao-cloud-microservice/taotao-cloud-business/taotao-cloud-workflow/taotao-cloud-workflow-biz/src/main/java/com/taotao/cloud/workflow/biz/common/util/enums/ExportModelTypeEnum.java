package com.taotao.cloud.workflow.biz.common.util.enums;

/**
 * 导入导出模板类型
 *
 */

public enum ExportModelTypeEnum {
	/**
	 * 功能设计
	 */
	Design(1,"design"),

	/**
	 * APP
	 */
	App(2,"app"),

	/**
	 *门户
	 */
	Portal(5,"portal");
	private final int code;
	private final String message;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	ExportModelTypeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
