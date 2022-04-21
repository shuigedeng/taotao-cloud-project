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

package com.taotao.cloud.common.model;

import java.io.Serializable;
import java.util.function.Supplier;
import org.springframework.lang.Nullable;

/**
 * Holder of a value that is computed lazy.
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class Lazy<T> implements Supplier<T>, Serializable {

	@Nullable
	private transient Supplier<? extends T> supplier;
	@Nullable
	private T value;

	/**
	 * Creates new instance of Lazy.
	 *
	 * @param supplier Supplier
	 * @param <T>      泛型标记
	 * @return Lazy
	 */
	public static <T> Lazy<T> of(final Supplier<T> supplier) {
		return new Lazy<>(supplier);
	}

	private Lazy(final Supplier<T> supplier) {
		this.supplier = supplier;
	}

	/**
	 * Returns the value. Value will be computed on first call.
	 *
	 * @return lazy value
	 */
	@Nullable
	@Override
	public T get() {
		return (supplier == null) ? value : computeValue();
	}

	@Nullable
	private synchronized T computeValue() {
		final Supplier<? extends T> s = supplier;
		if (s != null) {
			value = s.get();
			supplier = null;
		}
		return value;
	}

}
