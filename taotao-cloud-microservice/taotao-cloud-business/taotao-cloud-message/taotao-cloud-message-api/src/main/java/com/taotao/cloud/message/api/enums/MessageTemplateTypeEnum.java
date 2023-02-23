package com.taotao.cloud.message.api.enums;

/**
 * 消息模板类型
 */
public enum MessageTemplateTypeEnum {

	//会员消息
	MEMBER("会员消息"),
	//店铺消息
	STORE("店铺消息"),
	//其他消息
	OTHER("其他消息");

	private final String description;

	MessageTemplateTypeEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}


}
