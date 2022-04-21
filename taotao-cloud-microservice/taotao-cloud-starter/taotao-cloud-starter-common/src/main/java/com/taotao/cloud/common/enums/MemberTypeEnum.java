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
 * 会员用户类型
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:32:32
 */
public enum MemberTypeEnum implements BaseEnum {
	/**
	 * 个人用户
	 */
	PERSONAL(1, "personal", "个人用户"),
	/**
	 * 企业用户
	 */
	ENTERPRISE(2, "enterprise", "企业用户");

	private final int code;
	private final String value;
	private final String desc;

	MemberTypeEnum(int code, String value, String desc) {
		this.code = code;
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
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
		for (MemberTypeEnum result : MemberTypeEnum.values()) {
			if (result.getCode() == code) {
				return result.name().toLowerCase();
			}
		}
		return null;
	}
}
