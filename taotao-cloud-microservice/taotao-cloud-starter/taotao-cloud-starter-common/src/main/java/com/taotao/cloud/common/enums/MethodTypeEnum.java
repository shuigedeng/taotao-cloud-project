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
package com.taotao.cloud.common.enums;

/**
 * 方法类型
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:31:49
 */
public enum MethodTypeEnum implements BaseEnum {

	/**
	 * 方法类型 GET PUT POST DELETE OPTIONS
	 */
	GET(1, "GET"),
	PUT(2, "PUT"),
	POST(3, "POST"),
	DELETE(4, "DELETE"),
	HEAD(5, "HEAD"),
	OPTIONS(6, "OPTIONS");

	private final int code;
	private final String desc;

	MethodTypeEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getNameByCode(int code) {
		for (MethodTypeEnum result : MethodTypeEnum.values()) {
			if (result.getCode() == code) {
				return result.name().toLowerCase();
			}
		}
		return null;
	}
}
