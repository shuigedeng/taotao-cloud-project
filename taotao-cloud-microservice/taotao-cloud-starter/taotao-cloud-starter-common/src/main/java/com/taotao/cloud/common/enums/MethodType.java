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
 * 方法类型
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:31:49
 */
public enum MethodType {

	/**
	 * 方法类型 GET PUT POST DELETE OPTIONS
	 */
	GET(false),
	PUT(true),
	POST(true),
	DELETE(false),
	HEAD(false),
	OPTIONS(false);

	private final boolean hasContent;

	MethodType(boolean hasContent) {
		this.hasContent = hasContent;
	}

	public boolean isHasContent() {
		return hasContent;
	}

	@Override
	public String toString() {
		return "MethodType{" +
			"hasContent=" + hasContent +
			"} " + super.toString();
	}
}
