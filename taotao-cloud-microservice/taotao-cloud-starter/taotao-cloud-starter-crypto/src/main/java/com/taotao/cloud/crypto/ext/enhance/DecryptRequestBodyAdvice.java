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
package com.taotao.cloud.crypto.ext.enhance;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.taotao.cloud.common.utils.common.JsonUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import com.taotao.cloud.crypto.ext.annotation.Crypto;
import com.taotao.cloud.crypto.ext.processor.HttpCryptoProcessor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

/**
 * <p>Description: RequestBody 解密 Advice</p>
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 11:30:14
 */
@RestControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

	private static final Logger log = LoggerFactory.getLogger(DecryptRequestBodyAdvice.class);

	private HttpCryptoProcessor httpCryptoProcessor;

	public void setInterfaceCryptoProcessor(HttpCryptoProcessor httpCryptoProcessor) {
		this.httpCryptoProcessor = httpCryptoProcessor;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) {

		String methodName = methodParameter.getMethod().getName();
		Crypto crypto = methodParameter.getMethodAnnotation(Crypto.class);

		boolean isSupports = ObjectUtils.isNotEmpty(crypto) && crypto.requestDecrypt();

		log.trace(
			"Is DecryptRequestBodyAdvice supports method [{}] ? Status is [{}].",
			methodName, isSupports);
		return isSupports;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage,
		MethodParameter methodParameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		String sessionKey = httpInputMessage.getHeaders().get("session-key").get(0);

		if (StringUtils.isBlank(sessionKey)) {
			log.warn(
				"Cannot find Herodotus Cloud custom session header. Use interface crypto founction need add X_HERODOTUS_SESSION to request header.");
			return httpInputMessage;
		}

		log.info("DecryptRequestBodyAdvice begin decrypt data.");

		String methodName = methodParameter.getMethod().getName();
		String className = methodParameter.getDeclaringClass().getName();

		String content = IoUtil.read(httpInputMessage.getBody()).toString();

		if (StringUtils.isNotBlank(content)) {
			String data = httpCryptoProcessor.decrypt(sessionKey, content);
			if (StringUtils.equals(data, content)) {
				data = decrypt(sessionKey, content);
			}
			log.debug("Decrypt request body for rest method [{}] in [{}] finished.",
				methodName, className);
			return new DecryptHttpInputMessage(httpInputMessage, StrUtil.utf8Bytes(data));
		} else {
			return httpInputMessage;
		}
	}

	private String decrypt(String sessionKey, String content)  {
		JsonNode jsonNode = JsonUtils.parse(content);
		if (ObjectUtils.isNotEmpty(jsonNode)) {
			decrypt(sessionKey, jsonNode);
			return JsonUtils.toJson(jsonNode);
		}

		return content;
	}

	private void decrypt(String sessionKey, JsonNode jsonNode)  {
		if (jsonNode.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
			while (it.hasNext()) {
				Map.Entry<String, JsonNode> entry = it.next();
				if (entry.getValue() instanceof TextNode && entry.getValue().isValueNode()) {
					TextNode t = (TextNode) entry.getValue();
					String value = httpCryptoProcessor.decrypt(sessionKey, t.asText());
					entry.setValue(new TextNode(value));
				}
				decrypt(sessionKey, entry.getValue());
			}
		}

		if (jsonNode.isArray()) {
			for (JsonNode node : jsonNode) {
				decrypt(sessionKey, node);
			}
		}
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
		MethodParameter parameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage,
		MethodParameter parameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	public static class DecryptHttpInputMessage implements HttpInputMessage {

		private final HttpInputMessage httpInputMessage;
		private final byte[] data;

		public DecryptHttpInputMessage(HttpInputMessage httpInputMessage, byte[] data) {
			this.httpInputMessage = httpInputMessage;
			this.data = data;
		}

		@Override
		public InputStream getBody() throws IOException {
			return new ByteArrayInputStream(this.data);
		}

		@Override
		public HttpHeaders getHeaders() {
			return this.httpInputMessage.getHeaders();
		}
	}
}
