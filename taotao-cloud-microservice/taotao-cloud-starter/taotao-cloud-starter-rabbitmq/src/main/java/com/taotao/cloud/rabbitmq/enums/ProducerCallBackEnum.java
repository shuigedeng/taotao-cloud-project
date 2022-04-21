/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
 * ProducerCallBackEnum
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/9/29 15:56
 */
public enum ProducerCallBackEnum {

	SEND(0, "消息发送,无反馈"),
	SUCCESS(1, "消息发送到broker成功"),
	FAIL(2, "消息发送到broker失败");

	private Integer code;
	private String desc;

	ProducerCallBackEnum(Integer code, String desc) {

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
