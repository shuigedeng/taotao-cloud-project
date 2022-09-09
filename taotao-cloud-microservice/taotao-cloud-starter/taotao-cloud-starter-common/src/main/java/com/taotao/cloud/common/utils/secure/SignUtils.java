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

import com.taotao.cloud.common.utils.lang.StringUtils;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.apache.shenyu.common.utils.Md5Utils;

/**
 * SignUtils
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-09 16:47
 */
public class SignUtils {
	public static <T> String sign(String accessSecret, String url) throws IllegalAccessException {
		Map<String, Object> signMap = new HashMap<>();
		Map<String, Object> paramMap = getUrlParams(url);
		if (paramMap != null) {
			signMap.putAll(paramMap);
		}

		StringBuffer sb = new StringBuffer();
		signMap.forEach((k, v) -> {
			sb.append(k).append("=").append(v).append("&");
		});
		sb.deleteCharAt(sb.length()-1).append(accessSecret);

		return url + "&sig=" + MD5Utils.md5(sb.toString());
	}

	public static <T> String sign(String accessSecret, String url, Map<String, Object> headers, T body) throws IllegalAccessException {
		Map<String, Object> signMap = new HashMap<>();
		if (headers != null) {
			signMap.putAll(headers);
		}
		Map<String, Object> paramMap = getUrlParams(url);
		if (paramMap != null) {
			signMap.putAll(paramMap);
		}
		Map<String, Object> bodyMap = getBodyParams(body);
		if (bodyMap != null) {
			signMap.putAll(bodyMap);
		}

		StringBuffer sb = new StringBuffer();
		signMap.forEach((k, v) -> {
			sb.append(k).append("=").append(v).append("&");
		});
		sb.append("accessSecret=").append(accessSecret);
		return stringToMD5(sb.toString());
	}

	private static Map<String, Object> getUrlParams(String url) {
		if (StringUtils.isBlank(url) || !url.contains("?")) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<>();
		String params = url.split("\\?")[1];
		for (String param : params.split("&")) {
			String[] p = param.split("=");
			paramMap.put(p[0], p[1]);
		}
		return paramMap;
	}

	private static <T> Map<String, Object> getBodyParams(T body) throws IllegalAccessException {
		if (body == null) {
			return null;
		}
		Map<String, Object> bodyMap = new HashMap<>();
		for (Field field : body.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			bodyMap.put(field.getName(), field.get(body));
		}
		return bodyMap;
	}

	private static String stringToMD5(String plainText) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(
				plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有这个md5算法！");
		}

		return new BigInteger(1, secretBytes).toString(16);
	}
}
