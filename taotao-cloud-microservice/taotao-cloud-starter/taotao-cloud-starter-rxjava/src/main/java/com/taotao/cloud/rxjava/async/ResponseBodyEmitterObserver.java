/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.rxjava.async;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * Subscriber that any value produced by the {@link Observable} into the {@link ResponseBodyEmitter}.
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:49:18
 */
class ResponseBodyEmitterObserver<T> extends DisposableObserver<T> implements Runnable {

	private final MediaType mediaType;

	private final ResponseBodyEmitter responseBodyEmitter;

	private boolean completed;

	public ResponseBodyEmitterObserver(MediaType mediaType, Observable<T> observable,
		ResponseBodyEmitter responseBodyEmitter) {

		this.mediaType = mediaType;
		this.responseBodyEmitter = responseBodyEmitter;
		this.responseBodyEmitter.onTimeout(this);
		this.responseBodyEmitter.onCompletion(this);
		observable.subscribe(this);
	}

	@Override
	public void onNext(T value) {

		try {
			if (!completed) {
				responseBodyEmitter.send(value, mediaType);
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void onError(Throwable e) {
		responseBodyEmitter.completeWithError(e);
	}

	@Override
	public void onComplete() {
		if (!completed) {
			completed = true;
			responseBodyEmitter.complete();
		}
	}

	@Override
	public void run() {
		this.dispose();
	}
}
