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
 * bean map key，提高性能
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class BeanMapKey {

	private final Class<?> type;
	private final int require;

	public BeanMapKey(Class<?> type, int require) {
		this.type = type;
		this.require = require;
	}

	public Class<?> getType() {
		return type;
	}

	public int getRequire() {
		return require;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BeanMapKey that = (BeanMapKey) o;
		return require == that.require && Objects.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, require);
	}
}
