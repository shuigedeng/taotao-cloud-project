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
package com.taotao.cloud.web.quartz;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * QuartzExectionExecption
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/01/17 09:05
 */
public class QuartzExecutionExecution extends BaseException {

	public QuartzExecutionExecution(String message) {
		super(message);
	}

	public QuartzExecutionExecution(Integer code, String message) {
		super(code, message);
	}

	public QuartzExecutionExecution(Throwable e) {
		super(e);
	}

	public QuartzExecutionExecution(String message, Throwable e) {
		super(message, e);
	}

	public QuartzExecutionExecution(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public QuartzExecutionExecution(ResultEnum result) {
		super(result);
	}

	public QuartzExecutionExecution(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
