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
 * 用户登录类型
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:22:27
 */
public enum LoginTypeEnum implements BaseEnum {
	/**
	 * 用户密码登录
	 */
	LOGIN_USER_PASSWORD(1, "user_password", "用户密码登录"),
	/**
	 * 短信密码登录
	 */
	LOGIN_SMS(2, "sms", "短信密码登录"),
	/**
	 * qq登录
	 */
	LOGIN_QQ(3, "qq", "qq登录"),
	/**
	 * 微信登录
	 */
	LOGIN_WEIXIN(4, "weixin", "微信登录"),
	/**
	 * gitee登录
	 */
	LOGIN_GITEE(5, "gitee", "gitee登录"),
	/**
	 * github登录
	 */
	LOGIN_GITHUB(6, "github", "github登录");

	private final int code;
	private final String type;
	private final String desc;

	LoginTypeEnum(int code, String type, String description) {
		this.code = code;
		this.type = type;
		this.desc = description;
	}

	public String getType() {
		return type;
	}

	@Override
	public String getDesc() {
		return desc;
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
	public int getCode() {
		return this.code;
	}
}
