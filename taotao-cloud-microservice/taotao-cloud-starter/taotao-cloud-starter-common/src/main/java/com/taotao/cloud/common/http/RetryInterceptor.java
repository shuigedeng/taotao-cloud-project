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

package com.taotao.cloud.common.http;

import com.taotao.cloud.common.http.retry.IRetry;
import java.io.IOException;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 重试拦截器，应对代理问题
 *
 */
public class RetryInterceptor implements Interceptor {

	private final IRetry retry;
	@Nullable
	private final Predicate<ResponseSpec> respPredicate;

	public RetryInterceptor(IRetry retry,
		@Nullable Predicate<ResponseSpec> respPredicate) {
		this.retry = retry;
		this.respPredicate = respPredicate;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		return retry.execute(() -> {
			Response response = chain.proceed(request);
			// 结果集校验
			if (respPredicate == null) {
				return response;
			}
			// copy 一份 body
			ResponseBody body = response.peekBody(Long.MAX_VALUE);
			try (HttpResponse httpResponse = new HttpResponse(response)) {
				if (respPredicate.test(httpResponse)) {
					throw new IOException("Http Retry ResponsePredicate test Failure.");
				}
			}
			return response.newBuilder().body(body).build();
		});
	}

}
