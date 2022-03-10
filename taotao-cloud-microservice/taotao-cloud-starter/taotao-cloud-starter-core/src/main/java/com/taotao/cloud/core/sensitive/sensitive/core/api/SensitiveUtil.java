package com.taotao.cloud.core.sensitive.sensitive.core.api;


import com.taotao.cloud.core.heaven.util.guava.Guavas;
import com.taotao.cloud.core.heaven.util.util.CollectionUtil;
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
	 * @since 0.0.4 以前用的是单例。建议使用 spring 等容器管理 ISensitive 实现。
	 */
	public static <T> T desCopy(T object) {
		return SensitiveBs.newInstance().desCopy(object);
	}

	/**
	 * 返回脱敏后的对象 json null 对象，返回字符串 "null"
	 *
	 * @param object 对象
	 * @return 结果 json
	 * @since 0.0.6
	 */
	public static String desJson(Object object) {
		return SensitiveBs.newInstance().desJson(object);
	}

	/**
	 * 脱敏对象集合
	 *
	 * @param collection 原始集合
	 * @param <T>        泛型
	 * @return 脱敏后的对象集合，如果原始对象为 {@link com.github.houbb.heaven.util.util.CollectionUtil#isEmpty(Collection)}，则返回空列表。
	 */
	public static <T> List<T> desCopyCollection(Collection<T> collection) {
		if (CollectionUtil.isEmpty(collection)) {
			return Guavas.newArrayList();
		}

		List<T> resultList = Guavas.newArrayList(collection.size());
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
	 * @return 脱敏后的对象集合，如果原始对象为 {@link com.github.houbb.heaven.util.util.CollectionUtil#isEmpty(Collection)}，则返回空列表。
	 * @see #desJson(Object) 单个对象的脱敏 json
	 * @since 0.0.7
	 */
	public static List<String> desJsonCollection(Collection<?> collection) {
		if (CollectionUtil.isEmpty(collection)) {
			return Guavas.newArrayList();
		}

		List<String> resultList = Guavas.newArrayList(collection.size());
		for (Object item : collection) {
			String sensitiveJson = desJson(item);
			resultList.add(sensitiveJson);
		}
		return resultList;
	}

}
