/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import java.io.Serializable;

/**
 * 基础异常
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:29:47
 */
public class BaseException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 6610083281801529147L;

	/**
	 * 异常码
	 */
	private final Integer code;

	/**
	 * 返回信息
	 */
	private final String message;

	public BaseException(String message) {
		super(message);
		this.code = ResultEnum.ERROR.getCode();
		this.message = message;
	}

	public BaseException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BaseException(Throwable e) {
		super(e);
		this.code = ResultEnum.ERROR.getCode();
		this.message = ResultEnum.ERROR.getDesc();
	}

	public BaseException(String message, Throwable e) {
		super(message, e);
		this.code = ResultEnum.ERROR.getCode();
		this.message = message;
	}

	public BaseException(Integer code, String message, Throwable e) {
		super(message, e);
		this.code = code;
		this.message = message;
	}

	public BaseException(ResultEnum result) {
		super(result.getDesc());
		this.message = result.getDesc();
		this.code = result.getCode();
	}

	public BaseException(ResultEnum result, Throwable e) {
		super(result.getDesc(), e);
		this.message = result.getDesc();
		this.code = result.getCode();
	}

	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
