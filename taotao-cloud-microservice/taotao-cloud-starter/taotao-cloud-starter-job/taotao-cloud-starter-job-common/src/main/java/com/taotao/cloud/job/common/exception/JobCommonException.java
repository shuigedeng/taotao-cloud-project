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
package com.taotao.cloud.job.common.exception;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

import java.io.Serial;
import java.util.function.Supplier;

import static com.taotao.cloud.common.enums.ResultEnum.NOT_FOUND;

/**
 * BusinessException
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:09:13
 */
public class JobCommonException extends BaseException {

	@Serial
	private static final long serialVersionUID = 6610083281801529147L;

	public JobCommonException(String message) {
		super(message);
	}

	public JobCommonException(Integer code, String message) {
		super(code, message);
	}

	public JobCommonException(String message, Throwable e) {
		super(message, e);
	}

	public JobCommonException(Throwable e) {
		super(e);
	}

	public JobCommonException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public JobCommonException(ResultEnum result) {
		super(result);
	}

	public JobCommonException(ResultEnum result, Throwable e) {
		super(result, e);
	}


	public static Supplier<JobCommonException> businessException(String msg) {
		return () -> new JobCommonException(msg);
	}

	public static Supplier<JobCommonException> notFound() {
		return () -> new JobCommonException(NOT_FOUND);
	}

	public static JobCommonException notFoundException() {
		return new JobCommonException(NOT_FOUND);
	}
}
