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
package com.taotao.cloud.tracing.common.exception;

import static com.taotao.cloud.common.enums.ResultEnum.NOT_FOUND;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;
import java.io.Serial;
import java.util.function.Supplier;

/**
 * BusinessException
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:09:13
 */
public class TracingCommonException extends BaseException {

	@Serial
	private static final long serialVersionUID = 6610083281801529147L;

	public TracingCommonException(String message) {
		super(message);
	}

	public TracingCommonException(Integer code, String message) {
		super(code, message);
	}

	public TracingCommonException(String message, Throwable e) {
		super(message, e);
	}

	public TracingCommonException(Throwable e) {
		super(e);
	}

	public TracingCommonException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public TracingCommonException(ResultEnum result) {
		super(result);
	}

	public TracingCommonException(ResultEnum result, Throwable e) {
		super(result, e);
	}


	public static Supplier<TracingCommonException> businessException(String msg) {
		return () -> new TracingCommonException(msg);
	}

	public static Supplier<TracingCommonException> notFound() {
		return () -> new TracingCommonException(NOT_FOUND);
	}

	public static TracingCommonException notFoundException() {
		return new TracingCommonException(NOT_FOUND);
	}
}
