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
package com.taotao.cloud.common.enums;

import com.taotao.cloud.common.constant.SecurityConstant;

/**
 * 用户登录类型
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 18:11
 */
public enum LoginTypeEnum implements BaseEnum {
	/**
	 * 用户密码登录
	 */
	NORMAL(1, SecurityConstant.NORMAL, "用户密码登录"),
	/**
	 * 短信密码登录
	 */
	SMS(2, SecurityConstant.SMS, "短信密码登录"),
	/**
	 * qq登录
	 */
	LOGIN_QQ(3, SecurityConstant.LOGIN_QQ, "qq登录"),
	/**
	 * 微信登录
	 */
	LOGIN_WEIXIN(4, SecurityConstant.LOGIN_WEIXIN, "微信登录"),
	/**
	 * gitee登录
	 */
	LOGIN_GITEE(5, SecurityConstant.LOGIN_GITEE, "gitee登录"),
	/**
	 * github登录
	 */
	LOGIN_GITHUB(6, SecurityConstant.LOGIN_GITHUB, "github登录");

	private final Integer value;
	private final String type;
	private final String description;

	LoginTypeEnum(Integer value, String type, String description) {
		this.value = value;
		this.type = type;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String getNameByCode(int code) {
		for (LoginTypeEnum result : LoginTypeEnum.values()) {
			if (result.getCode() == code) {
				return result.name().toLowerCase();
			}
		}
		return null;
	}

	@Override
	public Integer getCode() {
		return this.value;
	}
}
