package com.taotao.cloud.sys.biz.controller.tools.jvm.service.dtos;

import lombok.Data;

@Data
public class VMParam {
    private String key;
    private String value;

    public VMParam() {
    }

    public VMParam(String key, String value) {
        this.key = key;
        this.value = value;
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
