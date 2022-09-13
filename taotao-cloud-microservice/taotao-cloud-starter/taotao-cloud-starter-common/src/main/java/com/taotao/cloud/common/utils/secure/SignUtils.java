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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * SignUtils
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-09 16:47
 */
public class SignUtils {

	//private final static String AMAP_KEY = System.getenv("AMAP_KEY");
	//private final static String AMAP_SECURITY_KEY = System.getenv("AMAP_SECURITY_KEY");
	//private final static String syncUrl =
	//	"https://restapi.amap.com/v3/config/district?subdistrict=4&key=" + AMAP_KEY;
	//
	//public static void main(String[] args) throws IllegalAccessException {
	//	String sign = sign(AMAP_SECURITY_KEY, syncUrl);
	//	System.out.println(sign);
	//}

	public static <T> String sign(String accessSecret, String url) throws IllegalAccessException {
		Map<String, Object> signMap = new HashMap<>();
		Map<String, Object> paramMap = getUrlParams(url);
		if (paramMap != null) {
			signMap.putAll(paramMap);
		}

		StringBuffer sb = new StringBuffer();
		sortMapByKey(signMap).forEach((k, v) -> {
			sb.append(k).append("=").append(v).append("&");
		});
		sb.deleteCharAt(sb.length() - 1).append(accessSecret);

		return url + "&sig=" + MD5Utils.md5(sb.toString());
	}


	/**
	 * Map 按 value 进行排序
	 *
	 * @param map
	 * @return
	 */

	public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, String> sortedMap = new LinkedHashMap<>();
		List<Entry<String, String>> entryList = new ArrayList<>(oriMap.entrySet());
		Collections.sort(entryList, new MapValueComparator());
		Iterator<Entry<String, String>> iter = entryList.iterator();
		Map.Entry<String, String> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;

	}

	/**
	 * 将 Url Params String 转为 Map
	 *
	 * @param param aa=11&bb=22&cc=33
	 * @return map
	 */
	public static Map<String, Object> urlParams2Map(String param) {
		Map<String, Object> map = new HashMap<>();
		if ("".equals(param) || null == param) {
			return map;
		}
		String[] params = param.split("&");
		for (String s : params) {
			String[] p = s.split("=");
			if (p.length == 2) {
				map.put(p[0], p[1]);
			}
		}
		return map;
	}

	/**
	 * 将 map 转为 Url Params String
	 *
	 * @param map
	 * @return aa=11&bb=22&cc=33
	 */
	public static String map2UrlParams(Map<String, String> map, boolean isSort) {
		if (map == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		List<String> keys = new ArrayList<>(map.keySet());
		if (isSort) {
			Collections.sort(keys);
		}
		for (String key : keys) {
			String value = map.get(key);
			sb.append(key).append("=").append(value);
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = s.substring(0, s.lastIndexOf("&"));
		}
		return s;
	}

	static class MapValueComparator implements Comparator<Map.Entry<String, String>> {
		@Override
		public int compare(Entry<String, String> me1, Entry<String, String> me2) {
			return me1.getValue().compareTo(me2.getValue());
		}
	}


	public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}

	static class MapKeyComparator implements Comparator<String> {

		@Override
		public int compare(String str1, String str2) {
			return str1.compareTo(str2);
		}
	}

	public static <T> String sign(String accessSecret, String url, Map<String, Object> headers,
		T body) throws IllegalAccessException {
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
