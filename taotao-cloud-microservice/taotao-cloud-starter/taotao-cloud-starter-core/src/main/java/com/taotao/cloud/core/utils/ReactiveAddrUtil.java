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
package com.taotao.cloud.core.utils;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.LogUtil;
import lombok.experimental.UtilityClass;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * ReactiveAddrUtil
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 22:14
 */
@UtilityClass
public class ReactiveAddrUtil {

	private final String UNKNOWN_STR = "unknown";

	/**
	 * 获取客户端IP地址
	 *
	 * @param request request
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2020/10/15 15:46
	 */
	public String getRemoteAddr(ServerHttpRequest request) {
		Map<String, String> headers = request.getHeaders().toSingleValueMap();
		String ip = headers.get("X-Forwarded-For");
		if (isEmptyIp(ip)) {
			ip = headers.get("Proxy-Client-IP");
			if (isEmptyIp(ip)) {
				ip = headers.get("WL-Proxy-Client-IP");
				if (isEmptyIp(ip)) {
					ip = headers.get("HTTP_CLIENT_IP");
					if (isEmptyIp(ip)) {
						ip = headers.get("HTTP_X_FORWARDED_FOR");
						if (isEmptyIp(ip)) {
							ip = request.getRemoteAddress().getAddress().getHostAddress();
							if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
								// 根据网卡取本机配置的IP
								ip = getLocalAddr();
							}
						}
					}
				}
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = ips[index];
				if (!isEmptyIp(ip)) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	private boolean isEmptyIp(String ip) {
		if (StrUtil.isEmpty(ip) || UNKNOWN_STR.equalsIgnoreCase(ip)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取本机的IP地址
	 *
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 17:04
	 */
	public String getLocalAddr() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LogUtil.error("InetAddress.getLocalHost()-error", e);
		}
		return "";
	}
}
