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

import com.taotao.cloud.common.utils.NetworkUtil;
import lombok.val;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * WebUtil
 *
 * @author dengtao
 * @date 2020/6/2 16:43
 * @since v1.0
 */
public class WebUtil {

	private static final ThreadLocal<WebContext> THREAD_CONTEXT = new ThreadLocal<>();

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
		return "http://" + NetworkUtil.getIpAddress() + ":" + webServer.getPort();
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
		HttpMessageReader<byte[]> httpMessageReader = new DecoderHttpMessageReader(new ByteArrayDecoder());
		ResolvableType resolvableType = ResolvableType.forClass(byte[].class);
		Mono<byte[]> mono = httpMessageReader.readMono(resolvableType, serverHttpRequest, Collections.emptyMap());
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


}
