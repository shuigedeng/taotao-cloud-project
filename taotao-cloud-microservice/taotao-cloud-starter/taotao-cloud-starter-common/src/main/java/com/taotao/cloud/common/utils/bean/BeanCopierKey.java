/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.common.utils.bean;

import java.util.Objects;

/**
 * copy key
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class BeanCopierKey {

	private final Class<?> source;
	private final Class<?> target;
	private final boolean useConverter;
	private final boolean nonNull;


	public BeanCopierKey(Class<?> source, Class<?> target, boolean useConverter,
		boolean nonNull) {
		this.source = source;
		this.target = target;
		this.useConverter = useConverter;
		this.nonNull = nonNull;
	}

	public Class<?> getSource() {
		return source;
	}

	public Class<?> getTarget() {
		return target;
	}

	public boolean isUseConverter() {
		return useConverter;
	}

	public boolean isNonNull() {
		return nonNull;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BeanCopierKey that = (BeanCopierKey) o;
		return useConverter == that.useConverter && nonNull == that.nonNull && Objects.equals(
			source, that.source) && Objects.equals(target, that.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target, useConverter, nonNull);
	}
}
