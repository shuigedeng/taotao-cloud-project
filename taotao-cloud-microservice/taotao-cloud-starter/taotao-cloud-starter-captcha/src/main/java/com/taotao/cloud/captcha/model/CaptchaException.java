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
package com.taotao.cloud.captcha.model;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * CaptchaException 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:07
 */
public class CaptchaException extends BaseException {

	public CaptchaException(String message) {
		super(message);
	}

	public CaptchaException(Integer code, String message) {
		super(code, message);
	}

	public CaptchaException(String message, Throwable e) {
		super(message, e);
	}

	public CaptchaException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public CaptchaException(ResultEnum result) {
		super(result);
	}

	public CaptchaException(ResultEnum result, Throwable e) {
		super(result, e);
	}

	public CaptchaException(CaptchaCodeEnum result) {
		super(result.getDesc());
	}

	public CaptchaException(CaptchaCodeEnum result, Throwable e) {
		super(result.getDesc(), e);
	}
}
