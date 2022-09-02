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
package com.taotao.cloud.common.http.retry;


import com.taotao.cloud.common.support.function.CheckedCallable;

/**
 * 重试接口
 *
 */
public interface IRetry {

	/**
	 * Execute the supplied {@link CheckedCallable} with the configured retry semantics. See
	 * implementations for configuration details.
	 *
	 * @param <T>           the return value
	 * @param retryCallback the {@link CheckedCallable}
	 * @param <E>           the exception thrown
	 * @return T the return value
	 * @throws E the exception thrown
	 */
	<T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;

}
