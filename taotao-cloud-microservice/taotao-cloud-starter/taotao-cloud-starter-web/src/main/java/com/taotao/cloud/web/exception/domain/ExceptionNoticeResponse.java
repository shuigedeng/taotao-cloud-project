package com.taotao.cloud.web.exception.domain;


/**
 * 异常消息通知响应
 */
public class ExceptionNoticeResponse {

	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 错误信息
	 */
	private String errMsg;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
