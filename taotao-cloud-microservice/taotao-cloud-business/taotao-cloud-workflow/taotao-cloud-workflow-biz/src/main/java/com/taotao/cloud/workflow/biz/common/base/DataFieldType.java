package com.taotao.cloud.workflow.biz.common.base;
/**
 * 数据权限字段类型
 *
 */
public enum DataFieldType {
	/**
	 * 浮点型
	 */
	Double("Double"),
	/**
	 * 字符型
	 */
	Varchar("String"),

	/**
	 * 数值型
	 */
	Number("Int32");

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	DataFieldType(String message) {
		this.message = message;
	}
}
