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

import java.security.MessageDigest;

/**
 * MD5工具类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:52
 */
public abstract class MD5Util {

	/**
	 * 获取指定字符串的md5值
	 *
	 * @param dataStr 明文
	 * @return String
	 */
	public static String md5(String dataStr) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(dataStr.getBytes("UTF8"));
			byte[] s = m.digest();
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < s.length; i++) {
				result.append(Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6));
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取指定字符串的md5值, md5(str+salt)
	 *
	 * @param dataStr 明文
	 * @return String
	 */
	public static String md5WithSalt(String dataStr, String salt) {
		return md5(dataStr + salt);
	}

}
