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
package com.taotao.cloud.common.base;


import java.util.Objects;

/**
 * 模拟out和ref语法
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/6/22 17:10
 **/
public class Ref<T> {

	private volatile T data;

	public Ref(T data) {
		this.data = data;
	}

	public boolean isNull() {
		return data == null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Ref<?> ref = (Ref<?>) o;
		return Objects.equals(data, ref.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(data);
	}

	@Override
	public String toString() {
		return "Ref{" +
			"data=" + data +
			'}';
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
