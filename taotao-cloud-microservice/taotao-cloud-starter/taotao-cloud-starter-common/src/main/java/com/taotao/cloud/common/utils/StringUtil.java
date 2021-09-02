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

/**
 * StringUtil
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/2 16:42
 */
public class StringUtil {

	private StringUtil() {
	}

	/**
	 * nullToEmpty
	 *
	 * @param str str
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:15
	 */
	public static String nullToEmpty(Object str) {
		return str != null ? str.toString() : "";
	}

	/**
	 * isEmpty
	 *
	 * @param str str
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:23
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	/**
	 * 部分字符串获取
	 *
	 * @param str    字符串
	 * @param maxlen 最大长度
	 * @return {@link String } 字符串
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:32
	 */
	public static String subString2(String str, int maxlen) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		if (str.length() <= maxlen) {
			return str;
		}
		return str.substring(0, maxlen);
	}

	/**
	 * 部分字符串获取 超出部分末尾...
	 *
	 * @param str    字符串
	 * @param maxlen 最大长度
	 * @return {@link String } 字符串
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:46
	 */
	public static String subString3(String str, int maxlen) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		if (str.length() <= maxlen) {
			return str;
		}
		return str.substring(0, maxlen) + "...";
	}

	/**
	 * Check that the given {@code String} is neither {@code null} nor of length 0.
	 * <p>Note: this method returns {@code true} for a {@code String} that
	 * purely consists of whitespace.
	 *
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null} and has length
	 * @see #hasLength(CharSequence)
	 * @see #hasText(String)
	 */
	public static boolean hasLength(String str) {
		return (str != null && !str.isEmpty());
	}

	/**
	 * Check that the given {@code CharSequence} is neither {@code null} nor of length 0.
	 * <p>Note: this method returns {@code true} for a {@code CharSequence}
	 * that purely consists of whitespace.
	 * <p><pre class="code">
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 *
	 * @param str the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
	 * @see #hasLength(String)
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check whether the given {@code CharSequence} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code CharSequence} is not {@code null}, its length is greater than 0, and it contains at
	 * least one non-whitespace character.
	 * <p><pre class="code">
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 *
	 * @param str the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater
	 * than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given {@code String} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code String} is not {@code null}, its length is greater than 0, and it contains at least
	 * one non-whitespace character.
	 *
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null}, its length is greater than 0,
	 * and it does not contain whitespace only
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	/**
	 * trimToNull
	 *
	 * @param nextLine nextLine
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:58
	 */
	public static String trimToNull(String nextLine) {
		return nextLine.trim();
	}
}
