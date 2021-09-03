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
package com.taotao.cloud.web.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

/**
 * CollectionUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:23:00
 */
public class CollectionUtil {

	/**
	 * 将以“,”分隔的字符串转成为Collection
	 *
	 * @param str 字符串
	 * @return {@link java.util.Collection }
	 * @author shuigedeng
	 * @since 2021-09-02 22:23:13
	 */
	public static Collection<? extends Serializable> stringToCollection(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		} else {
			String[] strArray = str.split(",");
			final Long[] longs = new Long[strArray.length];
			for (int i = 0; i < strArray.length; i++) {
				longs[i] = strToLong(strArray[i], 0L);
			}
			return arrayToCollection(longs);
		}
	}

	/**
	 * 将字组转换成Collection
	 *
	 * @param longArray Long数组
	 * @return {@link java.util.Collection }
	 * @author shuigedeng
	 * @since 2021-09-02 22:23:21
	 */
	public static Collection<? extends Serializable> arrayToCollection(Long[] longArray) {
		return Arrays.asList(longArray);
	}


	/**
	 * 字符串转换为long
	 *
	 * @param str          str
	 * @param defaultValue defaultValue
	 * @return long
	 * @author shuigedeng
	 * @since 2021-09-02 22:23:30
	 */
	public static long strToLong(@Nullable final String str, final long defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(str);
		} catch (final NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * 字符串转换为long
	 *
	 * @param str          str
	 * @param defaultValue defaultValue
	 * @return long
	 * @author shuigedeng
	 * @since 2021-09-02 22:23:41
	 */
	public static long objectToLong(@Nullable final Object str, final long defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(String.valueOf(str));
		} catch (final NumberFormatException nfe) {
			return defaultValue;
		}
	}
}
