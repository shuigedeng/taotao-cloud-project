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
package com.taotao.cloud.captcha.model;

import java.text.MessageFormat;

/**
 * 返回应答码
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:48
 */
public enum CaptchaCodeEnum {

	SUCCESS("0000", "成功"),
	ERROR("0001", "操作失败"),
	EXCEPTION("9999", "服务器内部异常"),

	BLANK_ERROR("0011", "{0}不能为空"),
	NULL_ERROR("0011", "{0}不能为空"),
	NOT_NULL_ERROR("0012", "{0}必须为空"),
	NOT_EXIST_ERROR("0013", "{0}数据库中不存在"),
	EXIST_ERROR("0014", "{0}数据库中已存在"),
	PARAM_TYPE_ERROR("0015", "{0}类型错误"),
	PARAM_FORMAT_ERROR("0016", "{0}格式错误"),

	API_CAPTCHA_INVALID("6110", "验证码已失效，请重新获取"),
	API_CAPTCHA_COORDINATE_ERROR("6111", "验证失败"),
	API_CAPTCHA_ERROR("6112", "获取验证码失败,请联系管理员"),
	API_CAPTCHA_BASEMAP_NULL("6113", "底图未初始化成功，请检查路径"),

	API_REQ_LIMIT_GET_ERROR("6201", "get接口请求次数超限，请稍后再试!"),
	API_REQ_INVALID("6206", "无效请求，请重新获取验证码"),
	API_REQ_LOCK_GET_ERROR("6202", "接口验证失败数过多，请稍后再试"),
	API_REQ_LIMIT_CHECK_ERROR("6204", "check接口请求次数超限，请稍后再试!"),
	API_REQ_LIMIT_VERIFY_ERROR("6205", "verify请求次数超限!"),
	;
	private String code;
	private String desc;

	CaptchaCodeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public String getName() {
		return this.name();
	}

	public String parseError(Object... fieldNames) {
		return MessageFormat.format(this.desc, fieldNames);
	}

}
