package com.taotao.cloud.prometheus.properties.enums;

public enum EmailTextType {

	TEXT("text");

	private final String value;

	public String getValue() {
		return value;
	}

	private EmailTextType(String value) {
		this.value = value;
	}

}
