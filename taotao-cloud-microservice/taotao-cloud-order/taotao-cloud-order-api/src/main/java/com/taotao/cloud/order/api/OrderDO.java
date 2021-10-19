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
package com.taotao.cloud.order.api;


import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:31:52
 */
public record OrderDO(
	/**
	 * 买家ID
	 */
	Long memberId,
	/**
	 * 订单编码
	 */
	String code,
	/**
	 * 订单主状态
	 */
	Integer mainStatus,
	/**
	 * 订单子状态
	 */
	Integer childStatus,
	/**
	 * 订单金额
	 */
	BigDecimal amount,
	/**
	 * 收货人姓名
	 */
	String receiverName) implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

}
