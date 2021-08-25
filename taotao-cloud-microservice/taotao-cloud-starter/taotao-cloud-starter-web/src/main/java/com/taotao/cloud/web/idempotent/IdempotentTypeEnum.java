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
package com.taotao.cloud.web.idempotent;

/**
 * 幂等枚举类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:42
 */
public enum IdempotentTypeEnum {
	/**
	 * 0+1
	 */
	ALL(0, "ALL"),
	/**
	 * ruid 是针对每一次请求的
	 */
	RID(1, "RID"),
	/**
	 * key+val 是针对相同参数请求
	 */
	KEY(2, "KEY");

	private final Integer index;
	private final String title;

	IdempotentTypeEnum(Integer index, String title) {
		this.index = index;
		this.title = title;
	}

	public Integer getIndex() {
		return index;
	}

	public String getTitle() {
		return title;
	}
}
