/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.utils;

import org.springframework.lang.Nullable;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * 随机类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:48
 */
public final class RandomUtils {

	/**
	 * 默认随机字符集合
	 */
	public static final char[] CHAR_LIST = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
		'l', 'm', 'n', 'o',
		'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
		'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
		'2', '3', '4',
		'5', '6', '7', '8', '9'};

	private RandomUtils() {
	}

	/**
	 * 随机字符串，取值范围(a-zA-Z0-9)
	 *
	 * @param length 字符串长度
	 * @return 随机字符串
	 */
	public static String nextString(final int length) {
		return nextString(length, CHAR_LIST);
	}

	/**
	 * 自定义取值范围的随机字符串
	 *
	 * @param length 字符串长度
	 * @param chars  取值范围
	 * @return 随机字符串
	 */
	public static String nextString(final int length, @Nullable final char[] chars) {
		if (length <= 0) {
			return "";
		}

		char[] nowChars = chars;

		if (nowChars == null || nowChars.length == 0) {
			nowChars = CHAR_LIST;
		}

		char[] list = new char[length];

		ThreadLocalRandom random = current();

		for (int i = 0; i < list.length; i++) {
			list[i] = nowChars[random.nextInt(nowChars.length)];
		}

		return new String(list);
	}
}
