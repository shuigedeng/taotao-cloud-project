/*
 * Copyright 2018-2022 the original author or authors.
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
package com.taotao.cloud.sms.channel.netease;

import java.security.MessageDigest;

/**
 * @author shuigedeng
 */
public class CheckSumBuilder {

	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
		'b', 'c', 'd',
		'e', 'f'};

	/**
	 * 计算并获取CheckSum
	 *
	 * @param appSecret 密钥
	 * @param nonce     随机数
	 * @param curTime   当前时间
	 * @return checkSum
	 */
	public static String getCheckSum(String appSecret, String nonce, String curTime) {
		return encode("sha1", appSecret + nonce + curTime);
	}

	/**
	 * 计算并获取md5值
	 *
	 * @param requestBody 请求内容
	 * @return md5值
	 */
	public static String getMd5(String requestBody) {
		return encode("md5", requestBody);
	}

	private static String encode(String algorithm, String value) {
		if (value == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(value.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (byte aByte : bytes) {
			buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[aByte & 0x0f]);
		}
		return buf.toString();
	}
}
