package com.taotao.cloud.common.utils.func;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * func跑龙套
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-26 14:37:25
 */
public class FuncUtil {

	/**
	 * 谓词
	 *
	 * @param t            t
	 * @param predicate    谓词
	 * @param defaultValue 默认值
	 * @return {@link T }
	 * @since 2022-05-26 14:39:29
	 */
	public static <T> T predicate(T t, Predicate<T> predicate, T defaultValue) {
		return predicate.test(t) ? t : defaultValue;
	}

	/**
	 * 谓词
	 *
	 * @param t            t
	 * @param supplier     供应商
	 * @param defaultValue 默认值
	 * @return {@link T }
	 * @since 2022-05-26 15:06:52
	 */
	public static <T> T predicate(T t, Supplier<Boolean> supplier, T defaultValue) {
		return supplier.get() ? t : defaultValue;
	}

}
