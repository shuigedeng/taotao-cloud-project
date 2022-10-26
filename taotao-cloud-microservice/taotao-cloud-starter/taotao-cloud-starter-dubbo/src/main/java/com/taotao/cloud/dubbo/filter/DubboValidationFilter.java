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
package com.taotao.cloud.dubbo.filter;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.ConfigUtils;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.validation.Validation;
import org.apache.dubbo.validation.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

import static org.apache.dubbo.common.constants.FilterConstants.VALIDATION_KEY;

/**
 * CustomValidationFilter
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-10 13:36:55
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class DubboValidationFilter implements Filter {

	private Validation validation;

	public void setValidation(Validation validation) {
		this.validation = validation;
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		LogUtils.info("DubboValidationFilter activate ------------------------------");

		if (validation != null && !invocation.getMethodName().startsWith("$")
			&& ConfigUtils.isNotEmpty(
			invoker.getUrl().getMethodParameter(invocation.getMethodName(), VALIDATION_KEY))) {
			try {
				Validator validator = validation.getValidator(invoker.getUrl());
				if (validator != null) {
					validator.validate(invocation.getMethodName(), invocation.getParameterTypes(),
						invocation.getArguments());
				}
			} catch (RpcException e) {
				throw e;
			} catch (ConstraintViolationException e) {
				Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
				if (CollectionUtils.isNotEmpty(violations)) {
					ConstraintViolation<?> violation = violations.iterator().next();
					com.taotao.cloud.common.model.Result<String> result = com.taotao.cloud.common.model.Result.fail(
						violation.getMessage(), ResultEnum.ERROR.getCode());
					return AsyncRpcResult.newDefaultAsyncResult(result, invocation);
				}
				return AsyncRpcResult.newDefaultAsyncResult(new ValidationException(e.getMessage()),
					invocation);
			} catch (Throwable t) {
				return AsyncRpcResult.newDefaultAsyncResult(t, invocation);
			}
		}
		return invoker.invoke(invocation);
	}
}
