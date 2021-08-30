package com.taotao.cloud.health.model;


import com.taotao.cloud.common.exception.BaseException;

/**
 * @author: chejiangyi
 * @version: 2019-07-24 15:33
 **/
public class HealthException extends BaseException {

	public HealthException(Throwable exp) {
		super(exp);
	}

	public HealthException(String message) {
		super(message);
	}

	public HealthException(String message, Throwable cause) {
		super(message, cause);
	}
}
