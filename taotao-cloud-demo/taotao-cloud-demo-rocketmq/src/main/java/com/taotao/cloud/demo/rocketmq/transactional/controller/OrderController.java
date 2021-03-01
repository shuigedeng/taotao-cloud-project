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
package com.taotao.cloud.demo.rocketmq.transactional.controller;

import cn.hutool.core.util.RandomUtil;
import com.taotao.cloud.demo.rocketmq.transactional.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController
 */
@RestController
public class OrderController {

	private final Source source;

	@Autowired
	public OrderController(Source source) {
		this.source = source;
	}

	/**
	 * 正常情况
	 */
	@GetMapping("/success")
	public String success() {
		Order order = new Order();
		order.setOrderId(11111L);
		order.setOrderNo(RandomUtil.randomString(4));

		Message message = MessageBuilder
			.withPayload(order)
			.setHeader("orderId", order.getOrderId())
			.build();
		//发送半消息
		source.output().send(message);
		return "下单成功";
	}

	/**
	 * 发送消息失败
	 */
	@GetMapping("/produceError")
	public String produceError() {
		Order order = new Order();
		order.setOrderId(11111L);
		order.setOrderNo(RandomUtil.randomString(4));

		Message message = MessageBuilder
			.withPayload(order)
			.setHeader("orderId", order.getOrderId())
			.setHeader("produceError", "1")
			.build();
		//发送半消息
		source.output().send(message);
		return "发送消息失败";
	}

	/**
	 * 消费消息失败
	 */
	@GetMapping("/consumeError")
	public String consumeError() {
		Order order = new Order();
		order.setOrderId(11111L);
		order.setOrderNo(RandomUtil.randomString(4));

		Message message = MessageBuilder
			.withPayload(order)
			.setHeader("orderId", order.getOrderId())
			.setHeader("consumeError", "1")
			.build();
		//发送半消息
		source.output().send(message);
		return "消费消息失败";
	}
}
