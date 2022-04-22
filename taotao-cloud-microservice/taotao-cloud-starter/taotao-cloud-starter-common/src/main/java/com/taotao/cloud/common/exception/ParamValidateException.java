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
package com.taotao.cloud.common.exception;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.ValidatorConstants;
import com.taotao.cloud.common.enums.ResultEnum;

/**
 * 参数校验异常
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-22 10:49:21
 */
public class ParamValidateException extends BusinessException {

	public ParamValidateException(String message) {
		super(message);
	}

	public ParamValidateException(Integer code, String message) {
		super(code, message);
	}

	public ParamValidateException(String message, Throwable e) {
		super(message, e);
	}

	public ParamValidateException(Throwable e) {
		super(e);
	}

	public ParamValidateException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public ParamValidateException(ResultEnum result) {
		super(result);
	}

	public ParamValidateException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
