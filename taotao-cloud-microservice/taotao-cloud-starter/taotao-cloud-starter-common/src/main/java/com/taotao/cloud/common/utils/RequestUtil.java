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
import com.alibaba.ttl.TransmittableThreadLocal;
import com.taotao.cloud.common.constant.StrPool;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

/**
 * RequestUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:52:45
 */
public class RequestUtil {

	/**
	 * UNKNOWN
	 */
	private static final String UNKNOWN = "unknown";
	/**
	 * IP_INCLUDE_REGEX_KEY
	 */
	public static final String IP_INCLUDE_REGEX_KEY = "taotao.cloud.core.ip.include.regex";
	/**
	 * IP_EXCLUDE_REGEX_KEY
	 */
	public static final String IP_EXCLUDE_REGEX_KEY = "taotao.cloud.core.ip.exclude.regex";
	/**
	 * UNKNOWN_STR
	 */
	private final static String UNKNOWN_STR = "unknown";
	/**
	 * WEB_CONTEXT
	 */
	private static final ThreadLocal<WebContext> WEB_CONTEXT = new TransmittableThreadLocal<>();

	/**
	 * getContext
	 *
	 * @return {@link RequestUtil.WebContext }
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:14
	 */
	public static WebContext getContext() {
		return WEB_CONTEXT.get();
	}

	/**
	 * getRequest
	 *
	 * @return {@link javax.servlet.http.HttpServletRequest }
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:17
	 */
	public static HttpServletRequest getRequest() {
		WebContext webContext = getContext();
		return webContext == null ? null : webContext.request;
	}

	/**
	 * getResponse
	 *
	 * @return {@link javax.servlet.http.HttpServletResponse }
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:20
	 */
	public static HttpServletResponse getResponse() {
		WebContext webContext = getContext();
		return webContext == null ? null : webContext.response;
	}

	/**
	 * bindContext
	 *
	 * @param request  request
	 * @param response response
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:23
	 */
	public static void bindContext(HttpServletRequest request, HttpServletResponse response) {
		WEB_CONTEXT.set(new WebContext(request, response));
	}

	/**
	 * clearContext
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:26
	 */
	public static void clearContext() {
		WEB_CONTEXT.remove();
	}

	/**
	 * WebContext
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:54:32
	 */
	public static class WebContext {

		/**
		 * request
		 */
		private final HttpServletRequest request;
		/**
		 * response
		 */
		private final HttpServletResponse response;

		public WebContext(HttpServletRequest request, HttpServletResponse response) {
			this.request = request;
			this.response = response;
		}
	}

	/**
	 * getBaseUrl
	 *
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:35
	 */
	public static String getBaseUrl() {
		if (!isWeb()) {
			return "";
		}
		WebServer webServer = getConfigurableWebServerApplicationContext().getWebServer();
		if (webServer == null) {
			return "";
		}
		return "http://" + getIpAddress() + ":" + webServer.getPort();
	}

	/**
	 * getAllRequestParam
	 *
	 * @param request request
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:50
	 */
	public static Map<String, String> getAllRequestParam(HttpServletRequest request) {
		Map<String, String> res = new HashMap<>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
			}
		}
		return res;
	}

	/**
	 * getAllRequestHeaders
	 *
	 * @param request request
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:54
	 */
	public static Map<String, String> getAllRequestHeaders(HttpServletRequest request) {
		Map<String, String> res = new HashMap<>();
		Enumeration<?> temp = request.getHeaderNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getHeader(en);
				res.put(en, value);
			}
		}
		return res;
	}

	/**
	 * getConfigurableWebServerApplicationContext
	 *
	 * @return {@link org.springframework.boot.web.context.ConfigurableWebServerApplicationContext }
	 * @author shuigedeng
	 * @since 2021-09-02 20:54:57
	 */
	public static ConfigurableWebServerApplicationContext getConfigurableWebServerApplicationContext() {
		ApplicationContext context = ContextUtil.getApplicationContext();
		if (context instanceof ConfigurableWebServerApplicationContext) {
			return (ConfigurableWebServerApplicationContext) context;
		}
		return null;
	}

	/**
	 * isWeb
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:00
	 */
	public static boolean isWeb() {
		return getConfigurableWebServerApplicationContext() != null;
	}

	/**
	 * getBodyString
	 *
	 * @param serverHttpRequest serverHttpRequest
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:03
	 */
	public static String getBodyString(ServerHttpRequest serverHttpRequest) {
		HttpMessageReader<byte[]> httpMessageReader = new DecoderHttpMessageReader(
			new ByteArrayDecoder());
		ResolvableType resolvableType = ResolvableType.forClass(byte[].class);
		Mono<byte[]> mono = httpMessageReader
			.readMono(resolvableType, serverHttpRequest, Collections.emptyMap());
		return mono.map(String::new).block();
	}

	/**
	 * getBodyString
	 *
	 * @param request request
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:07
	 */
	public static String getBodyString(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = request.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString().trim();
	}


	/**
	 * getHttpServletRequest
	 *
	 * @return {@link javax.servlet.http.HttpServletRequest }
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:30
	 */
	public static HttpServletRequest getHttpServletRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null
			: ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	public static HttpServletResponse getHttpServletResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null
			: ((ServletRequestAttributes) requestAttributes).getResponse();
	}

	/**
	 * getHttpServletRequestIpAddress
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:33
	 */
	public static String getHttpServletRequestIpAddress() {
		HttpServletRequest request = getHttpServletRequest();
		assert request != null;
		return getHttpServletRequestIpAddress(request);
	}

	/**
	 * getHttpServletRequestIpAddress
	 *
	 * @param request request
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:36
	 */
	public static String getHttpServletRequestIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.contains(",")) {
			ip = ip.split(",")[0];
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}


	/**
	 * getServerHttpRequestIpAddress
	 *
	 * @param request request
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:39
	 */
	public static String getServerHttpRequestIpAddress(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		String ip = headers.getFirst("x-forwarded-for");
		if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
			if (ip.contains(StrPool.COMMA)) {
				ip = ip.split(StrPool.COMMA)[0];
			}
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}


	/**
	 * getRemoteAddr
	 *
	 * @param request request
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:43
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
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
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:52
	 */
	public static boolean isEmptyIp(String ip) {
		return StrUtil.isEmpty(ip) || UNKNOWN_STR.equalsIgnoreCase(ip);
	}

	/**
	 * 获取本机的IP地址
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:55:59
	 */
	public static String getLocalAddr() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LogUtil.error("InetAddress.getLocalHost()--error", e);
		}
		return "";
	}


	/**
	 * 获取ip地址
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:56:05
	 */
	public static String getIpAddress() {
		String ipExclude = PropertyUtil.getPropertyCache(IP_EXCLUDE_REGEX_KEY, "");
		if (org.springframework.util.StringUtils.hasText(ipExclude)) {
			String regex = buildRegex(ipExclude);
			return getIpAddressExMatched(regex);
		}

		String ipInclude = PropertyUtil.getPropertyCache(IP_INCLUDE_REGEX_KEY, "");
		if (org.springframework.util.StringUtils.hasText(ipInclude)) {
			String regex = buildRegex(ipInclude);
			return getIpAddressMatched(regex);
		}

		return getIpAddress0();
	}

	/**
	 * 获取ip地址
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:56:11
	 */
	public static String getIpAddress0() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
				.getNetworkInterfaces();
			InetAddress ip;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()
					|| netInterface.isPointToPoint()) {
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = addresses.nextElement();
						if (ip instanceof Inet4Address) {
							return ip.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return "";
	}

	/**
	 * 获取指定网段地址
	 *
	 * @param regex 10.0.18 网址前两个或前三个地址段
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:56:20
	 */
	public static String getIpAddressMatched(String regex) {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
				.getNetworkInterfaces();
			InetAddress ip;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = addresses.nextElement();
						if (ip instanceof Inet4Address) {
							String strIp = ip.getHostAddress();
							//如果匹配网段则返回
							if (Pattern.matches(regex, strIp)) {
								return strIp;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return "";
	}

	/**
	 * 获取指定网段地址
	 *
	 * @param regex 10.0.18 排除地址段，两个或前三个地址段
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:56:27
	 */
	public static String getIpAddressExMatched(String regex) {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
				.getNetworkInterfaces();
			InetAddress ip;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = addresses.nextElement();
						if (ip instanceof Inet4Address) {
							String strIp = ip.getHostAddress();
							//如果不匹配匹配网段则返回;
							if (!Pattern.matches(regex, strIp)) {
								return strIp;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return "";
	}

	/**
	 * 构建正则表达式
	 *
	 * @param source source
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:56:35
	 */
	private static String buildRegex(String source) {
		StringBuilder sb = new StringBuilder();
		String[] strSource = source.split(",");
		for (String s : strSource) {
			sb.append("|(^").append(s).append(".*)");
		}
		String regex = sb.toString();
		if (!StringUtil.isEmpty(regex)) {
			//去掉开头|号
			return regex.substring(1);
		}
		return "";
	}

	/**
	 * 获取客户端IP地址
	 *
	 * @param request request
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:56:44
	 */
	public static String getRemoteAddr(ServerHttpRequest request) {
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
}
