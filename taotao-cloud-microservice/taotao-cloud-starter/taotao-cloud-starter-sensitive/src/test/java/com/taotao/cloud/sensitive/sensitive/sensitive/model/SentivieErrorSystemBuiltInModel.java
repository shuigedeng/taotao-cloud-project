package com.taotao.cloud.sensitive.sensitive.sensitive.model;


import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.SensitiveErrorSystemBuiltIn;

/**
 * 模拟错误的注解使用
 */
public class SentivieErrorSystemBuiltInModel {

	@SensitiveErrorSystemBuiltIn
	private String errorField;

	public String getErrorField() {
		return errorField;
	}

	public void setErrorField(String errorField) {
		this.errorField = errorField;
	}

}
