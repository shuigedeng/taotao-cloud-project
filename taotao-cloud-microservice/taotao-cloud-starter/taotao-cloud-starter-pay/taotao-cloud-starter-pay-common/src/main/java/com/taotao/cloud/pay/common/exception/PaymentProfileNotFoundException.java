/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.pay.common.exception;

import com.taotao.cloud.common.enums.ResultEnum;

/**
 * <p>Description: Profile 未找到 HerodotusException </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 21:01
 */
public class PaymentProfileNotFoundException extends PaymentException {

	public PaymentProfileNotFoundException(String message) {
		super(message);
	}

	public PaymentProfileNotFoundException(Integer code, String message) {
		super(code, message);
	}

	public PaymentProfileNotFoundException(Throwable e) {
		super(e);
	}

	public PaymentProfileNotFoundException(String message, Throwable e) {
		super(message, e);
	}

	public PaymentProfileNotFoundException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public PaymentProfileNotFoundException(ResultEnum result) {
		super(result);
	}

	public PaymentProfileNotFoundException(ResultEnum result,
		Throwable e) {
		super(result, e);
	}
}
