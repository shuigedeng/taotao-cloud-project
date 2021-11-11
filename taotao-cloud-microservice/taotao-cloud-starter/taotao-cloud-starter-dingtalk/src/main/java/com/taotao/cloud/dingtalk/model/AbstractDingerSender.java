/*
 * Copyright ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.model;

import com.taotao.cloud.dingtalk.entity.DingerCallback;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.exception.DingerException;
import com.taotao.cloud.dingtalk.properties.DingerProperties;
import com.taotao.cloud.dingtalk.support.CustomMessage;

/**
 * AbstractDingTalkSender
 *
 */
public abstract class AbstractDingerSender extends DingerHelper implements DingerSender {

	protected DingerProperties dingerProperties;
	protected DingerManagerBuilder dingTalkManagerBuilder;

	public AbstractDingerSender(DingerProperties dingerProperties,
		DingerManagerBuilder dingTalkManagerBuilder) {
		this.dingerProperties = dingerProperties;
		this.dingTalkManagerBuilder = dingTalkManagerBuilder;
	}

	/**
	 * 消息类型校验
	 *
	 * @param messageSubType 消息类型
	 * @return 消息生成器
	 */
	protected CustomMessage customMessage(MessageSubType messageSubType) {
		return messageSubType == MessageSubType.TEXT ? dingTalkManagerBuilder.getTextMessage()
			: dingTalkManagerBuilder.getMarkDownMessage();
	}

	/**
	 * 异常回调
	 *
	 * @param dingerId dingerId
	 * @param message  message
	 * @param ex       ex
	 * @param <T>      T
	 */
	protected <T> void exceptionCallback(String dingerId, T message, DingerException ex) {
		DingerCallback dkExCallable = new DingerCallback(dingerId, message, ex);
		dingTalkManagerBuilder.getDingerExceptionCallback().execute(dkExCallable);
	}
}
