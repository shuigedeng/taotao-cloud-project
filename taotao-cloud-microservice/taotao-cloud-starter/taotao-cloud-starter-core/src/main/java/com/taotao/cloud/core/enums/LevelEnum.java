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
package com.taotao.cloud.core.enums;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LevelEnum 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:15:18
 */
public enum LevelEnum {
	//报警级别
	HIGN(3, "极其严重"),
	MIDDLE(2, "严重"),
	LOW(1, "一般");

	private final int level;
	private final String description;

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
