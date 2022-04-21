/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.support;

import com.taotao.cloud.dingtalk.exception.SendMsgException;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Dinger默认Http客户端
 *

 * @since 1.0
 */
public class DingerHttpTemplate extends AbstractDingerHttpClient {

	private RestTemplate restTemplate;

	public DingerHttpTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public <T> String post(String url, Map<String, String> headers, T message)
		throws SendMsgException {
		HttpHeaders httpHeaders = new HttpHeaders();
		headers.forEach((headerName, headerValue) -> {
			httpHeaders.set(headerName, headerValue);
		});

		HttpEntity<T> request = new HttpEntity<>(message, httpHeaders);
		return restTemplate.postForObject(url, request, String.class);
	}
}
