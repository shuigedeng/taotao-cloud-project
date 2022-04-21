/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.channel.upyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taotao.cloud.sms.utils.StringUtils;

/**
 * 手机号发送短信的结果
 *
 * @author shuigedeng
 */
public class MessageId {

	/**
	 * 错误情况
	 */
	@JsonProperty("error_code")
	private String errorCode;

	/**
	 * 旧版本国内短信的 message 编号
	 */
	@JsonProperty("message_id")
	private Integer messageId;

	/**
	 * message 编号
	 */
	@JsonProperty("msg_id")
	private String msgId;

	/**
	 * 手机号
	 */
	@JsonProperty("mobile")
	private String mobile;

	/**
	 * 判断是否成功
	 *
	 * @return 是否成功
	 */
	public boolean succeed() {
		return StringUtils.isBlank(errorCode) && StringUtils.isNotBlank(msgId);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
