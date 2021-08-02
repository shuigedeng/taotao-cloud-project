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
package com.taotao.cloud.web.enums;


/**
 * 是或否的枚举，一般用在数据库字段，例如del_flag字段，char(1)，填写Y或N
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/07/30 17:59
 */
public enum YesOrNotEnum {

	/**
	 * 是
	 */
	Y("Y", "是"),

	/**
	 * 否
	 */
	N("N", "否");

	private final String code;

	private final String message;

	YesOrNotEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}


	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
