/**
 * Copyright (c) 2015-2016 the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.taotao.cloud.rxjava.async;

import io.reactivex.Observable;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * A specialized {@link DeferredResult} that handles {@link Observable} type.
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:48:52
 */
public class ObservableDeferredResult<T> extends DeferredResult<List<T>> {

	private static final Object EMPTY_RESULT = new Object();

	private final DeferredResultObserver<List<T>> observer;

	public ObservableDeferredResult(Observable<T> observable) {
		this(null, EMPTY_RESULT, observable);
	}

	public ObservableDeferredResult(long timeout, Observable<T> observable) {
		this(timeout, EMPTY_RESULT, observable);
	}

	public ObservableDeferredResult(Long timeout, Object timeoutResult, Observable<T> observable) {
		super(timeout, timeoutResult);
		Assert.notNull(observable, "observable can not be null");

		observer = new DeferredResultObserver<List<T>>(observable.toList().toObservable(), this);
	}
}
