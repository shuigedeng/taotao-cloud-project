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
package com.taotao.cloud.logger.logging.loki;

import com.github.loki4j.client.http.HttpConfig;
import com.github.loki4j.client.http.HttpHeaders;
import com.github.loki4j.client.http.Loki4jHttpClient;
import com.github.loki4j.client.http.LokiResponse;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * Loki4j OkHttpClient
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:42:15
 */
public class Loki4jOkHttpClient implements Loki4jHttpClient {
	private final HttpConfig conf;
	private final OkHttpClient httpClient;
	private final MediaType mediaType;
	private final Request requestBuilder;
	private byte[] bodyBuffer = new byte[0];

	public Loki4jOkHttpClient(HttpConfig conf) {
		this.conf = conf;
		this.httpClient = okHttpClientBuilder(conf);
		this.mediaType = MediaType.get(conf.contentType);
		this.requestBuilder = requestBuilder(conf);
	}

	private static OkHttpClient okHttpClientBuilder(HttpConfig conf) {
		return new OkHttpClient.Builder()
			.connectTimeout(conf.connectionTimeoutMs, TimeUnit.MICROSECONDS)
			.writeTimeout(conf.requestTimeoutMs, TimeUnit.MICROSECONDS)
			.readTimeout(conf.requestTimeoutMs, TimeUnit.MICROSECONDS)
			.build();
	}

	private static Request requestBuilder(HttpConfig conf) {
		Request.Builder request = new Request.Builder()
			.url(conf.pushUrl)
			.addHeader(HttpHeaders.CONTENT_TYPE, conf.contentType);
		conf.tenantId.ifPresent(tenant -> request.addHeader(HttpHeaders.X_SCOPE_ORGID, tenant));
		conf.basicAuthToken().ifPresent(token -> request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + token));
		return request.build();
	}

	@Override
	public HttpConfig getConfig() {
		return this.conf;
	}

	@Override
	public LokiResponse send(ByteBuffer batch) throws Exception {
		Request.Builder request = requestBuilder.newBuilder();
		if (batch.hasArray()) {
			request.post(RequestBody.create(batch.array(), mediaType, batch.position(), batch.remaining()));
		} else {
			int len = batch.remaining();
			if (len > bodyBuffer.length) {
				bodyBuffer = new byte[len];
			}
			batch.get(bodyBuffer, 0, len);
			request.post(RequestBody.create(bodyBuffer, mediaType, 0, len));
		}
		Call call = httpClient.newCall(request.build());
		try (Response response = call.execute()) {
			String body = response.body() != null ? response.body().string() : "";
			return new LokiResponse(response.code(), body);
		} catch (IOException e) {
			throw new RuntimeException("Error while sending batch to Loki", e);
		}
	}

	@Override
	public void close() throws Exception {
		httpClient.dispatcher().executorService().shutdown();
		httpClient.connectionPool().evictAll();
		assert httpClient.cache() != null;
		Util.closeQuietly(httpClient.cache());
	}
}
