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

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.crypto.ext.annotation.Crypto;
import com.taotao.cloud.crypto.ext.processor.HttpCryptoProcessor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <p>Description: 响应体加密Advice </p>
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 11:30:26
 */
@RestControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	private static final Logger log = LoggerFactory.getLogger(EncryptResponseBodyAdvice.class);

	private HttpCryptoProcessor httpCryptoProcessor;

	public void setInterfaceCryptoProcessor(HttpCryptoProcessor httpCryptoProcessor) {
		this.httpCryptoProcessor = httpCryptoProcessor;
	}

	@Override
	public boolean supports(MethodParameter methodParameter,
		Class<? extends HttpMessageConverter<?>> converterType) {

		String methodName = methodParameter.getMethod().getName();
		Crypto crypto = methodParameter.getMethodAnnotation(Crypto.class);

		boolean isSupports = ObjectUtils.isNotEmpty(crypto) && crypto.responseEncrypt();

		log.trace(
			"Is EncryptResponseBodyAdvice supports method [{}] ? Status is [{}].",
			methodName, isSupports);
		return isSupports;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
		MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {

		String sessionKey = request.getHeaders().get("session_key").get(0);

		if (StringUtils.isBlank(sessionKey)) {
			log.warn(
				"Cannot find Herodotus Cloud custom session header. Use interface crypto founction need add X_HERODOTUS_SESSION to request header.");
			return body;
		}

		log.info("EncryptResponseBodyAdvice begin encrypt data.");

		String methodName = methodParameter.getMethod().getName();
		String className = methodParameter.getDeclaringClass().getName();

		String bodyString = JsonUtils.toJson(body);
		String result = httpCryptoProcessor.encrypt(sessionKey, bodyString);
		if (StringUtils.isNotBlank(result)) {
			log.debug("Encrypt response body for rest method [{}] in [{}] finished.",
				methodName, className);
			return result;
		} else {
			return body;
		}
	}
}
