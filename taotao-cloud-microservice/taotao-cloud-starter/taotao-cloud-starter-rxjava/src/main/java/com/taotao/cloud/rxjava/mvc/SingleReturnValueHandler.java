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
package com.taotao.cloud.rxjava.mvc;

import com.taotao.cloud.rxjava.async.SingleDeferredResult;
import io.reactivex.Single;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * A specialized {@link AsyncHandlerMethodReturnValueHandler} that handles {@link Single} return types.
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:50:19
 */
public class SingleReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

	@Override
	public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
		return returnValue != null && supportsReturnType(returnType);
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return Single.class.isAssignableFrom(returnType.getParameterType());
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType,
		ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

		if (returnValue == null) {
			mavContainer.setRequestHandled(true);
			return;
		}

		final Single<?> single = Single.class.cast(returnValue);
		WebAsyncUtils.getAsyncManager(webRequest)
			.startDeferredResultProcessing(new SingleDeferredResult(single), mavContainer);
	}
}
