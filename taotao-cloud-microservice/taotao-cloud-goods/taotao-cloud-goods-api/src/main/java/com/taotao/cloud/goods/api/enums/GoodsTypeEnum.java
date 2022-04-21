package com.taotao.cloud.goods.api.enums;

/**
 * 商品类型
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-19 20:59:38
 */
public enum GoodsTypeEnum {

	/**
	 * "实物商品"
	 */
	PHYSICAL_GOODS("实物商品"),
	/**
	 * "虚拟商品"
	 */
	VIRTUAL_GOODS("虚拟商品"),
	/**
	 * "电子卡券"
	 */
	E_COUPON("电子卡券");

	private final String description;

	GoodsTypeEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

}
