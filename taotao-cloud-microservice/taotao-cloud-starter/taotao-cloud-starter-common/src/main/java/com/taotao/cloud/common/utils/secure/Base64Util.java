/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.common.utils.secure;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.Charsets;

/**
 * Base64工具类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 17:52:02
 */
public class Base64Util extends org.springframework.util.Base64Utils {

	/**
	 * 编码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encode(String value) {
		return Base64Util.encode(value, StandardCharsets.UTF_8);
	}

	/**
	 * 编码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encode(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(Base64Util.encode(val), charset);
	}

	/**
	 * 编码URL安全
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value) {
		return Base64Util.encodeUrlSafe(value, StandardCharsets.UTF_8);
	}

	/**
	 * 编码URL安全
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(Base64Util.encodeUrlSafe(val), charset);
	}

	/**
	 * 解码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decode(String value) {
		return Base64Util.decode(value, StandardCharsets.UTF_8);
	}

	/**
	 * 解码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decode(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = Base64Util.decode(val);
		return new String(decodedValue, charset);
	}

	/**
	 * 解码URL安全
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value) {
		return Base64Util.decodeUrlSafe(value, StandardCharsets.UTF_8);
	}

	/**
	 * 解码URL安全
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = Base64Util.decodeUrlSafe(val);
		return new String(decodedValue, charset);
	}

	private Base64Util() {
	}

	///**
	// * Base64 编码
	// *
	// * @param text 明文
	// * @return 密文
	// * @author shuigedeng
	// * @since 2021-09-02 17:51:28
	// */
	//public static String encode(String text) {
	//	if (StringUtils.isBlank(text)) {
	//		return null;
	//	}
	//	return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
	//}
	//
	///**
	// * Base64 解码
	// *
	// * @param ciphertext 密文
	// * @return 明文
	// * @author shuigedeng
	// * @since 2021-09-02 17:51:28
	// */
	//public static String decode(String ciphertext) {
	//	if (StringUtils.isBlank(ciphertext)) {
	//		return null;
	//	}
	//	final byte[] decode = Base64.getDecoder().decode(ciphertext);
	//	return new String(decode, StandardCharsets.UTF_8);
	//}
}
