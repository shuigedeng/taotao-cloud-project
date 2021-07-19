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

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 报警级别
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:43
 **/
public enum LevelEnum {
	//报警级别
	HIGN(3, "极其严重"),
	MIDDLE(2, "严重"),
	LOW(1, "一般");

	private int level = 1;
	private String description;

	public String getDescription() {
		return description;
	}

	public int getLevel() {
		return level;
	}

	LevelEnum(int level, String description) {
		this.description = description;
		this.level = level;
	}

	private static final Map<Integer, LevelEnum> valueLookup = new ConcurrentHashMap<>(
		values().length);

	static {
		for (LevelEnum type : EnumSet.allOf(LevelEnum.class)) {
			valueLookup.put(type.level, type);
		}
	}

	public static LevelEnum resolve(Integer code) {

		return (code != null ? valueLookup.get(code) : null);
	}

	public static String resolveName(Integer code) {
		LevelEnum mode = resolve(code);
		return mode == null ? "" : mode.getDescription();
	}
}
