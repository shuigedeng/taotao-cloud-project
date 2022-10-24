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
package com.taotao.cloud.cache.jetcache.exception;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * StampParameterIllegalException
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-25 08:44
 */
public class StampParameterIllegalException extends BaseException {

	public StampParameterIllegalException(String message) {
		super(message);
	}

	public StampParameterIllegalException(Integer code, String message) {
		super(code, message);
	}

	public StampParameterIllegalException(Throwable e) {
		super(e);
	}

	public StampParameterIllegalException(String message, Throwable e) {
		super(message, e);
	}

	public StampParameterIllegalException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public StampParameterIllegalException(ResultEnum result) {
		super(result);
	}

	public StampParameterIllegalException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
