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
package com.taotao.cloud.data.mybatis.plus.datascope;

import com.taotao.cloud.common.enums.BaseEnum;
import java.util.stream.Stream;

/**
 * 数据权限类型 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:32
 */
public enum DataScopeTypeEnum implements BaseEnum {

	/**
	 * ALL=5全部
	 */
	ALL(5, "全部"),
	/**
	 * THIS_LEVEL=4本级
	 */
	THIS_LEVEL(4, "本级"),
	/**
	 * THIS_LEVEL_CHILDREN=3本级以及子级
	 */
	THIS_LEVEL_CHILDREN(3, "本级以及子级"),
	/**
	 * CUSTOMIZE=2自定义
	 */
	CUSTOMIZE(2, "自定义"),
	/**
	 * SELF=1个人
	 */
	SELF(1, "个人"),
	;

	private final int val;

	private final String desc;


	public static DataScopeTypeEnum match(String val, DataScopeTypeEnum def) {
		return Stream.of(values()).parallel().filter((item) -> item.name().equalsIgnoreCase(val))
			.findAny().orElse(def);
	}

	public static DataScopeTypeEnum match(Integer val, DataScopeTypeEnum def) {
		return Stream.of(values()).parallel().filter((item) -> val != null && item.getVal() == val)
			.findAny().orElse(def);
	}

	public static DataScopeTypeEnum get(String val) {
		return match(val, null);
	}

	public static DataScopeTypeEnum get(Integer val) {
		return match(val, null);
	}

	public boolean eq(final DataScopeTypeEnum val) {
//		return val != null && eq(val.name());
		return val != null;
	}

	@Override
	public Integer getCode() {
		return this.val;
	}

	@Override
	public String getNameByCode(int code) {
		return null;
	}

	DataScopeTypeEnum(int val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public int getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}
}
