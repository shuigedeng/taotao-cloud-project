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

import java.util.HashMap;

/**
 * EventEnum 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:13:18
 */
public enum EventEnum {
	/**
	 * 属性缓存更新事件
	 */
	PropertyCacheUpdateEvent(new HashMap<String, Object>().getClass(), "属性缓存更新事件");

	/**
	 * 类
	 */
	Class dataClass;

	/**
	 * 描述
	 */
	String desc;


	public Class getDataClass() {
		return dataClass;
	}

	EventEnum(Class dataClass, String desc) {
		this.desc = desc;
		this.dataClass = dataClass;
	}
}
