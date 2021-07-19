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
package com.taotao.cloud.common.exception;

import com.taotao.cloud.common.enums.ResultEnum;

/**
 * 消息异常
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/13 09:53
 */
public class MessageException extends BaseException {

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Integer code, String message) {
		super(code, message);
	}

	public MessageException(String message, Throwable e) {
		super(message, e);
	}

	public MessageException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public MessageException(ResultEnum result) {
		super(result);
	}

	public MessageException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
