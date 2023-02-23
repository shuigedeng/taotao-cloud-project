package com.taotao.cloud.operation.api.enums;


/**
 * 开关枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */

public enum SwitchEnum {
	/**
	 * 开启
	 */
	OPEN("开启"),
	/**
	 * 关闭
	 */
	CLOSE("关闭");
	private String description;

	SwitchEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
