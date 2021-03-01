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
package com.taotao.cloud.demo.rocketmq.transactional.service.impl;

import com.taotao.cloud.demo.rocketmq.transactional.model.Order;
import com.taotao.cloud.demo.rocketmq.transactional.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * OrderServiceImpl
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

	@Override
	public void save(Order order) {
		System.out.println("============保存订单成功：" + order.getOrderId());
	}
}
