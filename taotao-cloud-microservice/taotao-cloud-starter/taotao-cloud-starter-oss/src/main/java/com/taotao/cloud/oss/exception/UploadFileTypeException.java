/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.oss.exception;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * 业务异常
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/2 11:21
 */
public class UploadFileTypeException extends BaseException {

	private static final long serialVersionUID = 6610083281801529147L;

	public UploadFileTypeException(String message) {
		super(message);
	}

	public UploadFileTypeException(Integer code, String message) {
		super(code, message);
	}

	public UploadFileTypeException(String message, Throwable e) {
		super(message, e);
	}

	public UploadFileTypeException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public UploadFileTypeException(ResultEnum result) {
		super(result);
	}

	public UploadFileTypeException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
