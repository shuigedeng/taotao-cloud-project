package com.taotao.cloud.file.api.enums;


/**
 * app类型 安卓 IOS
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:47:12
 */
public enum FileTypeEnum {

	/**
	 * IOS
	 */
	IOS("IOS"),
	/**
	 * 安卓
	 */
	ANDROID("安卓");

	private final String description;

	FileTypeEnum(String description) {
		this.description = description;

	}

	public String getDescription() {
		return description;
	}

	public String description() {
		return this.description;
	}

}

