package com.taotao.cloud.common.utils.common;


import com.taotao.cloud.common.support.tuple.impl.Pair;
import com.taotao.cloud.common.utils.lang.StringUtils;

/**
 * 占位符工具类
 */
public final class PlaceholderUtils {

	private PlaceholderUtils() {
	}

	/**
	 * 默认前置
	 */
	public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

	/**
	 * 默认后置
	 */
	public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

	/**
	 * 默认分隔符
	 */
	public static final String DEFAULT_VALUE_SEPARATOR = ":";

	/**
	 * 转换占位符
	 *
	 * @param rawPlaceholder 原始的占位符
	 * @return 结果
	 */
	public static Pair<String, String> parsePlaceholder(String rawPlaceholder) {
		String trim = StringUtils.trim(rawPlaceholder);
		if (StringUtils.isEmpty(trim)) {
			return Pair.of(null, null);
		}

		// 判断是否为占位符
		if (!trim.startsWith(DEFAULT_PLACEHOLDER_PREFIX) || !trim.endsWith(
			DEFAULT_PLACEHOLDER_SUFFIX)) {
			return Pair.of(null, null);
		}

		// 移除前缀
		String removePrefix = trim.substring(DEFAULT_PLACEHOLDER_PREFIX.length());

		// 移除后缀
		String removeSuffix = removePrefix.substring(0,
			removePrefix.length() - DEFAULT_PLACEHOLDER_SUFFIX.length());

		// 判断是否存在分割符
		int splitIndex = removeSuffix.indexOf(DEFAULT_VALUE_SEPARATOR);

		if (splitIndex < 0) {
			return Pair.of(removeSuffix, null);
		}

		String key = removeSuffix.substring(0, splitIndex);
		String defaultValue = removeSuffix.substring(splitIndex + 1);
		return Pair.of(key, defaultValue);
	}

	//public static void main(String[] args) {
	//	System.out.println(parsePlaceholder(null));
	//	System.out.println(parsePlaceholder("asdfsa"));
	//	System.out.println(parsePlaceholder("${name}"));
	//	System.out.println(parsePlaceholder("${name:ruo}"));
	//}

}
