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
import com.taotao.cloud.common.constant.StringPoolConstant;
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
import lombok.val;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
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
 * @author dengtao
 * @version 1.0.0
 * @since 2020/6/2 16:43
 */
public class RequestUtil {

	private final static String UNKNOWN_STR = "unknown";

	private static final ThreadLocal<WebContext> THREAD_CONTEXT = new TransmittableThreadLocal<>();

	public static WebContext getContext() {
		return THREAD_CONTEXT.get();
	}

	public static HttpServletRequest getRequest() {
		WebContext webContext = getContext();
		return webContext == null ? null : webContext.request;
	}

	public static HttpServletResponse getResponse() {
		WebContext webContext = getContext();
		return webContext == null ? null : webContext.response;
	}

	public static void bindContext(HttpServletRequest request, HttpServletResponse response) {
		THREAD_CONTEXT.set(new WebContext(request, response));
	}

	public static void clearContext() {
		THREAD_CONTEXT.remove();
	}

	public static class WebContext {

		private final HttpServletRequest request;
		private final HttpServletResponse response;

		public WebContext(HttpServletRequest request, HttpServletResponse response) {
			this.request = request;
			this.response = response;
		}
	}

	public static String getBaseUrl() {
		if (!isWeb()) {
			return "";
		}
		val webServer = getConfigurableWebServerApplicationContext().getWebServer();
		if (webServer == null) {
			return "";
		}
		return "http://" + getIpAddress() + ":" + webServer.getPort();
	}

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

	public static ConfigurableWebServerApplicationContext getConfigurableWebServerApplicationContext() {
		ApplicationContext context = ContextUtil.getApplicationContext();
		if (context instanceof ConfigurableWebServerApplicationContext) {
			return (ConfigurableWebServerApplicationContext) context;
		}
		return null;
	}

	public static boolean isWeb() {
		return getConfigurableWebServerApplicationContext() != null;
	}

	/**
	 * 从Flux<DataBuffer>中获取字符串的方法
	 *
	 * @return 请求体
	 */
	public static String getBodyString(ServerHttpRequest serverHttpRequest) {
		HttpMessageReader<byte[]> httpMessageReader = new DecoderHttpMessageReader(
			new ByteArrayDecoder());
		ResolvableType resolvableType = ResolvableType.forClass(byte[].class);
		Mono<byte[]> mono = httpMessageReader
			.readMono(resolvableType, serverHttpRequest, Collections.emptyMap());
		return mono.map(String::new).block();
	}

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

	private static final String UNKNOWN = "unknown";


	public static HttpServletRequest getHttpServletRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null
			: ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	/**
	 * 获取请求IP
	 *
	 * @return String IP
	 */
	public static String getHttpServletRequestIpAddress() {
		HttpServletRequest request = getHttpServletRequest();
		return getHttpServletRequestIpAddress(request);
	}

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

	public static String getServerHttpRequestIpAddress(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		String ip = headers.getFirst("x-forwarded-for");
		if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
			if (ip.contains(StringPoolConstant.COMMA)) {
				ip = ip.split(StringPoolConstant.COMMA)[0];
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
	 * 获取客户端IP地址
	 *
	 * @param request request
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:58
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
	 * @author dengtao
	 * @since 2021/2/25 16:58
	 */
	public static boolean isEmptyIp(String ip) {
		return StrUtil.isEmpty(ip) || UNKNOWN_STR.equalsIgnoreCase(ip);
	}

	/**
	 * 获取本机的IP地址
	 *
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:58
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
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:33
	 */
	public static String getIpAddress() {
		String ipExclude = "";
		if (StringUtil.hasText(ipExclude)) {
			String regex = buildRegex(ipExclude);
			return getIpAddressExMatched(regex);
		}

		String ipInclude = "";
		if (StringUtil.hasText(ipInclude)) {
			String regex = buildRegex(ipInclude);
			return getIpAddressMatched(regex);
		}

		return getIpAddress0();
	}

	/**
	 * 获取ip地址
	 *
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:33
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
					continue;
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
		} catch (Exception ignored) {
		}
		return "";
	}

	/**
	 * 获取指定网段地址
	 *
	 * @param regex 10.0.18 网址前两个或前三个地址段
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:33
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
		} catch (Exception ignored) {
		}
		return "";
	}

	/**
	 * 获取指定网段地址
	 *
	 * @param regex 10.0.18 排除地址段，两个或前三个地址段
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:34
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
		} catch (Exception ignored) {
		}
		return "";
	}

	/**
	 * 构建正则表达式
	 *
	 * @param source source
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:34
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
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2020/10/15 15:46
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
