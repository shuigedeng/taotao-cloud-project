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
 * 客户端类型
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-22 10:48:26
 */
public enum ClientTypeEnum {

	/**
	 * "移动端"
	 */
	H5("移动端"),
	/**
	 * "PC端"
	 */
	PC("PC端"),
	/**
	 * "小程序端"
	 */
	WECHAT_MP("小程序端"),
	/**
	 * "移动应用端"
	 */
	APP("移动应用端"),
	/**
	 * "未知"
	 */
	UNKNOWN("未知");

	private final String clientName;

	ClientTypeEnum(String des) {
		this.clientName = des;
	}

	public String clientName() {
		return this.clientName;
	}

	public String value() {
		return this.name();
	}
}
