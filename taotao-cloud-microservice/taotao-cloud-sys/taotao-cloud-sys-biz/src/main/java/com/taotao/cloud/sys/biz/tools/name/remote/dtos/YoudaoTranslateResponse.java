package com.taotao.cloud.sys.biz.tools.name.remote.dtos;


import java.util.Set;

public class YoudaoTranslateResponse {
    private String errorCode;
    private Set<String> translation;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Set<String> getTranslation() {
		return translation;
	}

	public void setTranslation(Set<String> translation) {
		this.translation = translation;
	}
}
