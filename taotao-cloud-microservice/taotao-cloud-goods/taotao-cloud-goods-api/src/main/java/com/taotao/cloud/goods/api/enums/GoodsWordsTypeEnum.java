package com.taotao.cloud.goods.api.enums;

/**
 * 商品关键字类型
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-19 20:59:38
 */
public enum GoodsWordsTypeEnum {

	/**
	 * 系统
	 */
	SYSTEM("系统"),

	/**
	 * 平台
	 */
	PLATFORM("平台");

	private final String description;

	GoodsWordsTypeEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

}
