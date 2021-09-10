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
package com.taotao.cloud.health.enums;

/**
 * 报警级别
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 16:06:42
 */
public enum WarnTypeEnum {
	ERROR(2, "错误"),
	WARN(1, "告警"),
	INFO(0, "通知");

	private int level;
	private String description;

	public String getDescription() {
		return description;
	}

	public int getLevel() {
		return level;
	}

	WarnTypeEnum(int level, String description) {
		this.description = description;
		this.level = level;
	}
}
