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

import cn.hutool.core.util.StrUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

/**
 * AddrUtils
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2019/9/8
 */
@UtilityClass
public class AddrUtil {

	private final static String UNKNOWN_STR = "unknown";

	/**
	 * 获取客户端IP地址
	 *
	 * @param request request
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:58
	 */
	public String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (isEmptyIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			if (isEmptyIp(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (isEmptyIp(ip)) {
					ip = request.getHeader("HTTP_CLIENT_IP");
					if (isEmptyIp(ip)) {
						ip = request.getHeader("HTTP_X_FORWARDED_FOR");
						if (isEmptyIp(ip)) {
							ip = request.getRemoteAddr();
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
			for (String strIp : ips) {
				if (!isEmptyIp(ip)) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 判断ip地址是否为空
	 *
	 * @param ip ip地址
	 * @return boolean
	 * @author dengtao
	 * @since 2021/2/25 16:58
	 */
	public boolean isEmptyIp(String ip) {
		return StrUtil.isEmpty(ip) || UNKNOWN_STR.equalsIgnoreCase(ip);
	}

	/**
	 * 获取本机的IP地址
	 *
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:58
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
