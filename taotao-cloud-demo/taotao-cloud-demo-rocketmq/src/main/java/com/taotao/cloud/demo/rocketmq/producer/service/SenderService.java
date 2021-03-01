/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.demo.rocketmq.producer.service;

import com.taotao.cloud.demo.rocketmq.producer.config.RocketMqConfig;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * SenderService
 */
@Service
public class SenderService {

	@Autowired
	private RocketMqConfig.MySource source;

	/**
	 * 发送字符消息
	 */
	public void send(String msg) {
		source.output().send(MessageBuilder.withPayload(msg).build());
	}

	/**
	 * 发送带tag的对象消息
	 */
	public <T> void sendWithTags(T msg, String tag) {
		Message message = MessageBuilder.withPayload(msg)
			.setHeader(MessageConst.PROPERTY_TAGS, tag)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build();
		source.output().send(message);
	}
}
