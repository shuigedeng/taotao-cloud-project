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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * 采用 CompletableFuture 简化异步使用
 *
 */
@ParametersAreNonnullByDefault
public class CompletableCallback implements Callback {
	private final CompletableFuture<ResponseSpec> future;

	public CompletableCallback(CompletableFuture<ResponseSpec> future) {
		this.future = future;
	}

	@Override
	public void onFailure(Call call, IOException e) {
		// 此处封装一个 failResponse？是否有必要？好像没有必要
		future.completeExceptionally(e);
	}

	@Override
	public void onResponse(Call call, Response response) throws IOException {
		try (BytesResponse bytesResponse = new BytesResponse(response)) {
			future.complete(bytesResponse);
		}
	}
}
