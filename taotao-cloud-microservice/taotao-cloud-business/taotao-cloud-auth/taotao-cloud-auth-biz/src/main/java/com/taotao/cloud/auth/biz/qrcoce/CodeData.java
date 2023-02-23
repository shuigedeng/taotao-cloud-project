package com.taotao.cloud.auth.biz.qrcoce;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeData {
	/**
	 * 二维码状态
	 */
	private CodeStatusEnum codeStatus;

	/**
	 * 提示消息
	 */
	private String message;

	/**
	 * 正式 token
	 */
	private String token;

	public CodeData(CodeStatusEnum codeStatus) {
		this.codeStatus = codeStatus;
	}

	public CodeData(CodeStatusEnum codeStatus, String message) {
		this.codeStatus = codeStatus;
		this.message = message;
	}

	public CodeData(CodeStatusEnum codeStatus, String message, String token) {
		this.codeStatus = codeStatus;
		this.message = message;
		this.token = token;
	}

}
