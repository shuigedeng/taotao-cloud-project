package com.taotao.cloud.core.sensitive.sensitive.core.api;


import com.google.common.collect.Lists;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.core.sensitive.sensitive.core.bs.SensitiveBs;
import java.util.Collection;
import java.util.List;

/**
 * 脱敏工具类
 */
public final class SensitiveUtil {

	private SensitiveUtil() {
	}

	/**
	 * 脱敏对象
	 * <p>
	 * 每次都创建一个新的对象，避免线程问题 可以使用 {@link ThreadLocal} 简单优化。
	 *
	 * @param object 原始对象
	 * @param <T>    泛型
	 * @return 脱敏后的对象
	 */
	public static <T> T desCopy(T object) {
		return SensitiveBs.newInstance().desCopy(object);
	}

	/**
	 * 返回脱敏后的对象 json null 对象，返回字符串 "null"
	 *
	 * @param object 对象
	 * @return 结果 json
	 */
	public static String desJson(Object object) {
		return SensitiveBs.newInstance().desJson(object);
	}

	/**
	 * 脱敏对象集合
	 *
	 * @param collection 原始集合
	 * @param <T>        泛型
	 * @return 脱敏后的对象集合，
	 */
	public static <T> List<T> desCopyCollection(Collection<T> collection) {
		if (CollectionUtils.isEmpty(collection)) {
			return Lists.newArrayList();
		}

		List<T> resultList = Lists.newArrayList();
		for (T item : collection) {
			T sensitive = desCopy(item);
			resultList.add(sensitive);
		}
		return resultList;
	}

	/**
	 * 脱敏对象 JSON 集合
	 *
	 * @param collection 原始集合
	 * @return 脱敏后的对象集合
	 * @see #desJson(Object) 单个对象的脱敏 json
	 */
	public static List<String> desJsonCollection(Collection<?> collection) {
		if (CollectionUtils.isEmpty(collection)) {
			return Lists.newArrayList();
		}

		List<String> resultList = Lists.newArrayList();
		for (Object item : collection) {
			String sensitiveJson = desJson(item);
			resultList.add(sensitiveJson);
		}
		return resultList;
	}

}
