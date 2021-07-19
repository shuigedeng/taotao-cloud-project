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
package com.taotao.cloud.gateway.filter.global;

import com.taotao.cloud.common.utils.LogUtil;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Part;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * SignFilter
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/07/19 14:42
 */
@Component
public class SignFilter implements GlobalFilter {

	private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies
		.withDefaults().messageReaders();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		LogUtil.info("访问地址：" + request.getURI());

		// 请求参数上的url地址
		Map<String, String> params = new HashMap<>();
		request.getQueryParams().forEach((key, items) -> {
			params.put(key, items.get(0));
		});
		if ("GET".equals(request.getMethodValue())) {
			return this.checkSign(params, chain, exchange);
		} else if ("POST".equals(request.getMethodValue())) {
			return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
				DataBufferUtils.retain(dataBuffer);
				final Flux<DataBuffer> cachedFlux = Flux
					.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
				final ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
					exchange.getRequest()) {
					@NotNull
					@Override
					public Flux<DataBuffer> getBody() {
						return cachedFlux;
					}
				};
				final ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest)
					.build();

				return cacheBody(mutatedExchange, chain, params);
			});

		}
		return chain.filter(exchange);
	}

	/***
	 * 验证签名
	 * @author Lance lance_lan_2016@163.com
	 * @date 2020-01-07 09:57
	 * @param params
	 * @param chain
	 * @param exchange
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 *
	 * */
	private Mono<Void> checkSign(Map<String, String> params, GatewayFilterChain chain,
		ServerWebExchange exchange) {
		LogUtil.info("校验参数集合：" + params);
//		if (!MD5Sign.checkSign(appSecret, params)) {
//			// 返回json格式
//			JsonResponse jsonResponse = new JsonResponse();
//			jsonResponse.errorAuth();
//
//			exchange.getResponse().setStatusCode(HttpStatus.OK);
//			exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
//			return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(
//				JsonUtils.toString(jsonResponse).getBytes())));
//		}
		return chain.filter(exchange);
	}

	@SuppressWarnings("unchecked")
	private Mono<Void> cacheBody(ServerWebExchange exchange, GatewayFilterChain chain,
		Map<String, String> params) {
		final HttpHeaders headers = exchange.getRequest().getHeaders();
		if (headers.getContentLength() == 0) {
			return chain.filter(exchange);
		}
		final ResolvableType resolvableType;
		if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(headers.getContentType())) {
			resolvableType = ResolvableType
				.forClassWithGenerics(MultiValueMap.class, String.class, Part.class);
		} else {
			resolvableType = ResolvableType.forClass(String.class);
		}

		return MESSAGE_READERS.stream()
			.filter(reader -> reader.canRead(resolvableType, exchange.getRequest().getHeaders().getContentType()))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("no suitable HttpMessageReader."))
			.readMono(resolvableType, exchange.getRequest(), Collections.emptyMap())
			.flatMap(resolvedBody -> {
				if (resolvedBody instanceof MultiValueMap) {
					@SuppressWarnings("rawtypes")
					MultiValueMap<String, Object> map = (MultiValueMap) resolvedBody;
					map.keySet().forEach(key -> {
//                            SynchronossPartHttpMessageReader
						Object obj = map.get(key);
						List<Object> list = (List<Object>) obj;
						for (Object object : list) {
							if ("class org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader$SynchronossFilePart"
								.equals(object.getClass().toString())) {
								continue;
							}
							Field[] fields = object.getClass().getDeclaredFields();
							try {
								for (Field field : fields) {
									field.setAccessible(true);
									params.put(key, field.get(object) + "");
								}
							} catch (IllegalAccessException e) {
								e.printStackTrace();
								LogUtil.info(e.getLocalizedMessage());
							}
						}
					});
				} else {
					if (null != resolvedBody) {
						String path = null;
						try {
							path = URLDecoder.decode(resolvedBody.toString(), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							LogUtil.error(e.getLocalizedMessage());
						}
						if (null != path) {
							String[] items = path.split("&");
							for (String item : items) {
								String[] subItems = item.split("=");
								if (subItems.length == 2) {
									params.put(subItems[0], subItems[1]);
								}
							}
						}
					}
				}
				return this.checkSign(params, chain, exchange);
			});
	}
}
