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
package com.taotao.cloud.captcha.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * RandomUtils
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:52
 */
public class RandomUtils {

	/**
	 * 生成UUID
	 *
	 * @return
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		return uuid;
	}

	/**
	 * 获取指定文字的随机中文
	 *
	 * @return
	 */
	public static String getRandomHan(String hanZi) {
		String ch = hanZi.charAt(new Random().nextInt(hanZi.length())) + "";
		return ch;
	}

	public static int getRandomInt(int bound) {
		return ThreadLocalRandom.current().nextInt(bound);
	}

	/**
	 * 获取随机中文
	 *
	 * @return
	 */
	public static String getRandomHan() {
		String str = "";
		int highCode;
		int lowCode;

		Random random = new Random();

		highCode = (176 + Math.abs(random.nextInt(39))); //B0 + 0~39(16~55) 一级汉字所占区
		lowCode = (161 + Math.abs(random.nextInt(93))); //A1 + 0~93 每区有94个汉字

		byte[] b = new byte[2];
		b[0] = (Integer.valueOf(highCode)).byteValue();
		b[1] = (Integer.valueOf(lowCode)).byteValue();

		try {
			str = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 随机范围内数字
	 *
	 * @param startNum
	 * @param endNum
	 * @return
	 */
	public static Integer getRandomInt(int startNum, int endNum) {
		return ThreadLocalRandom.current().nextInt(endNum - startNum) + startNum;
	}

	/**
	 * 获取随机字符串
	 *
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

}
