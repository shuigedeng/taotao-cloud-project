package com.taotao.cloud.goods.api.enums;

/**
 * 草稿商品保存类型
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:32:07
 */
public enum DraftGoodsSaveTypeEnum {

	/**
	 * "草稿"
	 */
	DRAFT("草稿"),
	/**
	 * "模版"
	 */
	TEMPLATE("模版");

	private final String description;

	DraftGoodsSaveTypeEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

}
