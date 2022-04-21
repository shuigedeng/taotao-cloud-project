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
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A specialized {@link SseEmitter} that handles {@link Observable} types. The emitter subscribes to
 * the passed {@link Observable} instance and emits every produced value through {@link
 * #send(Object, MediaType)}.
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:49:03
 */
public class ObservableSseEmitter<T> extends SseEmitter {

	private final ResponseBodyEmitterObserver<T> observer;

	public ObservableSseEmitter(Observable<T> observable) {
		this(null, observable);
	}

	public ObservableSseEmitter(MediaType mediaType, Observable<T> observable) {
		this(null, mediaType, observable);
	}

	public ObservableSseEmitter(Long timeout, MediaType mediaType, Observable<T> observable) {
		super(timeout);
		this.observer = new ResponseBodyEmitterObserver<T>(mediaType, observable, this);
	}
}
