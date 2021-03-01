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
package com.taotao.cloud.demo.rocketmq.comsumer.service;

import com.taotao.cloud.demo.rocketmq.comsumer.model.Order;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ReceiveService {

	/**
	 * 字符串消息
	 */
	@StreamListener(Sink.INPUT)
	public void receiveInput(String receiveMsg) {
		System.out.println("input receive: " + receiveMsg);
	}

	/**
	 * 对象消息
	 */
	@StreamListener("input2")
	public void receiveInput2(@Payload Order order) {
		System.out.println("input2 receive: " + order);
	}

	/**
	 * 通过spring.messaging对象来接收消息
	 */
	@StreamListener("input3")
	public void receiveInput3(Message msg) {
		System.out.println("input3 receive: " + msg);
	}
}
