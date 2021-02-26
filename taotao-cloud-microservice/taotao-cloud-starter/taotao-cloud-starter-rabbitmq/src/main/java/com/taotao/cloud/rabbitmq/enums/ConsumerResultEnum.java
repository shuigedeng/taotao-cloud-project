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
package com.taotao.cloud.rabbitmq.enums;

/**
 * ConsumerResultEnum
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/9/29 15:57
 */
public enum ConsumerResultEnum {
	/**
	 * 收到消息,未确认
	 */
	SEND(0, "收到消息,未确认"),
	/**
	 * 收到消息,确认消费成功
	 */
	SUCCESS(1, "收到消息，确认消费成功"),
	/**
	 * 收到消息,确认消费失败
	 */
	FAIL(2, "收到消息，确认消费失败");

	private Integer code;
	private String desc;

	ConsumerResultEnum(Integer code, String desc) {

	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
