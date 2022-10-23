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
package com.taotao.cloud.jetcache.exception;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * StampHasExpiredException
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-25 08:45
 */
public class StampHasExpiredException extends BaseException {

	public StampHasExpiredException(String message) {
		super(message);
	}

	public StampHasExpiredException(Integer code, String message) {
		super(code, message);
	}

	public StampHasExpiredException(Throwable e) {
		super(e);
	}

	public StampHasExpiredException(String message, Throwable e) {
		super(message, e);
	}

	public StampHasExpiredException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public StampHasExpiredException(ResultEnum result) {
		super(result);
	}

	public StampHasExpiredException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
