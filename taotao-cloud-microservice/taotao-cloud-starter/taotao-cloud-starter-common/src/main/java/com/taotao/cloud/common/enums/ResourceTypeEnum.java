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

/**
 * 目录类型
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/2 15:37
 */
public enum ResourceTypeEnum implements BaseEnum {
	/**
	 * 一级(左侧)菜单
	 */
	LEFT_MENU((byte) 1, "一级(左侧)菜单"),
	/**
	 * 二级(顶部)菜单
	 */
	TOP_MENU((byte) 2, "二级(顶部)菜单"),
	/**
	 * 按钮
	 */
	BUTTON((byte) 3, "按钮");

	private final Byte value;
	private final String description;

	ResourceTypeEnum(Byte value, String description) {
		this.value = value;
		this.description = description;
	}

	public Byte getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String getNameByCode(int code) {
		for (ResourceTypeEnum result : ResourceTypeEnum.values()) {
			if (result.getCode() == code) {
				return result.name().toLowerCase();
			}
		}
		return null;
	}

	@Override
	public Integer getCode() {
		return (int) value;
	}
}
