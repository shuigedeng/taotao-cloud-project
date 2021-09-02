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

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Objects;

/**
 * IP工具类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 16:41:13
 */
public class IPUtil {

	private IPUtil() {
	}

	/**
	 * IP_LOCAL
	 */
	private final static boolean IP_LOCAL = false;

	/**
	 * 根据ip获取详细地址
	 *
	 * @param ip ip
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:41:27
	 */
	public static String getCityInfo(String ip) {
		if (IP_LOCAL) {
			//待开发
			return null;
		} else {
			return getHttpCityInfo(ip);
		}
	}

	/**
	 * 根据ip获取详细地址 临时使用，待调整
	 *
	 * @param ip ip
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:41:38
	 */
	public static String getHttpCityInfo(String ip) {
		String api = String.format("http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true", ip);
		JsonNode node = JsonUtil.parse((String) HttpUtil.getRequest(api, "gbk"));
		if (Objects.nonNull(node)) {
			LogUtil.info(node.toString());
			return node.get("addr").toString();
		}

		return null;
	}

	//public static void main(String[] args) {
	//	System.err.println(getCityInfo("220.248.12.158"));
	//}
}
