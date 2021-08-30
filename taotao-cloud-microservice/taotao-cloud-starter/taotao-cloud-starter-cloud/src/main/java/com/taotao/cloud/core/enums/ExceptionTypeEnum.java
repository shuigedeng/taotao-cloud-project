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
package com.taotao.cloud.core.enums;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExceptionTypeEnum 异常级别
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:36
 */
public enum ExceptionTypeEnum {
	BE(1, "后端"),
	FE(2, "前端"),
	BUSINESS(3, "业务"),
	OPERATION(4, "运维"),
	DBA(5, "DBA"),
	ELSE(6, "其他");

	private int code;
	private String name;

	ExceptionTypeEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	private static final Map<Integer, ExceptionTypeEnum> valueLookup = new ConcurrentHashMap<>(
		values().length);

	static {
		for (ExceptionTypeEnum type : EnumSet.allOf(ExceptionTypeEnum.class)) {
			valueLookup.put(type.code, type);
		}
	}

	public static ExceptionTypeEnum resolve(Integer code) {

		return (code != null ? valueLookup.get(code) : null);
	}

	public static String resolveName(Integer code) {
		ExceptionTypeEnum mode = resolve(code);
		return mode == null ? "" : mode.getName();
	}
}
