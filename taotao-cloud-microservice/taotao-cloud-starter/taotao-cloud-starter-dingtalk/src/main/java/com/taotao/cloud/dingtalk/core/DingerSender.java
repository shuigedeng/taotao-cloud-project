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
package com.taotao.cloud.dingtalk.core;

import com.taotao.cloud.dingtalk.entity.DingerRequest;
import com.taotao.cloud.dingtalk.entity.DingerResponse;
import com.taotao.cloud.dingtalk.enums.DingerType;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.properties.DingerProperties;

/**
 * DingTalk Sender
 *
 * @author Jaemon
 * @since 1.0
 */
public interface DingerSender {

	/**
	 * 发送消息到指定群
	 *
	 * <pre>
	 *     使用配置的默认钉钉机器人, {@link DingerProperties#getDefaultDinger()}
	 * </pre>
	 *
	 * @param messageSubType 消息类型{@link MessageSubType}
	 * @param request        请求体 {@link DingerRequest}
	 * @return 响应报文
	 */
	DingerResponse send(MessageSubType messageSubType, DingerRequest request);

	/**
	 * 发送消息到指定群
	 *
	 * @param dingerType     Dinger类型 {@link DingerType}
	 * @param messageSubType 消息类型{@link MessageSubType}
	 * @param request        请求体 {@link DingerRequest}
	 * @return 响应报文
	 */
	DingerResponse send(DingerType dingerType, MessageSubType messageSubType,
		DingerRequest request);
}
