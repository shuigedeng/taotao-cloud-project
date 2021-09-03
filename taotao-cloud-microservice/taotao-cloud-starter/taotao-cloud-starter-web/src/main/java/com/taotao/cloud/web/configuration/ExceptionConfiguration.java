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
package com.taotao.cloud.web.configuration;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.exception.FeignException;
import com.taotao.cloud.common.exception.IdempotencyException;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.common.exception.MessageException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 全局统一返回值 包装器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:26:19
 */
@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller"}, annotations = {
		RestController.class, Controller.class})
//@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
//@RestControllerAdvice
public class ExceptionConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ExceptionConfiguration.class, StarterName.WEB_STARTER);
	}

	@ExceptionHandler({BaseException.class})
	public Result<String> baseException(NativeWebRequest req, BaseException e) {
		printLog(req, e);
		return Result.fail(e.getMessage(), e.getCode());
	}

	@ExceptionHandler({FeignException.class})
	public Result<String> feignException(NativeWebRequest req, FeignException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@ExceptionHandler({LockException.class})
	public Result<String> lockException(NativeWebRequest req, LockException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@ExceptionHandler({IdempotencyException.class})
	public Result<String> idempotencyException(NativeWebRequest req, IdempotencyException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@ExceptionHandler({BusinessException.class})
	public Result<String> businessException(NativeWebRequest req, BusinessException e) {
		printLog(req, e);
		return Result.fail(e.getMessage(), e.getCode());
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public Result<String> illegalArgumentException(NativeWebRequest req,
			IllegalArgumentException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.ILLEGAL_ARGUMENT_ERROR);
	}

	@ExceptionHandler({AccessDeniedException.class})
	public Result<String> badMethodExpressException(NativeWebRequest req, AccessDeniedException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.FORBIDDEN);
	}

	@ExceptionHandler({MessageException.class})
	public Result<String> badMessageException(NativeWebRequest req, MessageException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.MESSAGE_SEND_ERROR);
	}

	@ExceptionHandler({UsernameNotFoundException.class})
	public Result<String> badUsernameNotFoundException(NativeWebRequest req,
			UsernameNotFoundException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
	}

	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
	public Result<String> handleHttpRequestMethodNotSupportedException(NativeWebRequest req,
			HttpRequestMethodNotSupportedException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.METHOD_NOT_SUPPORTED_ERROR);
	}

	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	public Result<String> handleHttpMediaTypeNotSupportedException(NativeWebRequest req,
			HttpMediaTypeNotSupportedException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.MEDIA_TYPE_NOT_SUPPORTED_ERROR);
	}

	@ExceptionHandler({SQLException.class})
	public Result<String> handleSqlException(NativeWebRequest req, SQLException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	public Result<String> handleDataIntegrityViolationException(NativeWebRequest req,
			DataIntegrityViolationException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	/**
	 * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
	 */
	@ExceptionHandler(value = BindException.class)
	public Result<Map<String, String>> handleBindException(NativeWebRequest req, BindException e) {
		printLog(req, e);
		BindingResult bindingResult = e.getBindingResult();
		return Result.fail(getErrors(bindingResult));
	}

	/**
	 * RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<Map<String, String>> handleMethodArgumentNotValidException(NativeWebRequest req,
			MethodArgumentNotValidException e) {
		printLog(req, e);
		BindingResult bindingResult = e.getBindingResult();
		return Result.fail(getErrors(bindingResult));
	}

	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public Result<String> requestTypeMismatch(NativeWebRequest req,
			MethodArgumentTypeMismatchException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.METHOD_ARGUMENTS_TYPE_MISMATCH);
	}

	@ExceptionHandler({MissingServletRequestParameterException.class})
	public Result<String> requestMissingServletRequest(NativeWebRequest req,
			MissingServletRequestParameterException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.MISSING_SERVLET_REQUEST_PARAMETER);
	}

	@ExceptionHandler({HttpMessageNotReadableException.class})
	public Result<String> httpMessageNotReadableException(NativeWebRequest req,
			HttpMessageNotReadableException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.HTTP_MESSAGE_NOT_READABLE);
	}

	@ExceptionHandler(ValidationException.class)
	public Result<String> handleException(NativeWebRequest req, ValidationException e) {
		printLog(req, e);
		return Result.fail(ResultEnum.VERIFY_ARGUMENT_ERROR);
	}

	/**
	 * RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public Result<Map<String, String>> handleException(NativeWebRequest req,
			ConstraintViolationException e) {
		printLog(req, e);
		return Result.fail(getErrors(e));
	}

	@ExceptionHandler(Exception.class)
	public Result<String> handleException(NativeWebRequest req, Exception e) {
		printLog(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	/**
	 * 获取请求路径
	 *
	 * @param request request
	 * @return {@link java.lang.String }
	 * @author shuigedeng
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
	 * @param request request
	 * @return {@link java.lang.String }
	 * @author shuigedeng
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
	 * getErrors
	 *
	 * @param result result
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 21:27:21
	 */
	private String getErrors(BindingResult result) {
		Map<String, String> map = new HashMap<>();
		List<FieldError> list = result.getFieldErrors();
		for (FieldError error : list) {
			map.put(error.getField(), error.getDefaultMessage());
		}
		return map.toString();
	}

	/**
	 * getErrors
	 *
	 * @param e e
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 21:27:27
	 */
	private String getErrors(ConstraintViolationException e) {
		Map<String, String> map = new HashMap<>();
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			String property = constraintViolation.getPropertyPath().toString();
			String message = constraintViolation.getMessage();
			map.put(property, message);
		}
		return map.toString();
	}

	/**
	 * printLog
	 *
	 * @param req req
	 * @param e   e
	 * @author shuigedeng
	 * @since 2021-09-02 21:27:34
	 */
	private void printLog(NativeWebRequest req, Exception e) {
		LogUtil.error("【全局异常拦截】{}: 请求路径: {}, 请求参数: {}, 异常信息 {} ", e,
				e.getClass().getName(), uri(req), query(req), e.getMessage());
//		LogUtil.error(e.getMessage(), e);
	}
}

