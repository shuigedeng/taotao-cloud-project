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
package com.taotao.cloud.common.utils.ip;

import com.fasterxml.jackson.databind.JsonNode;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.io.net.HttpUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
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
	 * @return ip地址
	 * @since 2022-03-23 08:19:10
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
	 * 根据ip获取详细地址
	 *
	 * @param ip ip
	 * @return 详细地址
	 * @since 2022-03-23 08:17:23
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
}
