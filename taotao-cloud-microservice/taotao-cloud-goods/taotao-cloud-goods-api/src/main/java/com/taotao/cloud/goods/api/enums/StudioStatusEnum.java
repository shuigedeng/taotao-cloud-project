package com.taotao.cloud.goods.api.enums;


/**
 * 直播间状态
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
