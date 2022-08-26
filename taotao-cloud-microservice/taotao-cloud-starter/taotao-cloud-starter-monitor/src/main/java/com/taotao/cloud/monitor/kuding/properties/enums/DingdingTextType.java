package com.taotao.cloud.monitor.kuding.properties.enums;

public enum DingdingTextType {

	TEXT("text"),
	MARKDOWN("markdown");

	private final String msgType;

	public String getMsgType() {
		return msgType;
	}

	private DingdingTextType(String msgType) {
		this.msgType = msgType;
	}

}
