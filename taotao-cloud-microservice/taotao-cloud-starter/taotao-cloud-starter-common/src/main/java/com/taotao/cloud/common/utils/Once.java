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

package com.taotao.cloud.common.utils;

import org.springframework.lang.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 加载一次
 *
  * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class Once {
	private final AtomicBoolean value;

	public Once() {
		this.value = new AtomicBoolean(false);
	}

	/**
	 * 是否可以执行
	 *
	 * @return 是否可以执行
	 */
	public boolean canRun() {
		return value.compareAndSet(false, true);
	}

	/**
	 * 执行函数
	 *
	 * @param consumer Consumer
	 * @param argument 参数
	 * @param <T>      泛型
	 */
	public <T> void run(Consumer<T> consumer, T argument) {
		if (canRun()) {
			consumer.accept(argument);
		}
	}

	/**
	 * 执行函数
	 *
	 * @param consumer BiConsumer
	 * @param arg1     参数1
	 * @param arg2     参数1
	 * @param <T>      泛型
	 * @param <U>      泛型
	 */
	public <T, U> void run(BiConsumer<T, U> consumer, T arg1, U arg2) {
		if (canRun()) {
			consumer.accept(arg1, arg2);
		}
	}

	/**
	 * 执行函数
	 *
	 * @param function Function
	 * @param argument 参数
	 * @param <T>      泛型
	 * @param <R>      泛型
	 * @return 返回值，不可执行返回 null
	 */
	@Nullable
	public <T, R> R run(Function<T, R> function, T argument) {
		if (canRun()) {
			return function.apply(argument);
		}
		return null;
	}

}
