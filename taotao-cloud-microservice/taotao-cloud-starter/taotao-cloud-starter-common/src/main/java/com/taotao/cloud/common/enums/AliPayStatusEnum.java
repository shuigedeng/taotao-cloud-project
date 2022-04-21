/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.common.enums;

/**
 * 支付状态
 */
public enum AliPayStatusEnum {

	/**
	 * 交易成功
	 */
	FINISHED("交易成功", "TRADE_FINISHED"),

	/**
	 * 支付成功
	 */
	SUCCESS("支付成功", "TRADE_SUCCESS"),

	/**
	 * 交易创建
	 */
	BUYER_PAY("交易创建", "WAIT_BUYER_PAY"),

	/**
	 * 交易关闭
	 */
	CLOSED("交易关闭", "TRADE_CLOSED");

	private String value;

	AliPayStatusEnum(String name, String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
