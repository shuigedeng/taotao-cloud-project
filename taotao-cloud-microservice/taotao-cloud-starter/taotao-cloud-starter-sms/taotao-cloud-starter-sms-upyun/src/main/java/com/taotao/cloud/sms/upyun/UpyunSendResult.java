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
package com.taotao.cloud.sms.upyun;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * 发送响应
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:52:25
 */
public class UpyunSendResult {

	/**
	 * 所有手机号发送短信的结果
	 */
	@JsonProperty("message_ids")
	private Collection<MessageId> messageIds;


	public Collection<MessageId> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(Collection<MessageId> messageIds) {
		this.messageIds = messageIds;
	}
}
