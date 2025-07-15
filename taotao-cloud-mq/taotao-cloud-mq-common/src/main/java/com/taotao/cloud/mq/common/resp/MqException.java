package com.taotao.cloud.mq.common.resp;


import com.taotao.cloud.mq.common.dto.RespCode;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqException extends RuntimeException implements RespCode {

	private final RespCode respCode;

	public MqException(RespCode respCode) {
		this.respCode = respCode;
	}

	public MqException(String message, RespCode respCode) {
		super(message);
		this.respCode = respCode;
	}

	public MqException(String message, Throwable cause, RespCode respCode) {
		super(message, cause);
		this.respCode = respCode;
	}

	public MqException(Throwable cause, RespCode respCode) {
		super(cause);
		this.respCode = respCode;
	}

	public MqException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace, RespCode respCode) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.respCode = respCode;
	}

	@Override
	public String getCode() {
		return this.respCode.getCode();
	}

	@Override
	public String getMsg() {
		return this.respCode.getMsg();
	}
}
