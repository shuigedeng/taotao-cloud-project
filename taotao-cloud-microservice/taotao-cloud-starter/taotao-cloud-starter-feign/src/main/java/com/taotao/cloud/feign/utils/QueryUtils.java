package com.taotao.cloud.feign.utils;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * 解析request的query参数工具
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-08 10:44:31
 */
public class QueryUtils {

	/**
	 * 通过query字符串得到参数的map
	 *
	 * @param queryString ?后的字符
	 * @return {@link Map }<{@link String }, {@link String }>
	 * @since 2022-06-08 10:44:31
	 */
	public static Map<String, String> getQueryMap(String queryString) {
		if (StringUtils.isNotBlank(queryString)) {
			return Arrays.stream(queryString.split("&")).map(item -> item.split("="))
				.collect(Collectors.toMap(key -> key[0],
					value -> value.length > 1 && StringUtils.isNotBlank(value[1]) ? value[1] : ""));
		}
		return Collections.emptyMap();
	}

	/**
	 * 通过url获取参数map
	 *
	 * @param uri
	 */
	public static Map<String, String> getQueryMap(URI uri) {
		if (Objects.nonNull(uri)) {
			return getQueryMap(uri.getQuery());
		}
		return Collections.emptyMap();
	}
}
