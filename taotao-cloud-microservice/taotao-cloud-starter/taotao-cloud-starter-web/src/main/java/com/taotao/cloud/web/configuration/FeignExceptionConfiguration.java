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
package com.taotao.cloud.web.configuration;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.feign.annotation.FeignApi;
import com.taotao.cloud.feign.model.FeignExceptionResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.util.NestedServletException;

/**
 * feign统一返回异常 包装器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:26:19
 */
@AutoConfiguration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations ={FeignApi.class})
public class FeignExceptionConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(FeignExceptionConfiguration.class, StarterName.WEB_STARTER);
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public FeignExceptionResult handleBusinessException(NativeWebRequest req, BusinessException e) {
		printLog(req, e);
		return new FeignExceptionResult(e.getMessage());
	}

	@ExceptionHandler(NestedServletException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public FeignExceptionResult handleNestedServletException(NativeWebRequest req, NestedServletException e) {
		printLog(req, e);
		return new FeignExceptionResult(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public FeignExceptionResult handleException(NativeWebRequest req, Exception e) {
		printLog(req, e);
		return new FeignExceptionResult(e.getMessage());
	}

	/**
	 * 获取请求路径
	 *
	 * @param request 请求对象
	 * @return 请求路径
	 * @since 2021-09-02 21:27:08
	 */
	private String uri(NativeWebRequest request) {
		HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
		if (Objects.nonNull(nativeRequest)) {
			return nativeRequest.getRequestURI();
		} else {
			return "--";
		}
	}

	/**
	 * 获取请求参数
	 *
	 * @param request 请求对象
	 * @return 请求参数
	 * @since 2021-09-02 21:27:14
	 */
	private String query(NativeWebRequest request) {
		HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
		if (Objects.nonNull(nativeRequest)) {
			String queryString = nativeRequest.getQueryString();
			if (StrUtil.isNotBlank(queryString)) {
				return queryString;
			}
		}
		return "--";
	}

	/**
	 * 获取Binding错误数据
	 *
	 * @param result 请求对象
	 * @return 错误数据
	 * @since 2021-09-02 21:27:21
	 */
	private String getErrors(BindingResult result) {
		String errorMsg = "系统异常";

		Map<String, String> map = new HashMap<>();
		List<FieldError> list = result.getFieldErrors();
		for (FieldError error : list) {
			map.put(error.getField(), error.getDefaultMessage());

			errorMsg = error.getDefaultMessage();
		}
		//return JsonUtil.toJSONString(map);
		return errorMsg;
	}

	/**
	 * 获取校验错误数据
	 *
	 * @param e 异常信息
	 * @return 校验错误数据
	 * @since 2021-09-02 21:27:27
	 */
	private String getErrors(ConstraintViolationException e) {
		String errorMsg = "系统异常";
		Map<String, String> map = new HashMap<>();
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			String property = constraintViolation.getPropertyPath().toString();
			String message = constraintViolation.getMessage();
			map.put(property, message);

			errorMsg = message;
		}
		//return JsonUtil.toJSONString(map);
		return errorMsg;
	}

	/**
	 * 打印日志
	 *
	 * @param req 请求对象
	 * @param e   异常信息
	 * @since 2021-09-02 21:27:34
	 */
	private void printLog(NativeWebRequest req, Exception e) {
		LogUtil.error(e);
		LogUtil.error("【全局异常拦截】{}: 请求路径: {}, 请求参数: {}, 异常信息 {} ", e,
			e.getClass().getName(), uri(req), query(req), e.getMessage());
	}
}

