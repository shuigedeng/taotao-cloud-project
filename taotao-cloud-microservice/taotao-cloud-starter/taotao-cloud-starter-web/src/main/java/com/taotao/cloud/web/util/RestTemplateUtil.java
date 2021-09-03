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
package com.taotao.cloud.web.util;

import java.util.Arrays;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:26:13
 */
public class RestTemplateUtil {

	/**
	 * get请求（超时设置）
	 *
	 * @param url url
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-02 22:26:22
	 */
	public static Map httpGetRequestFactoryToMap(String url) {
		//超时处理设置
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(1000);
		requestFactory.setReadTimeout(1000);
		//应用超时设置
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate.getForObject(url, Map.class);
	}

	/**
	 * get请求（无超时设置）
	 *
	 * @param url url
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-02 22:26:32
	 */
	public static Map restTemplateGetToMap(String url) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, Map.class);
	}

	/**
	 * get请求 并接收封装成string
	 *
	 * @param url url
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 22:26:42
	 */
	public static String restTemplateGetToStr(String url) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, String.class);
	}

	/**
	 * get请求 并添加消息头
	 *
	 * @param url url
	 * @return {@link org.springframework.http.ResponseEntity }
	 * @author shuigedeng
	 * @since 2021-09-02 22:26:50
	 */
	public static ResponseEntity<String> httpGetHeaders(String url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	}

	/**
	 * post请求 添加请求参数以及消息头
	 *
	 * @param url  url
	 * @param map  map
	 * @param type type
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 22:26:59
	 */
	public static String getCommonPolicyJson(String url,
			MultiValueMap<String, String> map,
			MediaType type) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		//设置header
		headers.setContentType(type);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				map, headers);

		//执行请求
		ResponseEntity<String> resp = restTemplate.postForEntity(url, request, String.class);
		return resp.getBody();
	}
}
