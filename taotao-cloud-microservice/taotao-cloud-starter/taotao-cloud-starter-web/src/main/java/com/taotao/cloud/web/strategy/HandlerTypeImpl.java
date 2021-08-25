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
package com.taotao.cloud.web.strategy;

import java.lang.annotation.Annotation;

/**
 * 策略模型业务类型注解实现类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:21
 */
public class HandlerTypeImpl implements HandlerType {

	private final String type;
	private final String source;

	HandlerTypeImpl(String type, String source) {
		this.source = source;
		this.type = type;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode += (127 * "type".hashCode()) ^ type.hashCode();
		hashCode += (127 * "source".hashCode()) ^ source.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof HandlerType)) {
			return false;
		}
		HandlerType other = (HandlerType) obj;
		return type.equals(other.type()) && source.equals(other.source());
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return HandlerType.class;
	}

	@Override
	public String type() {
		return type;
	}

	@Override
	public String source() {
		return source;
	}
}
