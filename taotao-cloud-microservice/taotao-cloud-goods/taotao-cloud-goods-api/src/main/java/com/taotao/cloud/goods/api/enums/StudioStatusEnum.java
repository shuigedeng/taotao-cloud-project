package com.taotao.cloud.goods.api.enums;


/**
 * 直播间状态
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:32:28
 */
public enum StudioStatusEnum {

	/**
	 * 新建
	 */
	NEW("新建"),
	/**
	 * 开始
	 */
	START("开始"),
	/**
	 * 结束
	 */
	END("结束");

	private final String clientName;

	StudioStatusEnum(String des) {
		this.clientName = des;
	}

	public String clientName() {
		return this.clientName;
	}

}
