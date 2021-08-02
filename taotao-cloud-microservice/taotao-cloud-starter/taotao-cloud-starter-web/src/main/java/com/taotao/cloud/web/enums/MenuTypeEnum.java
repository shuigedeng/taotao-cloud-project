package com.taotao.cloud.web.enums;


/**
 * 菜单类型
 *
 * @author aristotle
 */
public enum MenuTypeEnum {

	/**
	 * 目录
	 */
	DIR("0", "目录"),
	/**
	 * 菜单
	 */
	MENU("1", "菜单"),
	/**
	 * 按钮
	 */
	BUTTON("2", "按钮");

	private final String code;

	private final String message;

	MenuTypeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
