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

import io.reactivex.Single;
import org.springframework.util.Assert;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * A specialized {@link DeferredResult} that handles {@link Single} return type.
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:49:27
 */
public class SingleDeferredResult<T> extends DeferredResult<T> {

	private static final Object EMPTY_RESULT = new Object();

	private final DeferredResultObserver<T> observer;

	public SingleDeferredResult(Single<T> single) {
		this(null, EMPTY_RESULT, single);
	}

	public SingleDeferredResult(long timeout, Single<T> single) {
		this(timeout, EMPTY_RESULT, single);
	}

	public SingleDeferredResult(Long timeout, Object timeoutResult, Single<T> single) {
		super(timeout, timeoutResult);
		Assert.notNull(single, "single can not be null");

		observer = new DeferredResultObserver<T>(single.toObservable(), this);
	}
}
