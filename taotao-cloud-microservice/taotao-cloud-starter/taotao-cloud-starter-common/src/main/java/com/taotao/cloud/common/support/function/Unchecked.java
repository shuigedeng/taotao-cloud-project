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

package com.taotao.cloud.common.support.function;


import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Lambda 受检异常处理
 *
 * <p>
 * https://segmentfault.com/a/1190000007832130 https://github.com/jOOQ/jOOL
 * </p>
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class Unchecked {

	/**
	 * 构造受检的 function
	 *
	 * @param function CheckedFunction
	 * @param <T>      泛型
	 * @return Function
	 */
	public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
		Objects.requireNonNull(function);
		return t -> {
			try {
				return function.apply(t);
			} catch (Throwable e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

	/**
	 * 构造受检的 supplier
	 *
	 * @param consumer CheckedConsumer
	 * @param <T>      泛型
	 * @return Consumer
	 */
	public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
		Objects.requireNonNull(consumer);
		return t -> {
			try {
				consumer.accept(t);
			} catch (Throwable e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

	/**
	 * 构造受检的 supplier
	 *
	 * @param supplier CheckedSupplier
	 * @param <T>      泛型
	 * @return Supplier
	 */
	public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier) {
		Objects.requireNonNull(supplier);
		return () -> {
			try {
				return supplier.get();
			} catch (Throwable e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

	/**
	 * 构造受检的 predicate
	 *
	 * @param predicate CheckedPredicate
	 * @param <T>       泛型
	 * @return Supplier
	 */
	public static <T> Predicate<T> predicate(CheckedPredicate<T> predicate) {
		Objects.requireNonNull(predicate);
		return (t) -> {
			try {
				return predicate.test(t);
			} catch (Throwable e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

	/**
	 * 构造受检的 runnable
	 *
	 * @param runnable CheckedRunnable
	 * @return Runnable
	 */
	public static Runnable runnable(CheckedRunnable runnable) {
		Objects.requireNonNull(runnable);
		return () -> {
			try {
				runnable.run();
			} catch (Throwable e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

	/**
	 * 构造受检的 callable
	 *
	 * @param callable CheckedCallable
	 * @param <T>      泛型
	 * @return Callable
	 */
	public static <T> Callable<T> callable(CheckedCallable<T> callable) {
		Objects.requireNonNull(callable);
		return () -> {
			try {
				return callable.call();
			} catch (Throwable e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

	/**
	 * 构造受检的 comparator
	 *
	 * @param comparator CheckedComparator
	 * @param <T>        泛型
	 * @return Comparator
	 */
	public static <T> Comparator<T> comparator(CheckedComparator<T> comparator) {
		Objects.requireNonNull(comparator);
		return (T o1, T o2) -> {
			try {
				return comparator.compare(o1, o2);
			} catch (Throwable e) {
				throw ExceptionUtil.unchecked(e);
			}
		};
	}

}
