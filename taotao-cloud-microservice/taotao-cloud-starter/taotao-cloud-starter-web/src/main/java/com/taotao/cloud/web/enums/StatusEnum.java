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
package com.taotao.cloud.web.enums;

/**
 * StatusEnum
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:14:53
 */
public enum StatusEnum {

	/**
	 * 启用
	 */
	ENABLE("enable", "启用"),

	/**
	 * 禁用
	 */
	DISABLE("disable", "禁用");

	private final String code;

	private final String message;

	StatusEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 根据code获取枚举
	 */
	public static StatusEnum codeToEnum(String code) {
		if (null != code) {
			for (StatusEnum e : StatusEnum.values()) {
				if (e.getCode().equals(code)) {
					return e;
				}
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
