package com.taotao.cloud.order.api.enums.cart;

/**
 * 配送方式
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:21:40
 */
public enum DeliveryMethodEnum {

	/**
	 * "自提"
	 */
	SELF_PICK_UP("自提"),
	/**
	 * "同城配送"
	 */
	LOCAL_TOWN_DELIVERY("同城配送"),
	/**
	 * "物流"
	 */
	LOGISTICS("物流");

	private final String description;

	DeliveryMethodEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
