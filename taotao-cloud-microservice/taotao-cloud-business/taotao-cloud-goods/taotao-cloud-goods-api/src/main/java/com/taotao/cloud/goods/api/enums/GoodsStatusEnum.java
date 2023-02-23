package com.taotao.cloud.goods.api.enums;

/**
 * 商品类型枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:32:20
 */
public enum GoodsStatusEnum {
	/**
	 * 上架
	 */
	UPPER("上架"),
	/**
	 * 下架
	 */
	DOWN("下架");

	private final String description;

	GoodsStatusEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}
}
